package khannps39199.khannps39199.Controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import khannps39199.khannps39199.Model.ColumnInfo;
import khannps39199.khannps39199.Model.ForeignKeyInfo;

@Service

public class GetAllTables {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    SQLServerDataSource ds;

    public List<ColumnInfo> getTableColumns(String tableName) throws SQLException {

        List<ColumnInfo> columns = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tableName, null);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME"); // e.g., VARCHAR, INT
                columns.add(new ColumnInfo(columnName, columnType));
            }
        }
        return columns;
    }

    public List<ForeignKeyInfo> getExportedForeignKeys(SQLServerDataSource connect, String tableName)
            throws SQLException {
        List<ForeignKeyInfo> exportedForeignKeys = new ArrayList<>();
        try (Connection conn = connect.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getExportedKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                String fkColumnName = rs.getString("FKCOLUMN_NAME"); // cột trong bảng hiện tại
                String pkTableName = rs.getString("PKTABLE_NAME"); // bảng được tham chiếu
                String pkColumnName = rs.getString("PKCOLUMN_NAME"); // cột khóa chính của bảng được tham chiếu

                exportedForeignKeys.add(new ForeignKeyInfo(fkColumnName, pkTableName, pkColumnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exportedForeignKeys;
    }

    public List<ForeignKeyInfo> getImportedForeignKeys(SQLServerDataSource connect, String tableName)
            throws SQLException {
        List<ForeignKeyInfo> importedForeignKeys = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getImportedKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                String fkColumnName = rs.getString("FKCOLUMN_NAME"); // cột trong bảng hiện tại
                String pkTableName = rs.getString("PKTABLE_NAME"); // bảng được tham chiếu
                String pkColumnName = rs.getString("PKCOLUMN_NAME"); // cột khóa chính của bảng được tham chiếu
                importedForeignKeys.add(new ForeignKeyInfo(fkColumnName, pkTableName, pkColumnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importedForeignKeys;
    }

    public List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "%", new String[] { "TABLE" });
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    public List<String> getAllDatabases() {
        List<String> databases = new ArrayList<>();
        String sql = "SELECT name FROM sys.databases";

        try (
                Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                databases.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return databases;
    }

}
