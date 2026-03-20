package org.example.comic;

import java.util.List;
import java.util.Optional;

public interface ComicRepository  {

    long addComic(String title, int volume, String author);

    List<Comic> getComics();

    Comic findById(long id);

    boolean updateComic(long id, String title, int volume, String author);

    boolean deleteById(long id);

    void updateRentedStatus(Long comicId, boolean isRented);
}