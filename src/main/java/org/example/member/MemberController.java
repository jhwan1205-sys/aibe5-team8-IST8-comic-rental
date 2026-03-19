package org.example.member;

import org.example.Rq;

import java.util.List;
import java.util.Scanner;

public class MemberController {
    private MemberService memberService = new MemberService();
    private Scanner scanner;

    public MemberController(Scanner scanner) {
        this.scanner = scanner;
    }
    /* app.java 라우팅에 따라 활용 예정
    public void handle(Rq rq) {
        switch (rq.getAction()) {
            case "add":
                addMember();
                break;
            case "list":
                listMembers();
                break;
            default:
                System.out.println("=> 존재하지 않는 멤버 명령어입니다.");
        }
    } */

    public void addMember() {
        System.out.print("이름: ");
        String name = scanner.nextLine().trim();

        System.out.print("전화번호: ");
        String phone = scanner.nextLine().trim();

        int id = memberService.addMember(name, phone);

        System.out.println("=> 회원이 등록되었습니다. (id=" + id + ")");
    }

    public void listMembers() {
        List<Member> members = memberService.listMembers();

        System.out.println("id | 이름       | 전화번호         | 등록일");
        System.out.println("-------------------------------------------------------------");

        for (Member m : members) {
            System.out.printf("%d | %s | %s | %s%n",
                    m.getId(), m.getName(), m.getPhone(), m.getRegDate());
        }
    }
}