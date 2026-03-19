package org.example.member;

import org.example.db.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    public Long addMember(String name, String phone) {
        String sql = """
            INSERT INTO member(name, phone)
            VALUES (?, ?)
            """;

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB 오류 발생", e);
        }

        throw new RuntimeException("ID 생성 실패");
    }

    public List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "select * from member";

        try(
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while(rs.next()) {
                Member member = new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getObject("regdate", LocalDate.class)
                );
                members.add(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB 조회 중 오류 발생", e);
        }
        return members;
    }
}
