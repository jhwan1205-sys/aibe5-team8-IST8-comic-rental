package org.example.member;

import java.util.List;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();

    public int addMember(String name, String phone) {

        validateName(name);
        validatePhone(phone);

        int id = memberRepository.addMember(name, phone);

        if (id == -1) {
            throw new RuntimeException("DB 오류로 회원 등록 실패");
        }

        return id;
    }

    public List<Member> listMembers() {
        return memberRepository.getMembers();
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 필수 입력값입니다.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("전화번호는 필수 입력값입니다.");
        }

        int hyphenCount = phone.length() - phone.replace("-", "").length();
        if (!phone.matches("^01[0-9]-\\d{3,4}-\\d{4}$")) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
        }
    }
}