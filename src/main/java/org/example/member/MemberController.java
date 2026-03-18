package org.example.member;

import java.util.List;
import java.util.Scanner;

public class MemberController {
    private MemberService memberService = new MemberService();
    private Scanner scanner;

    public MemberController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addMember() {
        try {
            System.out.print("이름: ");
            String name = scanner.nextLine().trim();
            validateName(name); // 이름 예외 처리

            System.out.print("전화번호: ");
            String phone = scanner.nextLine().trim();
            validatePhone(phone); // 전화번호 예외 처리

            // 검증을 통과하면 Service로 데이터 전달
            int id = memberService.addMember(name, phone);

            if (id != -1) {
                System.out.println("=> 회원이 등록되었습니다. (id=" + id + ")");
            } else {
                System.out.println("=> 회원 등록 중 DB 오류가 발생했습니다.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("=> 입력 오류: " + e.getMessage());
        }
    }

    public void listMembers() {
        List<Member> members = memberService.listMembers();
        System.out.println("id | 이름       | 전화번호         | 등록일");
        System.out.println("-------------------------------------------------------------");
        for (Member m : members) {
            System.out.printf("%d    | %s   | %s    | %s%n",
                    m.getId(), m.getName(), m.getPhone(), m.getRegDate());
        }
    }

    private void validateName(String name) {
        // null이거나 공백만 입력되었을 경우
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 필수 입력값입니다.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("전화번호는 필수 입력값입니다.");
        }

        int hyphenCount = phone.length() - phone.replace("-", "").length();
        if (hyphenCount != 2) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. (예: 010-1111-2222)");
        }
    }
}