package org.example.rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RentalController {
    // 1. final로 선언하여 불변성 확보
    private final RentalService r_Service;

    // 2. 생성자를 통해 외부에서 주입받기 ( DI )
    public RentalController(RentalService rentalService) {
        this.r_Service = rentalService;
    }

    public void rentComic(long memberId, long comicId) {
        try {
            long rentalId = r_Service.processRent(memberId, comicId);
            System.out.printf("=> 대여 완료: [대여id=%d] 만화(%d) → 회원(%d)%n", rentalId, comicId, memberId);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 대여 실패: "  + e.getMessage());
        } catch (Exception e){
            System.out.println("❌ 시스템 오류가 발생하였습니다 로그를 확인해주세요");
            // 로그 남기기
            e.printStackTrace();
        }
    }

    public void returnComic(long rentalId) {
       try {
           r_Service.processReturn(rentalId);
           System.out.println("=> 반납 완료: 대여id=" + rentalId);
       } catch (IllegalArgumentException e) {
           System.out.println("❌ 올바른 대여 번호를 확인해주세요:" + e.getMessage());

       } catch (Exception e) {
            System.out.println("❌ 시스템 오류가 발생하였습니다 로그를 확인해주세요");
            e.printStackTrace();
        }

    }

    public void listRentals(){
        List<Rental> list = r_Service.showAllRentalList();

        if (list.isEmpty()) {
            System.out.println("대여 기록이 없습니다.");
            return;
        }

        // 실행 예시와 완전히 동일한 테이블 양식으로 출력 (간격 맞춤)
        System.out.println("대여id | 만화id | 회원id | 대여일     | 반납일");
        System.out.println("-----------------------------------------------");
        for (Rental r : list) {
            String returnDateStr = (r.getReturnDate() == null) ? "-" : r.getReturnDate().toString();

            System.out.printf("%-6d | %-6d | %-6d | %-10s | %s%n",
                    r.getId(),
                    r.getComicId(),
                    r.getMemberId(),
                    r.getRentalDate(),
                    returnDateStr
            );
        }
    }
}
