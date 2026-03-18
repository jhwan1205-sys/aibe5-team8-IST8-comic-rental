package org.example.rental;

import java.time.LocalDate;
import java.util.List;

public class RentalService {
    RentalRepository repository = new RentalRepositoryImpl();

    public void rentComic(long comicId, long memberId) {
        System.out.println("== 대여 서비스입니다. ==");
        List<Rental> allRentals = repository.findAll();

        for(Rental r : allRentals) {
            if(r.getComicId() == comicId && r.getReturnDate() == null) {
                System.out.println("이미 누군가 대여를 하였습니다.");
                return;
            }
        }

        Rental rental = new Rental();
        rental.setMemberId(memberId);
        rental.setComicId(comicId);
        rental.setRentalDate(LocalDate.now());

        int result = repository.save(rental);
        if(result > 0) {
            System.out.println("대여가 완료되었습니다.");
        }
    }

    public void returnComic(long rentalId) {
        System.out.println("== 반납 서비스입니다. ==");
        Rental rental = repository.findByRentalId(rentalId);

        if (rental == null){
            System.out.println("해당 대여 번호를 찾을 수 없습니다.");
            return;
        }

        // volume++;
        rental.setReturnDate(LocalDate.now());
        repository.update(rental);
        System.out.println("반납이 완료되었습니다.");

    }

    public void listRentals(long memberId){
        List<Rental> rentalList = repository.findAll();
        System.out.println("== " + memberId + "번 회원의 대여 목록입니다. ==");

        boolean hasRentals = false;
        for(Rental r : rentalList) {
            if(r.getMemberId() == memberId) {
                System.out.println("대여id | 만화id | 회원id | 대여일 | 반납일 ");
                System.out.println("-------------------------------------");
                System.out.println(r.getId()+"  | " + r.getComicId() + "  | " + r.getMemberId() + "  | " + r.getRentalDate() + "  | " + r.getReturnDate());
                hasRentals = true;
            }
        }

        if(!hasRentals) {
            System.out.println("대여 기록이 없습니다");
        }

    }

}

