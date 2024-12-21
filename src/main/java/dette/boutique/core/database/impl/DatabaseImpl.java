package dette.boutique.core.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.database.Database;
import dette.boutique.data.entities.User;

public abstract class DatabaseImpl<T> implements Database<T> {
    private final String URL = "jdbc:postgresql://localhost:5432/gestion_dettes";
    private final String USER = "postgres";
    private final String PASSWORD = "1012";
    protected Connection connection = null;
    protected PreparedStatement ps = null;

    @Override
    public Connection connexion() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à la base de données.");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver JDBC PostgreSQL non trouvé.");
            throw new SQLException("Le driver JDBC est introuvable.", e);
        } catch (SQLException e) {
            System.err.println("Erreur : Impossible de se connecter à la base de données.");
            throw new SQLException("Connexion à la base de données échouée.", e);
        }
        return connection;
    }

    @Override
    public void deconnexion() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return ps.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return ps.executeUpdate();
    }

    @Override
    public String generateSql(T element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init(String sql) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("La connexion à la base de données n'est pas initialisée.");
        }
    
        String sqlUpperCase = sql.toUpperCase().trim();
        if (sqlUpperCase.startsWith("INSERT")) {
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } else {
            ps = connection.prepareStatement(sql);
        }
    }

    @Override
    public void setFields(PreparedStatement pstmt, T element) throws SQLException {
        if (element instanceof User user) {
        pstmt.setString(1, user.getLogin());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getRole().toString());
        } else {
            throw new IllegalArgumentException("Type non supporté : " + element.getClass().getName());
        }
    }
}