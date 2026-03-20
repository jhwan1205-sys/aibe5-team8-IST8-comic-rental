package org.example.member;

import java.time.LocalDate;

public class Member {
    private Long id;
    private String name;
    private String phone;
    private LocalDate regDate;

    public Member(Long id, String name, String phone, LocalDate regDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.regDate = regDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getRegDate() {
        return regDate;
    }
    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

}
