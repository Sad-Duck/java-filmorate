package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql")
public class GenreControllerTest {

    private final GenreDbStorage genreStorage;

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreStorage.getAll();
        assertEquals(genres.size(), 2);
    }

    @Test
    public void testFindGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.get(1));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddFilmGenre() {
        genreStorage.addFilmGenre(1, 1);
        genreStorage.addFilmGenre(1, 2);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 2);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

    @Test
    public void testRemoveFilmGenre() {
        genreStorage.addFilmGenre(1, 1);
        genreStorage.addFilmGenre(1, 2);
        genreStorage.removeFilmGenre(1, 2);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 1);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

    @Test
    public void testGetGenresByFilm() {
        genreStorage.addFilmGenre(1, 1);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 1);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

}
