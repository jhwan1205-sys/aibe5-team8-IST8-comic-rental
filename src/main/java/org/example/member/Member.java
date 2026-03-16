package org.example.member;

public class Member {
    private int id;
    private String name;
    private String phone;
    private String regDate;

    public Member(int id, String name, String phone, String regDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
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
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

}
