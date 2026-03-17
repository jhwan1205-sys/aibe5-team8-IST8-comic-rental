package org.example.member;

import org.example.db.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    public int addMember(String name, String phone) {
        String sql = """
                INSERT INTO member(name, phone, regdate)
                VALUES (?, ?, CURDATE())
                """;
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //AUTO_INCREMENT id 가져오기
        ) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return id;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
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
            e.printStackTrace();
        }
        return members;
    }
}
