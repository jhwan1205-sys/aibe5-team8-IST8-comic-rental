package org.example.comic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    // 신규 만화 등록
    public long addComic(String title, String volumeInput, String author) {
        vaildateTitle(title);
        int volume = vaildateVolume(volumeInput);
        vaildateAuthor(author);
        return comicRepository.addComic(title, volume, author);
    }

    // 전체 만화 목록 조회
    public List<Comic> getComics() {
        return comicRepository.getComics();
    }

    // id로 만화 조회: 존재하면 반환, 없으면 예외 발생
    public Comic findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        Comic comic =  comicRepository.findById(id);
        if (comic == null) {
            throw new NoSuchElementException("해당 id의 만화책이 없습니다. (id=" + id + ")");
        }
        return comic;
    }

    // 만화 정보 수정: 수정 성공 시 true, 대상 id가 없으면 false 반환.
    public boolean updateComic(long id, String title, String volumeInput, String author) {
        Comic before = comicRepository.findById(id);
        // 입력된 값이 없으면 이전 값으로 설정
        if (title.isEmpty()) {
            title = Objects.requireNonNull(before).getTitle();
        }

        int volume;
        // 입력된 값이 없으면 이전 값으로 설정
        if (volumeInput.isEmpty()) {
            volume = Objects.requireNonNull(before).getVolume();
        } else {
                volume = vaildateVolume(volumeInput);
        }

        // 입력된 값이 없으면 이전 값으로 설정
        if (author.isEmpty()) {
            author = Objects.requireNonNull(before).getAuthor();
        }

        return comicRepository.updateComic(id, title, volume, author);
    }

    // id로 만화 삭제: 삭제 성공 시 true, 대상 id가 없으면 false
    public boolean deleteComic(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.deleteById(id);
    }

    private void vaildateTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수 입력값입니다.");
        }
    }

    private int vaildateVolume(String volumeInput) {
        int volume;
        try {
            volume = Integer.parseInt(volumeInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("권수는 숫자만 입력해야 합니다.");
        }
        if (volume <= 0) {
            throw new IllegalArgumentException("권수는 1 이상이어야 합니다.");
        }
        return volume;
    }

    private void vaildateAuthor(String author) {
        if (author.isBlank()) {
            throw new IllegalArgumentException("작가는 필수 입력값입니다.");
        }
    }
}
