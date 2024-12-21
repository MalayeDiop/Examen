package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repo.impl.RepositoryDBImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public class UserRepositoryDBImpl extends RepositoryDBImpl<User> implements UserRepository {
    private final ClientRepository clientRepository;

    public UserRepositoryDBImpl(ClientRepository clientRepository) {
        this.tableName = "users";
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean insert(User user) {
        String query = "INSERT INTO " + this.tableName + " (prenom, nom, login, password, role, active, client_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, user.getPrenom());
            ps.setString(2, user.getNom());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole().name());
            ps.setBoolean(6, user.isActive());
            if (user.getClient() != null) {
                ps.setInt(7, user.getClient().getId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertWithoutClient(User user) {
        String query = "INSERT INTO " + this.tableName + " (prenom, nom, login, password, role, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, user.getPrenom());
            ps.setString(2, user.getNom());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole().name());
            ps.setBoolean(6, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur sans client : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateClientForUser(User user) {
        String query = "UPDATE " + this.tableName + " SET client_id = ? WHERE id = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            if (user.getClient() != null) {
                ps.setInt(1, user.getClient().getId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, user.getId());
            return ps.executeUpdate() > 0; 
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du client de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    @Override
    public User selectByLogin(String login) {
        User user = null;
        String query = "SELECT * FROM " + this.tableName + " WHERE login = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = convertToObject(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur par login : " + e.getMessage());
        }
        return user;
    }

    @Override
    public User convertToObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        String roleName = rs.getString("role_name");
        user.setRole(Role.valueOf(roleName));
        if (rs.getBoolean("active") == true) {
            user.setActif(true);
        } else {
            user.setActif(false);
        }
        // Client client = new Client();
        // // client.setId(rs.getInt("clientid"));
        // user.setClient(client);

        int clientId = rs.getInt("client_id");
        if (clientId > 0) {
            user.setClient(clientRepository.findById(clientId));
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        String query = "UPDATE " + this.tableName + " SET prenom = ?, nom = ?, login = ?, password = ?, client_id = ?, active = ? WHERE id = ?";
        try (PreparedStatement ps = connexion().prepareStatement(query)) {
            ps.setString(1, user.getPrenom());
            ps.setString(2, user.getNom());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getPassword());
            if (user.getClient() != null) {
                ps.setInt(5, user.getClient().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setBoolean(6, user.isActive());
            ps.setInt(7, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getPrenom());
        ps.setString(2, user.getNom());
        ps.setString(3, user.getLogin());
        ps.setString(4, user.getPassword());
        if (user.getClient() != null) {
            ps.setInt(5, user.getClient().getId());
        } else {
            ps.setNull(5, java.sql.Types.INTEGER);
        }
    }

    @Override
    protected void setIdFromResultSet(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt(1));
    }

    @Override
    protected String getTableName() {
        return this.tableName;
    }

    @Override
    protected String generateInsertQuery() {
        return String.format("INSERT INTO %s (prenom, nom, login, password, client_id, role) VALUES (?, ?, ?, ?, ?, ?)", this.tableName);
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getPrenom());
        ps.setString(2, user.getNom());
        ps.setString(3, user.getLogin());
        ps.setString(4, user.getPassword());
        if (user.getClient() != null) {
            ps.setInt(5, user.getClient().getId());
        } else {
            ps.setNull(5, java.sql.Types.INTEGER);
        }
        ps.setInt(6, user.getId());
    }

    @Override
    protected String generateSelectQuery() {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    protected String generateUpdateQuery() {
        return "UPDATE " + getTableName() + " SET prenom = ?, nom = ?, login = ?, password = ?, client_id = ? WHERE id = ?";
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = generateSelectQuery();
        try (PreparedStatement ps = connexion().prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(convertToObject(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les utilisateurs : " + e.getMessage());
        }
        return users;
    }

}