package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.repo.impl.RepositoryDBImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public class ClientRepositoryDBImpl extends RepositoryDBImpl<Client> implements ClientRepository {
    // private final UserRepository userRepository;

    public ClientRepositoryDBImpl() {
        this.tableName = "clients";
    }

    @Override
    public boolean insert(Client client) {
        String query = generateInsertQuery();
        try (PreparedStatement ps = connexion().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(ps, client);
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + client.getSurname() + " - " + e.getMessage());
        }
        return false;
    }

    @Override
    public Client findByTel(String telephone) {
        Client client = null;
        String query = "SELECT * FROM " + this.tableName + " WHERE telephone = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    client = convertToObject(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du client avec le téléphone : " + telephone + " - " + e.getMessage());
        }
        return client;
    }

    @Override
    public boolean insertWithoutUser(Client client) {
        String query = "INSERT INTO " + this.tableName + " (surname, adresse, telephone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, client.getSurname());
            ps.setString(2, client.getAdresse());
            ps.setString(3, client.getTelephone());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client sans utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateUserForClient(Client client) {
        if (client == null || client.getId() == 0) {
            System.out.println("Client invalide pour la mise à jour.");
            return false;
        }
    
        String query = "UPDATE " + this.tableName + " SET user_id = ? WHERE id = ?";
    
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            // Mettre à jour le user_id ou le définir comme NULL
            if (client.getUser() != null && client.getUser().getId() != 0) {
                ps.setInt(1, client.getUser().getId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
    
            ps.setInt(2, client.getId());
    
            int rowsUpdated = ps.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour le client ID: " + client.getId());
                return true;
            } else {
                System.out.println("Aucune ligne mise à jour. Client ID introuvable.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour de l'utilisateur pour le client ID: "
                    + client.getId() + " - " + e.getMessage());
        }
    
        return false;
    }

    @Override
    public Client convertToObject(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setSurname(rs.getString("surname"));
        client.setAdresse(rs.getString("adresse"));
        client.setTelephone(rs.getString("telephone"));
        User user = new User();
        user.setId(rs.getInt("user_id"));
        // user.setPrenom(rs.getString("prenom"));
        // user.setNom(rs.getString("nom"));
        // user.setLogin(rs.getString("login"));
        // user.setPassword(rs.getString("password"));
        // client.setUser(user);
        // int userId = rs.getInt("userId");
        // if (userId > 0) {
        //     client.setUser(userRepository.findById(userId));
        // }
        return client;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getSurname());
        ps.setString(2, client.getAdresse());
        ps.setString(3, client.getTelephone());
        if (client.getUser() != null) {
            ps.setInt(4, client.getUser().getId());
        } else {
            ps.setNull(4, java.sql.Types.INTEGER);
        }
    }

    @Override
    protected String getTableName() {
        return this.tableName;
    }

    @Override
    protected String generateInsertQuery() {
        return String.format("INSERT INTO %s (surname, adresse, telephone, user_id) VALUES (?, ?, ?, ?)", this.tableName);
    }

    @Override
    protected void setIdFromResultSet(Client client, ResultSet rs) throws SQLException {
        client.setId(rs.getInt(1));
    }

    @Override
    protected String generateSelectQuery() {
        return "SELECT * FROM " + this.tableName;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getSurname());
        ps.setString(2, client.getAdresse());
        ps.setString(3, client.getTelephone());
        if (client.getUser() != null) {
            ps.setInt(4, client.getUser().getId());
        } else {
            ps.setNull(4, java.sql.Types.INTEGER);
        }
        ps.setInt(5, client.getId());
    }

    @Override
    protected String generateUpdateQuery() {
        return String.format("UPDATE %s SET surname = ?, adresse = ?, telephone = ?, userId = ? WHERE id = ?", this.tableName);
    }

    @Override
    public void updateClient(int id, int userId) {
        String query = "UPDATE " + this.tableName + " SET user_id = ? WHERE id = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            if (userId != 0) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client ID " + id + " mis à jour avec succès.");
            } else {
                System.out.println("Aucun client trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du client ID: " + id + " - " + e.getMessage());
        }
    }
    
}