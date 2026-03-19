package org.example.rental;

import org.example.comic.Comic;
import org.example.comic.ComicService;
import org.example.comic.JdbcComicRepository;

import java.time.LocalDate;
import java.util.List;

public class RentalService {
    RentalRepository r_Repository = new RentalRepositoryImpl();
    ComicService comicService = new ComicService(new JdbcComicRepository());

    public long processRent(long memberId, long comicId){
        if (!validateRent(comicId)){
            throw new RuntimeException("대여가 불가능한 상태입니다");
        }
        Rental rental = new Rental();
        rental.setMemberId(memberId);
        rental.setComicId(comicId);
        rental.setRentalDate(LocalDate.now());

        comicService.updateComicRentedStatus(comicId, true);
        r_Repository.save(rental);

        return rental.getId();

    }
    public void processReturn(long rentalId){
        if(!validateReturn(rentalId)){
            throw new RuntimeException("반납이 불가능한 상태입니다.");
        }
        Rental rental = r_Repository.findByRentalId(rentalId);
        rental.setReturnDate(LocalDate.now());

        comicService.updateComicRentedStatus(rental.getComicId(), false);
        r_Repository.update(rental);
    }

    public boolean validateRent(long comicId){
        Comic comic = comicService.findById(comicId);

        // comic의 isRented가 True : 이미 만화를 누군가 빌려갔다면?
        if(comic.isRented()) {
            return false;
        }
        return true;
    }

    public boolean validateReturn(long rentalId){
        Rental rental = r_Repository.findByRentalId(rentalId);

        if (rental == null){
            System.out.println("해당 대여 번호를 찾을 수 없습니다.");

            return false;
        }
        if (rental.getReturnDate() != null){
            System.out.println("이미 반납이 완료된 도서입니다");

            return false;
        }

        return true;
    }

    public List showAllRentalList(){
        List<Rental> rentalList = r_Repository.findAll();
        if (rentalList.isEmpty()){
            System.out.println("대여 기록이 없습니다.");
        }

        return rentalList;
    }

}

