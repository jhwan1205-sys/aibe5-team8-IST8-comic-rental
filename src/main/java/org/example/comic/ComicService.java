package org.example.comic;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 비즈니스 로직만 담당합니다.
 * UI(App/Rq/Console)의 입력/출력은 별도 레이어에서 처리하고, 이 서비스는 순수하게 값만 받고 결과만 반환합니다.
 */
public class ComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = Objects.requireNonNull(comicRepository);
    }

    public long addComic(String title, int volume, String author) {
        String t = requireNotBlank(title, "title").trim();
        String a = requireNotBlank(author, "author").trim();
        if (volume <= 0) {
            throw new IllegalArgumentException("권수(volume)는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.addComic(t, volume, a);
    }

    public List<Comic> getComics() {
        return comicRepository.getComics();
    }

    public Optional<Comic> findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.findById(id);
    }

    /**
     * 부분 수정용 메서드입니다.
     * 파라미터로 null을 넘기면 해당 값은 기존 값을 유지합니다.
     */
    public boolean updateComic(long id, String title, Integer volume, String author, Boolean rented) {
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
        boolean nextRented = (rented == null) ? before.isRented() : rented;

        return comicRepository.updateComic(id, nextTitle, nextVolume, nextAuthor, nextRented);
    }

    public boolean deleteComic(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 1 이상의 값이어야 합니다.");
        }
        return comicRepository.deleteById(id);
    }

    private static String requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }

    // 수정 시: null은 유지, blank는 실수로 기존 값이 날아가는 것을 막기 위해 허용하지 않음.
    private static String mergeNullableString(String maybe, String current, String field) {
        if (maybe == null) {
            return current;
        }
        if (maybe.isBlank()) {
            throw new IllegalArgumentException(field + "은(는) 비어 있을 수 없습니다.");
        }
        return maybe.trim();
    }

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
