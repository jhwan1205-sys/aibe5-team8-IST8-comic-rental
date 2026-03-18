package org.example.rental;

import org.example.db.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {
    @Override
    public int save(Rental rental) {
        String sql = "INSERT INTO rentals (comic_id, member_id, rental_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setLong(1, rental.getComicId());
            pstmt.setLong(2, rental.getMemberId());
            pstmt.setDate(3, rental.getRentalDate());
            pstmt.setDate(4, rental.getReturnDate());

            return pstmt.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Rental findById(long id) {
        String sql = "SELECT * FROM rental WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                // 결과가 하나라도 있다면,
                if(rs.next()){
                    Rental rental = new Rental();
                    rental.setId(rs.getLong("id"));
                    rental.setComicId(rs.getLong("comic_id"));
                    rental.setMemberId(rs.getLong("member_id"));
                    rental.setRentalDate(rs.getDate("rental_date"));
                    rental.setReturnDate(rs.getDate("return_date"));

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
                rental.setComicId(rs.getLong("comic_id"));
                rental.setMemberId(rs.getLong("member_id"));
                rental.setRentalDate(rs.getDate("rental_date"));
                rental.setReturnDate(rs.getDate("return_date"));
                rentalList.add(rental);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return rentalList;
    }

    @Override
    public void update(Rental rental) {
        // 반납일자 뿐만이 아닌 다른 것들도 수정사항에 들어감
        // 반납일자를 수정하고자 할 때
        String sql = "UPDATE rental SET return_date = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setDate(1, rental.getReturnDate());
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
