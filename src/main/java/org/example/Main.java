package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("== DB 연결 테스트 시작 ==");

        // 1. DBUtil을 통해 연결 객체 가져오기
        Connection conn = DBUtil.getConnection();

        // 2. 연결 상태 확인
        if (conn != null) {
            System.out.println("DB 연결 성공!");

            try {
                // 연결 종료 (테스트 후 자원 반납)
                conn.close();
                System.out.println("DB 연결이 안전하게 닫혔습니다.");
            } catch (SQLException e) {
                System.err.println("연결 닫기 실패: " + e.getMessage());
            }
        } else {
            System.out.println("DB 연결 실패! DBUtil의 설정을 확인.");
        }
    }
}