package org.example.rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RentalController {
    RentalService r_Service = new RentalService();
    RentalRepository r_Repository = new RentalRepositoryImpl();

    public void rentComic(long comicId, long memberId) {
        System.out.println("== 대여 서비스입니다. ==");

        // 누군가가 빌려갔는지를 검증함
        r_Service.canRent(comicId);

        // 새로운 렌탈 기록을 작성함
        Rental rental = new Rental();
        rental.setMemberId(memberId);
        rental.setComicId(comicId);
        rental.setRentalDate(LocalDate.now());

        int result = r_Repository.save(rental);
        if(result > 0) {
            System.out.println("대여가 완료되었습니다.");
        }
    }

    public void returnComic(long rentalId) {
        System.out.println("== 반납 서비스입니다. ==");
        Rental rental = r_Repository.findByRentalId(rentalId);

        // 대여 기록을 찾고, 검증함
        r_Service.canReturn(rental.getId());

        rental.setReturnDate(LocalDate.now());
        r_Repository.update(rental);
        System.out.println("반납이 완료되었습니다.");

    }

    public void listRentals(long memberId){
        System.out.println("== " + memberId + "번 회원의 대여 목록입니다. ==");

        r_Service.showRentalList(memberId);

    }
}
