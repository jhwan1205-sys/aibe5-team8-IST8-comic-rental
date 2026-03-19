package org.example.rental;

import org.example.db.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {
    @Override
    public long save(Rental rental) {
        String sql = "INSERT INTO rental (comicId, memberId, rentalDate) VALUES (?, ?, ?)";

        // 세번째 인자로 RETURN_GENERATED_KEYS 추가
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, rental.getComicId());
            pstmt.setLong(2, rental.getMemberId());
            pstmt.setObject(3, rental.getRentalDate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return 0; // 저장 실패
            }

            // 생성된 키(ID) 꺼내기
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    // 자바 객체에 ID 세팅 (동기화)
                    rental.setId(id);
                    return id; // 실제 DB ID 반환
                } else {
                    return 0;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Rental findByRentalId(long rentalid) {
        String sql = "SELECT * FROM rental WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, rentalid);
            try(ResultSet rs = pstmt.executeQuery()){
                // 결과가 하나라도 있다면,
                if(rs.next()){
                    Rental rental = new Rental();
                    rental.setId(rs.getLong("id"));
                    rental.setComicId(rs.getLong("comicId"));
                    rental.setMemberId(rs.getLong("memberId"));
                    rental.setRentalDate(rs.getObject("rentalDate", LocalDate.class));
                    rental.setReturnDate(rs.getObject("returnDate", LocalDate.class));

                    return rental;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rental> findAll() {
        List<Rental> rentalList = new ArrayList<>();
        String sql = "SELECT * FROM rental";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                Rental rental = new Rental();
                rental.setId(rs.getLong("id"));
                rental.setComicId(rs.getLong("comicId"));
                rental.setMemberId(rs.getLong("memberId"));
                rental.setRentalDate(rs.getObject("rentalDate", LocalDate.class));
                rental.setReturnDate(rs.getObject("returnDate", LocalDate.class));
                rentalList.add(rental);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return rentalList;
    }

    @Override
    public void update(Rental rental) {
        String sql = "UPDATE rental SET returnDate = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setObject(1, rental.getReturnDate());
            pstmt.setLong(2, rental.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
