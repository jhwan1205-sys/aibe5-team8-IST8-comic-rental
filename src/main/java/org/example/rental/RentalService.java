package org.example.rental;

import org.example.comic.Comic;
import org.example.comic.ComicService;
import org.example.member.MemberService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RentalService {
    private final RentalRepository r_Repository;
    private final ComicService comicService;
    private final MemberService memberService;

    public RentalService(RentalRepository r_Repository, ComicService comicService, MemberService memberService){
        this.r_Repository = r_Repository;
        this.comicService = comicService;
        this.memberService = memberService;
    }

    public long processRent(long memberId, long comicId) throws SQLException {
        Comic comic = comicService.findById(comicId);
        Member member = memberService.findById(memberId);

        if(comic == null){
            throw new IllegalArgumentException("존재하지 않는 만화책입니다.");
        } else if (member == null){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        // comic의 isRented가 True : 이미 만화를 누군가 빌려갔다면?
        if(comic.isRented()) {
            throw new IllegalArgumentException("이미 누군가 빌려간 만화입니다.");
        }

        Rental rental = new Rental();
        rental.setMemberId(memberId);
        rental.setComicId(comicId);
        rental.setRentalDate(LocalDate.now());

        comicService.updateComicRentedStatus(comicId, true);
        long resultId = r_Repository.save(rental);

        if (resultId == 0) {
            // DB 저장 자체에 실패한 경우
            throw new SQLException("데이터베이스 저장 중 오류가 발생했습니다.");
        }

        return resultId;

    }
    public void processReturn(long rentalId) throws SQLException {
        Rental rental = r_Repository.findByRentalId(rentalId);

        if (rental == null){
            throw new IllegalArgumentException("해당 대여 번호를 찾을 수 없습니다.");
        }
        if (rental.getReturnDate() != null){
            throw new IllegalArgumentException("이미 반납이 완료된 도서입니다");
        }

        rental.setReturnDate(LocalDate.now());

        comicService.updateComicRentedStatus(rental.getComicId(), false);
        r_Repository.update(rental);
    }

    public List showAllRentalList() throws SQLException {
        List<Rental> rentalList = r_Repository.findAll();
        return rentalList;
    }

}

