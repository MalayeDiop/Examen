package dette.boutique.data.repository.bdImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.repo.impl.RepositoryDBImpl;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.interfaces.DetailRepository;

public class DetailRepositoryDBImpl extends RepositoryDBImpl<Details> implements DetailRepository {

    public DetailRepositoryDBImpl() {
        this.tableName = "details";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Details object) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInsertParameters'");
    }

    @Override
    protected void setIdFromResultSet(Details object, ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIdFromResultSet'");
    }

    @Override
    protected String getTableName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTableName'");
    }

    @Override
    protected String generateInsertQuery() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateInsertQuery'");
    }

    @Override
    protected Details convertToObject(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }

    @Override
    protected String generateSelectQuery() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSelectQuery'");
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Details object) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpdateParameters'");
    }

    @Override
    protected String generateUpdateQuery() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateUpdateQuery'");
    }
    
}