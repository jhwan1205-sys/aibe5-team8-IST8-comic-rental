package org.example.member;

import java.util.List;
import java.util.Scanner;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();
    private Scanner sc;
    public MemberService(Scanner sc) {
        this.sc = sc;
    }

    public void addMember() {
        System.out.print("이름: ");
        String name = sc.nextLine();
        System.out.print("전화번호: ");
        String phone = sc.nextLine();

        int id = memberRepository.addMember(name, phone);
        if (id == -1) {
            System.out.println("회원 등록에 실패하였습니다.");
        }  else {
            System.out.println("=> 회원이 등록되었습니다. (id=" + id + ")");
        }

    }
    public void listMembers() {
        List<Member> members = memberRepository.getMembers();

        System.out.println("번호 | 이름 | 전화번호 | 등록일");
        System.out.println("----------------------------------");

        for (Member member : members) {
            System.out.printf("%d | %s | %s | %s\n",
                    member.getId(),
                    member.getName(),
                    member.getPhone(),
                    member.getRegDate());
        }
    }
}
