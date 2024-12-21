package dette.boutique.core.repo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.impl.DatabaseImpl;
import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.Identifiable;

public abstract class RepositoryDBImpl<T> extends DatabaseImpl<T> implements Repository<T> {
    protected String tableName;
    public String convertLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    protected abstract void setInsertParameters(PreparedStatement ps, T object) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement ps, T object) throws SQLException;
    protected abstract void setIdFromResultSet(T object, ResultSet rs) throws SQLException;
    protected abstract String getTableName();
    protected abstract String generateInsertQuery();
    protected abstract String generateSelectQuery();
    protected abstract String generateUpdateQuery();
    protected abstract T convertToObject(ResultSet rs) throws SQLException;

    @Override
    public T findById(int id) {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = connexion();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertToObject(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(T object) {
        String query = generateInsertQuery();
        try (Connection connection = connexion(); 
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(ps, object);
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                setIdFromResultSet(object,rs);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion : " + e.getMessage());
        } 
        return false;
    }

    @Override
    public void remove(T object) {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = connexion();
            PreparedStatement ps = connection.prepareStatement(query)) {
            
            int id = ((Identifiable) object).getId();
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<T> selectAll() {
        List<T> list = new ArrayList<>();
        String query = generateSelectQuery();
        try (Connection connection = connexion();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                T entity = convertToObject(rs);
                list.add(entity);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données : " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean update(T element) {
        String query = generateUpdateQuery();
        try (Connection connection = connexion(); 
            PreparedStatement ps = connection.prepareStatement(query)) {
            setUpdateParameters(ps, element); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}