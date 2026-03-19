package org.example;

//임시로 폴더 전체 import
import org.example.comic.*;
import org.example.member.*;
import org.example.rental.*;

import java.util.List;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final MemberController memberController;
    private final ComicController comicController;
//    private final RentalController rentalController;

    public App() {
        this.sc = new Scanner(System.in);
        ComicRepository comicRepository = new JdbcComicRepository();
        MemberRepository memberRepository = new MemberRepository();
        RentalRepository rentalRepository=new RentalRepository();

        ComicService comicService = new ComicService(comicRepository);
        MemberService memberService = new MemberService(memberRepository);
        RentalService rentalService = new RentalService(rentalRepository, comicService, memberService);

        this.comicController = new ComicController(sc, comicService);
        this.memberController = new MemberController(sc, memberService);
        this.rentalController = new RentalController(sc, rentalService);
    }

    public void run() {
        System.out.println("===만화 대여 프로그램 시작===");
        while (true) {
            System.out.print("명령어 : ");
            String command = sc.nextLine().trim();
            if (command.isEmpty()) continue;
            //rq를 통한 명령어 해석
            Rq rq = new Rq(command);

            // 숫자를 잘못 입력한 경우 다시 입력받도록 차단
            if (!rq.isValid()) {
                continue;
            }

            String action = rq.getAction();
            int[] data = rq.getData();
            try {
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
                        if (data.length < 1) {
                            System.out.println("만화책 번호를 입력해주세요. 예: comic-detail 1");
                            break;
                        }
                        comicController.showComicDetail(data[0]);
                        break;
                    }
                    case "comic-update": {
                        if (data.length < 1) {
                            System.out.println("수정할 만화책 번호를 입력해주세요. 예: comic-update 1");
                            break;
                        }
                        comicController.updateComic(data[0]);
                        break;
                    }
                    case "comic-delete": {
                        if (data.length < 1) {
                            System.out.println("삭제할 만화책 번호를 입력해주세요. 예: comic-delete 1");
                            break;
                        }
                        comicController.deleteComic(data[0]);
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
                    if (data.length < 2) {
                        System.out.println("회원 번호와 만화책 번호를 모두 입력해주세요. 예: rent 1 2");
                        break;
                    }
                    rentalController.rentComic(data[0], data[1]);
                    break;
                }
                case "return": {
                    if (data.length < 1) {
                        System.out.println("반납할 만화책 번호를 입력해주세요. 예: return 1");
                        break;
                    }
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
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }



        }
    }

}
