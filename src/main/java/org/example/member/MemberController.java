package org.example.member;

import org.example.Rq;

import java.util.List;
import java.util.Scanner;

public class MemberController {
    private final MemberService memberService;
    private final Scanner scanner;

    public MemberController(Scanner scanner, MemberService memberService) {
        this.scanner = scanner;
        this.memberService = memberService;
    }

    public void addMember() {
        System.out.print("이름: ");
        String name = scanner.nextLine().trim();

        System.out.print("전화번호: ");
        String phone = scanner.nextLine().trim();

        Long id = memberService.addMember(name, phone);

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