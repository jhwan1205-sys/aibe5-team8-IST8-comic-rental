package org.example.comic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public final class ComicController {

    private Scanner scanner;
    private final ComicService comicService;

    public ComicController(Scanner scanner, ComicService comicService) {
        this.scanner = scanner;
        this.comicService = comicService;
    }

    // 신규 만화 등록
    public void addComic() {
        System.out.print("제목: ");
        String title = scanner.nextLine().trim();

        System.out.print("권수: ");
        String volumeInput = scanner.nextLine().trim();

        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        long id = comicService.addComic(title, volumeInput, author);
        if (id <= 0)
            throw new IllegalArgumentException("만화 등록에 실패했습니다.");
        System.out.println("=> 만화책이 등록되었습니다. (id=" + id + ")");
    }

    // 전체 만화 목록 조회
    public void listComics() {
        List<Comic> comics = comicService.getComics();
        System.out.println("번호 |  제목    | 권수 |  작가   |  상태  |  등록일");
        System.out.println("-------------------------------------------------------");
        for (Comic c : comics) {
            System.out.printf(
                    "%d | %s | %d | %s | %s | %s%n",
                    c.getId(),
                    c.getTitle(),
                    c.getVolume(),
                    c.getAuthor(),
                    c.isRented() ? "대여중" : "대여가능",
                    c.getRegDate()
            );
        }

    }



    // 만화책 상세 정보 조회
    public void showComicDetail(long id) {
        Comic comic = comicService.findById(id);
        System.out.println("번호: " + comic.getId());
        System.out.println("제목: " + comic.getTitle());
        System.out.println("권수: " + comic.getVolume());
        System.out.println("작가: " + comic.getAuthor());
        System.out.println("대여상태: " + (comic.isRented() ? "대여중" : "대여가능"));
        System.out.println("등록일: " + comic.getRegDate());
        System.out.println("=> 만화책의 상세 정보가 출력되었습니다. (id=" + id + ")");
    }

    // 만화 정보 수정
    public void updateComic(long id) {
        System.out.print("제목: ");
        String title = scanner.nextLine().trim();
        System.out.print("권수: ");
        String volumeInput = scanner.nextLine().trim();

        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        boolean updated = comicService.updateComic(id, title, volumeInput, author);
        if (!updated)
            throw new IllegalArgumentException("만화 수정에 실패했습니다.");
        System.out.println("=> " + id + "번 만화가 수정되었습니다.");
    }

    // 만화 삭제
    public void deleteComic(long id) {
        boolean deleted = comicService.deleteComic(id);
        if (!deleted)
            throw new IllegalArgumentException("만화 삭제에 실패했습니다.");
        System.out.println("=> " + id + "번 만화가 삭제되었습니다.");
    }
}

