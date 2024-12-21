package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.repo.impl.RepositoryDBImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.interfaces.ArticleRepository;

public class ArticleRepositoryDBImpl extends RepositoryDBImpl<Article> implements ArticleRepository {

    public ArticleRepositoryDBImpl() {
        this.tableName = "articles";
    }

    @Override
    public boolean insert(Article article) {
        String query = generateInsertQuery();
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            setInsertParameters(ps, article);
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'article : " + article.getLibelle() + " - " + e.getMessage());
            return false;
        }
    }

    @Override
    public Article findByLibelle(String libelle) {
        Article article = null;
        String query = "SELECT * FROM " + this.tableName + " WHERE libelle = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    article = convertToObject(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'article avec libellé : " + libelle + " - " + e.getMessage());
        }
        return article;
    }

    @Override
    public boolean updateQteStock(String libelle, int qte) {
        String query = "UPDATE " + this.tableName + " SET qte_stock = qte_stock + ? WHERE libelle = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setInt(1, qte);
            ps.setString(2, libelle);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la quantité en stock pour l'article avec libellé : " + libelle + " - " + e.getMessage());
        }
        return false;
    }

    @Override
    public Article convertToObject(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setLibelle(rs.getString("libelle"));
        article.setQteStock(rs.getInt("qte_stock"));
        article.setPrixUnitaire(rs.getInt("prix_unitaire"));
        return article;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Article article) throws SQLException {
        ps.setString(1, article.getLibelle());
        ps.setInt(2, article.getPrixUnitaire());
        ps.setInt(3, article.getQteStock());
    }

    @Override
    protected void setIdFromResultSet(Article article, ResultSet rs) throws SQLException {
        article.setId(rs.getInt(1));
    }

    @Override
    protected String getTableName() {
        return this.tableName;
    }

    @Override
    protected String generateInsertQuery() {
        return String.format("INSERT INTO %s (libelle, prix_unitaire, qte_stock) VALUES (?, ?, ?)", this.tableName);
    }

    @Override
    protected String generateSelectQuery() {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Article article) throws SQLException {
        ps.setString(1, article.getLibelle());
        ps.setInt(2, article.getPrixUnitaire());
        ps.setInt(3, article.getQteStock());
        ps.setInt(4, article.getId());
    }

    @Override
    protected String generateUpdateQuery() {
        return String.format("UPDATE %s SET libelle = ?, prix_unitaire = ?, qte_stock = ? WHERE id = ?", this.tableName);
    }
    
}