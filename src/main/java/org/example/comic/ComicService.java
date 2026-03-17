package org.example.comic;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComicService {
    private final ComicRepository comicRepository;

    // 생성자: comicRepository 초기화
    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = Objects.requireNonNull(comicRepository);
    }

    // 신규 만화 등록: title, author은 null 또는 공백 불가, volume은 1 이상
    public long addComic(String title, int volume, String author) {
        String t = requireNotBlank(title, "title").trim();
        String a = requireNotBlank(author, "author").trim();
        if (volume <= 0) {
            throw new IllegalArgumentException("권수(volume)는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.addComic(t, volume, a);
    }

    // 전체 만화 목록 조회
    public List<Comic> getComics() {
        return comicRepository.getComics();
    }

    // id로 만화 조회: id는 1 이상. 존재하면 Optional로 반환, 없으면 Optional.empty()
    public Optional<Comic> findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.findById(id);
    }

    // 만화 정보 수정: title, volume, author 중 수정하지 않을 값은 null로 전달하면 기존 값이 유지됨
    // blank는 허용하지 않음.
    // 수정 성공 시 true, 대상 id가 없으면 false 반환.
    public boolean updateComic(long id, String title, Integer volume, String author) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }

        Comic before = comicRepository.findById(id).orElse(null);
        if (before == null) {
            return false;
        }

        String nextTitle = mergeNullableString(title, before.getTitle(), "title");
        String nextAuthor = mergeNullableString(author, before.getAuthor(), "author");
        int nextVolume = mergeNullablePositiveInt(volume, before.getVolume(), "volume");

        return comicRepository.updateComic(id, nextTitle, nextVolume, nextAuthor);
    }

    // id로 만화 삭제: 삭제 성공 시 true, 대상 id가 없으면 false
    public boolean deleteComic(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.deleteById(id);
    }

    // 필수 문자열 파라미터 검증 유틸: null 또는 공백이면 예외를 던짐.
    private static String requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }


    // 만화 정보 수정 시 문자열 필드 병합: 수정할 문자(maybe)가 null이면 기존 값 유지하고, blank면 예외를 던진다.
    private static String mergeNullableString(String maybe, String current, String field) {
        if (maybe == null) {
            return current;
        }
        if (maybe.isBlank()) {
            throw new IllegalArgumentException(field + "은(는) 비어 있을 수 없습니다.");
        }
        return maybe.trim();
    }

    // 만화 정보 수정 시 정수 필드 병합: 수정할 정수(maybe)가 null이면 기존 값 유지하고, 1 미만이면 예외를 던진다.
    private static int mergeNullablePositiveInt(Integer maybe, int current, String field) {
        if (maybe == null) {
            return current;
        }
        if (maybe <= 0) {
            throw new IllegalArgumentException(field + "은(는) 1 이상의 값이어야 합니다.");
        }
        return maybe;
    }
}
