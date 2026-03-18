package org.example;

import org.example.comic.Comic;
import org.example.member.Member;
import org.example.rental.Rental;

import java.util.List;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final MemberController memberController;
    private final ComicController comicController;
    private final RentalController rentalController;

    public App() {
        this.sc = new Scanner(System.in);

        // 💡 객체 조립 (실제로는 Main이나 별도의 클래스에서 조립해서 넘겨주는 것이 더 완벽하지만, 현재 단계에선 여기서 해도 충분합니다!)
        ComicService comicService = new ComicService(new JdbcComicRepository());
        MemberService memberService = new MemberService(new JdbcMemberRepository());
        RentalService rentalService = new RentalService(new JdbcRentalRepository(), comicService, memberService);

        this.comicController = new ComicController(sc, comicService);
        this.memberController = new MemberController(sc, memberService);
        this.rentalController = new RentalController(sc, rentalService);
    }

    public void run() {
        System.out.println("===만화 대여 프로그램 시작===");
        while (true) {
            System.out.print("명령어 : ");
            String command = sc.next();
            //rq를 통한 명령어 해석
            Rq rq = new Rq(command);
            String action = rq.getAction();
            int[] data = rq.getData();

            //명령어에 따른 작동
            switch (action) {
                case "exit":
                    System.out.println("===프로그램 종료===");
                    sc.close();
                    return;
                //만화책 관련 명령어
                case "comic-add": {
                    comicController.addComic();
                    break;
                }
                case "comic-list": {
                    comicController.listComics();
                    break;
                }
                case "comic-detail": {
                    comicController.showComicDetail(data[0]);
                    break;
                }
                case "comic-update": {
                    comicController.updateComic(data[0]);
                    break;
                }
                //회원 관련 명령어
                case "member-add": {
                    memberController.addMember();
                    break;
                }
                case "member-list": {
                    memberController.listMembers();
                    
                    break;
                }
                //대여&반납 관련 명령어
                case "rent": {
                    rentalController.rentComic(data[0], data[1]);
                    break;
                }
                case "return": {
                    rentalController.returnComic(data[0]);
                    break;
                }
                case "rental-list": {
                    rentalController.listRentals();
                    break;
                }
                default:
                    System.out.println("존재하지 않는 명령어 입니다.");
                    break;

            }



        }
    }

}
