package dette.boutique.data.repository.bdImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repo.impl.RepositoryDBImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;

public class DetteRepositoryDBImpl extends RepositoryDBImpl<Dette> implements DetteRepository {
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;

    public DetteRepositoryDBImpl(ClientRepository clientRepository, ArticleRepository articleRepository) {
        this.tableName = "dettes";
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
    }

    public int insertDette(Dette dette) {
        if (dette.getClient() == null || dette.getClient().getId() <= 0) {
            System.out.println("Erreur : Client invalide ou non spécifié.");
            return -1;
        }
        String query = "INSERT INTO dettes (date, montant, montant_verse, archivee, client_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = connexion();
            PreparedStatement ps = conn.prepareStatement(query)) {
            if (dette.getDate() != null) {
                ps.setDate(1, new java.sql.Date(dette.getDate().getTime()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            ps.setInt(2, dette.getMontant());
            ps.setInt(3, dette.getMontantVerse());
            ps.setBoolean(4, false);
            ps.setInt(5, dette.getClient().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                dette.setId(id);
                return id;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la dette : " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean insert(Dette dette) {
        int id = insertDette(dette);
        if (id > 0) {
            dette.setId(id);
            try (Connection conn = connexion()) {
                insertDetails(conn, dette);
                return true;
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion des détails pour la dette ID: " + dette.getId() + " - " + e.getMessage());
            }
        } else {
            System.out.println("Erreur : l'ID de la dette n'a pas été généré correctement.");
        }
        return false;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Dette dette) throws SQLException {
        if (dette.getDate() != null) {
            ps.setDate(4, new java.sql.Date(dette.getDate().getTime()));
        } else {
            ps.setNull(4, java.sql.Types.DATE);
        }
        ps.setInt(1, dette.getMontant());
        ps.setInt(2, dette.getMontantVerse());
        if (dette.getClient() != null) {
            ps.setInt(3, dette.getClient().getId());
        } else {
            ps.setNull(3, java.sql.Types.INTEGER);
        }
    }

    @Override
    protected String getTableName() {
        return this.tableName;
    }

    @Override
    protected String generateInsertQuery() {
        return String.format("INSERT INTO %s (date, montant, montant_verse, client_id) VALUES (?, ?, ?, ?)", this.tableName);
    }

    @Override
    protected void setIdFromResultSet(Dette dette, ResultSet rs) throws SQLException {
        dette.setId(rs.getInt(1));
    }

    private void insertDetails(Connection conn, Dette dette) throws SQLException {
        String queryDetails = "INSERT INTO details (prix_total, qte, article_id, dette_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(queryDetails)) {
            for (Details detail : dette.getDetails()) {
                ps.setInt(1, detail.getPrixTotal());
                ps.setInt(2, detail.getQte());
                ps.setInt(3, detail.getArticle().getId());
                ps.setInt(4, dette.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    protected Dette convertToObject(ResultSet rs) throws SQLException {
        Dette dette = new Dette();
        dette.setId(rs.getInt("id"));
        dette.setMontant(rs.getInt("montant"));
        dette.setMontantVerse(rs.getInt("montant_verse"));
        dette.setDate(rs.getDate("date"));
        int clientId = rs.getInt("client_id");
        if (clientId > 0) {
            Client client = clientRepository.findById(clientId);
            dette.setClient(client);
        }
        dette.setDate(java.sql.Date.valueOf(rs.getDate("date").toLocalDate()));
        dette.setDetails(getDetailsByDetteId(dette.getId()));
        return dette;
    }

    private List<Details> getDetailsByDetteId(int detteId) {
        List<Details> detailsList = new ArrayList<>();
        String query = "SELECT * FROM details WHERE dette_id = ?";
        try (Connection conn = connexion(); 
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Details detail = new Details();
                    detail.setPrixTotal(rs.getInt("prix_total"));
                    detail.setArticle(articleRepository.findById(rs.getInt("articleId")));
                    detailsList.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailsList;
    }

    @Override
    public List<Dette> findByClient(Client client) {
        List<Dette> dettes = new ArrayList<>();
        String query = "SELECT * FROM " + this.tableName + " WHERE client_id = ?";
        try (Connection conn = connexion();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, client.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dettes.add(convertToObject(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dettes pour le client ID: " + client.getId() + " - " + e.getMessage());
        }

        return dettes;
    }

    @Override
    public void ajouterPaiement(Paiement paiement) {
        String query = "INSERT INTO paiements (montant, date, dette_id) VALUES (?, ?, ?)";
        try (Connection conn = connexion();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, paiement.getMontantPaye());
            ps.setDate(2, new java.sql.Date(paiement.getDate().getTime()));
            ps.setInt(3, paiement.getDette().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du paiement : " + e.getMessage());
        }
    }

    @Override
    public boolean articlesDette(Dette dette) {
        String query = "SELECT * FROM details WHERE dette_id = ?";
        try (Connection conn = connexion();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dette.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Details detail = new Details();
                    detail.setPrixTotal(rs.getInt("prix_total"));
                    detail.setQte(rs.getInt("qte"));
                    detail.setArticle(articleRepository.findById(rs.getInt("articleId")));
                    dette.getDetails().add(detail);
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des articles pour la dette ID: " + dette.getId() + " - " + e.getMessage());
        }
        return false;
    }
    
    @Override
    protected void setUpdateParameters(PreparedStatement ps, Dette dette) throws SQLException {
        ps.setInt(1, dette.getMontant());
        ps.setInt(2, dette.getMontantVerse());
        // ps.setInt(3, dette.getMontantRestant());
        if (dette.getClient() != null) {
            ps.setInt(3, dette.getClient().getId());
        } else {
            ps.setNull(3, java.sql.Types.INTEGER);
        }
        if (dette.getDate() != null) {
            ps.setDate(4, new java.sql.Date(dette.getDate().getTime()));
        } else {
            ps.setNull(4, java.sql.Types.DATE);
        }
        ps.setInt(5, dette.getId());
    }

    @Override
    protected String generateSelectQuery() {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    protected String generateUpdateQuery() {
        return String.format("UPDATE %s SET montant = ?, montant_verse = ?, client_id = ?, date = ? WHERE id = ?", this.tableName);
    }
    
}