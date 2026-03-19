package org.example.member;

import java.util.List;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long addMember(String name, String phone) {

        validateName(name);
        validatePhone(phone);

        return memberRepository.addMember(name, phone);
    }

    public List<Member> listMembers() {
        return memberRepository.getMembers();
    }

    public Member findById(long id) {
        Member member = memberRepository.findById(id);

        // Repository가 null을 반환했다면, 예외를 던져 NullPointerException 차단
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. (id=" + id + ")");
        }

        return member;
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
        String phoneRegex = "^(02|0[3-9]{1}[0-9]{1}|01[0-9])-\\d{3,4}-\\d{4}$";

        if (!phone.matches(phoneRegex)) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
        }
    }
}