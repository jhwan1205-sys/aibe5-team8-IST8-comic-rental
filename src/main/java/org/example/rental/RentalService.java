package org.example.rental;

import java.util.List;

public class RentalService {
    RentalRepository r_Repository = new RentalRepositoryImpl();

    public void canRent(long comicId){
        List<Rental> allRentals = r_Repository.findAll();

        for(Rental r : allRentals) {
            if(r.getComicId() == comicId && r.getReturnDate() == null) {
                System.out.println("이미 누군가 대여를 하였습니다.");
                return;
            }
        }
    }

    public void canReturn(long rentalId){
        List<Rental> allRentals = r_Repository.findAll();
        Rental rental = r_Repository.findByRentalId(rentalId);

        // 고려사항
        // 대여기록의 MemberId와 현재 로그인한 멤버의 ID와 같은가?

        if (rental == null){
            System.out.println("해당 대여 번호를 찾을 수 없습니다.");
        }
        return;
    }

    public void showRentalList(long memberId){
        List<Rental> rentalList = r_Repository.findAll();

        // comic.isRented
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

