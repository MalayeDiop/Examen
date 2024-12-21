package dette.boutique.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database<T> {
    Connection connexion() throws SQLException;

    void deconnexion() throws SQLException;

    ResultSet executeQuery() throws SQLException;

    int executeUpdate() throws SQLException;

    void setFields(PreparedStatement pstmt, T element) throws SQLException;

    String generateSql(T element);

    void init(String sql) throws SQLException;
}