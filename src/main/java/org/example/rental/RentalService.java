package org.example.rental;

import java.util.List;

public class RentalService {
    RentalRepository r_Repository = new RentalRepositoryImpl();

    public boolean validateRent(long comicId){
        List<Rental> allRentals = r_Repository.findAll();

        for(Rental r : allRentals) {
            // ReturnDate가 null인지 확인함
            if(r.getComicId() == comicId && r.getReturnDate() == null) {
                System.out.println("해당 도서는 " + r.getMemberId() + "번 회원이 대여한 상태입니다.");
                return false;
            }
        }
        return true;
    }

    public boolean validateReturn(long rentalId){
        Rental rental = r_Repository.findByRentalId(rentalId);

        if (rental == null){
            System.out.println("해당 대여 번호를 찾을 수 없습니다.");

            return false;
        }

        return true;
    }

    public void showRentalList(long memberId){
        List<Rental> rentalList = r_Repository.findAll();

        // JdbcComicRepository.isRented = false;
        boolean isRented = false;
        for(Rental r : rentalList) {
            if(r.getMemberId() == memberId) {
                System.out.println("대여id | 만화id | 회원id | 대여일 | 반납일 ");
                System.out.println("-------------------------------------");
                System.out.println(r.getId()+"  | " + r.getComicId() + "  | " + r.getMemberId() + "  | " + r.getRentalDate() + "  | " + r.getReturnDate());
                isRented = true;
            }
        }

        if(!isRented) {
            System.out.println("대여 기록이 없습니다");
        }
    }

}

