package model.repository;

import model.PlasmaService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class SQLiteRepository implements ResultRepository {
    private static final String URL = "jdbc:sqlite:plasma_results.db";

    public SQLiteRepository() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS history (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "algorithm TEXT, " +
                         "generator TEXT, " +
                         "hash BLOB," +
                         "value BLOB, " +
                         "duration INTEGER, " +
                         "created_at TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveResult(PlasmaService.ProcessedFrame processedFrame) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO history(algorithm, generator, hash, value, duration, created_at) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, processedFrame.algorithmName());
            pstmt.setString(2, processedFrame.generatorName());
            pstmt.setBytes(3,processedFrame.hash());
            pstmt.setBytes(4, processedFrame.generatedResult());
            pstmt.setLong(5, processedFrame.time());
            pstmt.setString(6, LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveResultAsList(List<PlasmaService.ProcessedFrame> result) {
        try (Connection conn = DriverManager.getConnection(URL)){
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO history(algorithm, generator, hash, value, duration, created_at) VALUES(?,?,?,?,?,?)")) {

                for (PlasmaService.ProcessedFrame frame : result) {
                    stmt.setString(1, frame.algorithmName());
                    stmt.setString(2, frame.generatorName());
                    stmt.setBytes(3, frame.hash());
                    stmt.setBytes(4, frame.generatedResult());
                    stmt.setLong(5, frame.time());
                    stmt.setString(6, LocalDateTime.now().toString());
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
            }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}