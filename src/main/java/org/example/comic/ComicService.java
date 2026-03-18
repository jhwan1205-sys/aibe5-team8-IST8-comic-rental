package org.example.comic;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComicService {
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    // 신규 만화 등록
    public long addComic(String title, int volume, String author) {
        return comicRepository.addComic(title, volume, author);
    }

    // 전체 만화 목록 조회
    public List<Comic> getComics() {
        return comicRepository.getComics();
    }

    // id로 만화 조회: 존재하면 Optional로 반환, 없으면 Optional.empty()
    public Optional<Comic> findById(long id) {
        return comicRepository.findById(id);
    }

    // 만화 정보 수정: 수정 성공 시 true, 대상 id가 없으면 false 반환.
    public boolean updateComic(long id, String title, Integer volume, String author) {
        return comicRepository.updateComic(id, title, volume, author);
    }

    // id로 만화 삭제: 삭제 성공 시 true, 대상 id가 없으면 false
    public boolean deleteComic(long id) {
        return comicRepository.deleteById(id);
    }

}
