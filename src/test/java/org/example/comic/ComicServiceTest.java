package org.example.comic;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class ComicServiceTest {

    @Test
    void addComic_잘못된_입력이_주어지면_예외가_발생한다() {
        ComicService service = new ComicService(new InMemoryComicRepository());

        assertThrows(IllegalArgumentException.class, () -> service.addComic(" ", 1, "a"));
        assertThrows(IllegalArgumentException.class, () -> service.addComic("t", 0, "a"));
        assertThrows(IllegalArgumentException.class, () -> service.addComic("t", 1, " "));
    }

    @Test
    void addComic_정상적인_입력이_주어지면_만화가_저장된다()  {
        ComicService service = new ComicService(new InMemoryComicRepository());

        long id = service.addComic("One Piece", 1, "Oda");

        Comic comic = service.findById(id).orElseThrow();
        assertEquals("One Piece", comic.getTitle());
        assertEquals(1, comic.getVolume());
        assertEquals("Oda", comic.getAuthor());
    }

    @Test
    void getComics_여러_만화를_저장하면_목록으로_조회된다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        service.addComic("One Piece", 1, "Oda");
        service.addComic("Naruto", 1, "Kishimoto");

        List<Comic> comics = service.getComics();

        assertEquals(2, comics.size());
    }

    @Test
    void getComics_저장된_만화의_정보가_정확하게_조회된다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        service.addComic("One Piece", 1, "Oda");
        service.addComic("Naruto", 2, "Kishimoto");

        List<Comic> comics = service.getComics();

        Comic first = comics.get(0);
        Comic second = comics.get(1);

        assertEquals("One Piece", first.getTitle());
        assertEquals(1, first.getVolume());
        assertEquals("Oda", first.getAuthor());

        assertEquals("Naruto", second.getTitle());
        assertEquals(2, second.getVolume());
        assertEquals("Kishimoto", second.getAuthor());
    }

    @Test
    void getComics_저장된_만화가_없으면_빈_리스트를_반환한다() {
        ComicService service = new ComicService(new InMemoryComicRepository());

        List<Comic> comics = service.getComics();

        assertTrue(comics.isEmpty());
    }

    @Test
    void updateComic_null_파라미터가_주어지면_기존값이_유지된다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        long id = service.addComic("One Piece", 1, "Oda");

        assertTrue(service.updateComic(id, "One Piece", null, null));

        Comic after = service.findById(id).orElseThrow();
        assertEquals("One Piece", after.getTitle());
        assertEquals(1, after.getVolume());
        assertEquals("Oda", after.getAuthor());
    }

    @Test
    void updateComic_존재하지_않는_ID가_주어지면_false를_반환한다() {
        ComicService service = new ComicService(new InMemoryComicRepository());
        assertFalse(service.updateComic(999, "t", 1, "a"));
    }

    @Test
    void updateComic_정상적인_수정값이_주어지면_값이_변경된다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        long id = service.addComic("One Piece", 1, "Oda");

        boolean result = service.updateComic(id, "Naruto", 2, "Kishimoto");

        assertTrue(result);

        Comic after = service.findById(id).orElseThrow();
        assertEquals("Naruto", after.getTitle());
        assertEquals(2, after.getVolume());
        assertEquals("Kishimoto", after.getAuthor());
    }

    @Test
    void updateComic_일부_필드만_주어지면_해당_필드만_변경된다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        long id = service.addComic("One Piece", 1, "Oda");

        service.updateComic(id, null, 10, null);

        Comic after = service.findById(id).orElseThrow();
        assertEquals("One Piece", after.getTitle()); // 유지
        assertEquals(10, after.getVolume());         // 변경
        assertEquals("Oda", after.getAuthor());      // 유지
    }

    @Test
    void deleteComic_정상적으로_삭제되면_더이상_조회되지_않는다() {
        InMemoryComicRepository repo = new InMemoryComicRepository();
        ComicService service = new ComicService(repo);

        long id = service.addComic("One Piece", 1, "Oda");

        boolean result = service.deleteComic(id);

        assertTrue(result);
        assertTrue(service.findById(id).isEmpty());
    }

    @Test
    void deleteComic_존재하지_않는_ID가_주어지면_false를_반환한다() {
        ComicService service = new ComicService(new InMemoryComicRepository());
        assertFalse(service.deleteComic(999));
    }


    // 테스트 전용 인메모리 구현체 (DB 없이 ComicRepository 동작을 검증하기 위해 사용)
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
        public boolean updateComic(long id, String title, int volume, String author) {
            Comic before = store.get(id);
            if (before == null) {
                return false;
            }
            store.put(id, new Comic(id, title, volume, author, before.isRented(), before.getRegDate()));
            return true;
        }

        @Override
        public boolean deleteById(long id) {
            return store.remove(id) != null;
        }
    }
}
