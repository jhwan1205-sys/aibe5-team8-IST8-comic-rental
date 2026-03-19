package org.example.rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RentalController {
    RentalService r_Service = new RentalService();
    RentalRepository r_Repository = new RentalRepositoryImpl();

    public void rentComic(long memberId, long comicId) {
        // 누군가가 빌려갔는지를 검증함
        boolean validated = r_Service.validateRent(comicId);

        if (validated) {
            // 새로운 렌탈 기록을 작성함
            Rental rental = new Rental();
            rental.setMemberId(memberId);
            rental.setComicId(comicId);
            rental.setRentalDate(LocalDate.now());

            long result = r_Repository.save(rental);
            // JdbcComicRepository.isRented = true;

            if(result > 0) {
                System.out.println("⭕ 만화 대여가 완료되었습니다.");
            }
        } else {
            System.out.println("❌ 만화 대여에 실패하였습니다");
        }
    }

    public void returnComic(long rentalId) {
        // 반납할 대여 기록을 찾아낸다.
        boolean validate = r_Service.validateReturn(rentalId);

        if (validate) {
            Rental rental = r_Repository.findByRentalId(rentalId);
            rental.setReturnDate(LocalDate.now());
            r_Repository.update(rental);
            // JdbcComicRepository.isRented = false;
            System.out.println("⭕ 만화 반납이 완료되었습니다.");
        } else {
            System.out.println("❌ 만화 반납에 실패하였습니다");
        }

    }

    public void listRentals(long memberId){
        // memberName으로 가져오면 더 좋을듯
        System.out.println(memberId + "번 회원의 대여 목록입니다. ==");
        r_Service.showRentalList(memberId);

    }
}
