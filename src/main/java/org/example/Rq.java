package org.example;

public class Rq {
    private String action;
    private int[] data;
    public Rq(String request) {
        String[] split = request.split(" ");
        action = split[0];

        if (split.length > 1) {
            data = new int[split.length - 1];
        }
        try {
            if (split.length == 2) {
                this.data[0] = Integer.parseInt(split[1]);
            } else if (split.length == 3) {
                this.data[0] = Integer.parseInt(split[1]);
                this.data[1] = Integer.parseInt(split[2]);
            }
        } catch (NumberFormatException e) {
            System.out.println("양식에 맞게 명령어를 입력하세요.");
        }

    }

    public String getAction() {
        return action;
    }

    public int[] getData() {
        return data;
    }
}
