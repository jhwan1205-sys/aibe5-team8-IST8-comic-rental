package org.example.comic;

import org.example.db.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcComicRepository implements ComicRepository {

    @Override
    public long addComic(String title, int volume, String author) {
        final String sql = """
                INSERT INTO comic (title, volume, author, isRented)
                VALUES (?, ?, ?, ?)
                """;
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            return -1L;
        }
        try (
                Connection c = conn;
                PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, title);
            pstmt.setInt(2, volume);
            pstmt.setString(3, author);
            pstmt.setBoolean(4, false);

            int updated = pstmt.executeUpdate();
            if (updated != 1) {
                return -1L;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @Override
    public List<Comic> getComics() {
        List<Comic> comics = new ArrayList<>();
        final String sql = """
                SELECT id, title, volume, author, isRented, regdate
                FROM comic
                ORDER BY id
                """;

        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            return comics;
        }

        try (
                Connection c = conn;
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                comics.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comics;
    }

    @Override
    public Optional<Comic> findById(long id) {
        final String sql = """
                SELECT id, title, volume, author, isRented, regdate
                FROM comic
                WHERE id = ?
                """;

        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            return Optional.empty();
        }

        try (
                Connection c = conn;
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updateComic(long id, String title, int volume, String author) {
        final String sql = """
                UPDATE comic
                SET title = ?, volume = ?, author = ?
                WHERE id = ?
                """;

        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            return false;
        }

        try (
                Connection c = conn;
                PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, volume);
            pstmt.setString(3, author);
            pstmt.setLong(4, id);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(long id) {
        final String sql = "DELETE FROM comic WHERE id = ?";
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            return false;
        }

        try (
                Connection c = conn;
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Comic mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        int volume = rs.getInt("volume");
        String author = rs.getString("author");
        boolean isRented = rs.getBoolean("isRented");
        java.sql.Date sqlRegDate = rs.getDate("regdate");
        LocalDate regDate = (sqlRegDate == null) ? null : sqlRegDate.toLocalDate();
        return new Comic(id, title, volume, author, isRented, regDate);
    }
}
