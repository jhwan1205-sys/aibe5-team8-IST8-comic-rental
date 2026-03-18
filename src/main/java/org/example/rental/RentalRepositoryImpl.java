package org.example.rental;

import org.example.db.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {
    @Override
    public int save(Rental rental) {
        String sql = "INSERT INTO rental (comicId, memberId, rentalDate) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setLong(1, rental.getComicId());
            pstmt.setLong(2, rental.getMemberId());
            pstmt.setObject(3, rental.getRentalDate());

            return pstmt.executeUpdate();
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

            int result = pstmt.executeUpdate();

            if(result > 0){
                System.out.println(rental.getId() + " 번 대여 기록이 수정되었습니다.");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
