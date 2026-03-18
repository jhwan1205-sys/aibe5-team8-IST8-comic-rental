package org.example.rental;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    RentalRepository repository = new RentalRepositoryImpl();

    public void rentComic(long comicId, long memberId, Date rentalDate) {
        System.out.println("== 대여 서비스입니다. ==");
        List<Rental> allRentals = repository.findAll();

        // volume에 따라 남은 책을 표시하고, 대여가 가능한지 여부(?)
        // 검증 : 모든 대여 기록에서..
        for(Rental r : allRentals) {
            if(r.getComicId() == comicId && r.getMemberId() == memberId) {
                // 코믹이 이미 빌린 목록에 있다면
                System.out.println("이미 대여를 하셨습니다.");
                return;
            }
        }

        Rental rental = new Rental();
        rental.setMemberId(memberId);
        rental.setComicId(comicId);
        rental.setRentalDate(rentalDate);

        int result = repository.save(rental);
        if(result > 0) {
            System.out.println("대여가 완료되었습니다.");
        }
    }

    public void returnComic(long rentalId, long memberId, Date returnDate) {
        System.out.println("== 반납 서비스입니다. ==");
        Rental rental = repository.findById(rentalId);

        rental.setMemberId(memberId);
        rental.setReturnDate(returnDate);

        int result = repository.save(rental);
        if(result > 0) {
            System.out.println("반납이 완료되었습니다.");
        }
    }

    public void listRentals(Rental rental){
        List<Rental> rentalList = repository.findAll();
        System.out.println("== " + rental.getMemberId() + "님의 대여 목록입니다. ==");

        for(Rental r : rentalList) {
            System.out.println(r);
        }

    }

}
