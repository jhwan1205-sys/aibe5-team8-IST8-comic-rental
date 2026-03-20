package org.example;

public class Rq {
    private String action;
    private int[] data;
    private boolean valid = true; // 명령어 유효성 상태를 저장

    public Rq(String command) {
        String[] split = command.trim().split("\\s+");
        this.action = split[0];

        if (split.length == 1) {
            this.data = new int[0];
            return;
        }
        this.data = new int[split.length - 1];

        try {
            for (int i = 1; i < split.length; i++) {
                this.data[i - 1] = Integer.parseInt(split[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("양식에 맞게 명령어를 입력하세요. (숫자 입력 필요)");
            this.valid = false; // 파싱 실패 시 false로 변경
        }

    }

    public String getAction() {
        return action;
    }

    public boolean isValid() {
        return valid;
    }

    public int[] getData() {
        return data;
    }
}
