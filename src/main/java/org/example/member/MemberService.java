package org.example.member;

import java.util.List;
import java.util.Scanner;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();

    public int addMember(String name, String phone) {
        return memberRepository.addMember(name, phone);
    }

    public List<Member> listMembers() {
        return memberRepository.getMembers();
    }
}
