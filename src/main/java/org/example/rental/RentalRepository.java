package org.example.rental;

import java.sql.SQLException;
import java.util.List;

public interface RentalRepository {
    // 대여 정보 저장
    long save(Rental rental) throws SQLException;

    // 멤버가 대여한 기록 찾기
    Rental findByRentalId(long id) throws SQLException;

    // 전체 대여 목록
    List<Rental> findAll() throws SQLException;

    // 업데이트 ( 반납날짜 등 )
    void update(Rental rental) throws SQLException;
}
