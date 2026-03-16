package org.example.comic;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class ComicServiceTest {

    @Test
    void addComic_validatesInputs() {
        ComicService service = new ComicService(new InMemoryComicRepository());

        assertThrows(IllegalArgumentException.class, () -> service.addComic(" ", 1, "a"));
        assertThrows(IllegalArgumentException.class, () -> service.addComic("t", 0, "a"));
        assertThrows(IllegalArgumentException.class, () -> service.addComic("t", 1, " "));
    }

    @Test
    void updateComic_partialUpdate_keepsExistingValues() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        long id = service.addComic("One Piece", 1, "Oda");

        assertTrue(service.updateComic(id, "One Piece", null, null, true));

        Comic after = service.findById(id).orElseThrow();
        assertEquals("One Piece", after.getTitle());
        assertEquals(1, after.getVolume());
        assertEquals("Oda", after.getAuthor());
        assertTrue(after.isRented());
    }

    @Test
    void updateComic_returnsFalseIfNotFound() {
        ComicService service = new ComicService(new InMemoryComicRepository());
        assertFalse(service.updateComic(999, "t", 1, "a", false));
    }

    @Test
    void deleteComic_returnsFalseIfNotFound() {
        ComicService service = new ComicService(new InMemoryComicRepository());
        assertFalse(service.deleteComic(999));
    }

    private static final class InMemoryComicRepository implements ComicRepository {
        private long sequence = 0L;
        private final Map<Long, Comic> store = new LinkedHashMap<>();

        @Override
        public long addComic(String title, int volume, String author) {
            long id = ++sequence;
            store.put(id, new Comic(id, title, volume, author, false, LocalDate.now()));
            return id;
        }

        @Override
        public List<Comic> getComics() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Optional<Comic> findById(long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public boolean updateComic(long id, String title, int volume, String author, boolean rented) {
            Comic before = store.get(id);
            if (before == null) {
                return false;
            }
            store.put(id, new Comic(id, title, volume, author, rented, before.getRegDate()));
            return true;
        }

        @Override
        public boolean deleteById(long id) {
            return store.remove(id) != null;
        }
    }
}

