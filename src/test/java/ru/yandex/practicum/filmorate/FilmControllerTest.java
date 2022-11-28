package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql")
public class FilmControllerTest {

    private final FilmDbStorage filmStorage;

    @Test
    public void createFilmTest() {

        Film testFilmOne = new Film();
        testFilmOne.setName("testCreateFilm");
        testFilmOne.setDescription("testDescription");
        testFilmOne.setReleaseDate(LocalDate.of(2004, 4, 4));
        testFilmOne.setMpa(new Mpa(1L, "G"));
        testFilmOne.setDuration(400);
        testFilmOne.setRate(0);
        filmStorage.create(testFilmOne);

        Film film = filmStorage.get(3);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("name", "testCreateFilm")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2004, 4, 4))
                                .hasFieldOrPropertyWithValue("duration", 400)
                                .hasFieldOrPropertyWithValue("rate", 0)
                );
    }

    @Test
    void testRemoveFilm() {
        filmStorage.delete(2);

        List<Film> films = filmStorage.getAll();
        assertEquals(films.size(), 1);
    }

    @Test
    void testUpdateFilm() {
        Film film = filmStorage.get(1);
        film.setName("testNameUpdated");
        film.setMpa(new Mpa(1L, "G"));
        filmStorage.update(film);

        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("name", "testNameUpdated")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2001, 1, 1))
                                .hasFieldOrPropertyWithValue("duration", 100)
                                .hasFieldOrPropertyWithValue("rate", 0)
                );
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.getAll();
        assertEquals(films.size(), 2);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.get(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddLike() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(2, 1);
        List<Film> topFilms = filmStorage.getFilmsTop(2);

        assertEquals("testFilmOne", topFilms.get(0).getName());
    }

    @Test
    public void testRemoveLike() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(2, 1);
        filmStorage.removeLike(1, 1);
        filmStorage.removeLike(1, 2);
        List<Film> topFilms = filmStorage.getFilmsTop(2);

        assertEquals("testFilmTwo", topFilms.get(0).getName());
    }

    @Test
    public void testGetFilmsTop() {
        filmStorage.addLike(1, 1);
        List<Film> topFilms = filmStorage.getFilmsTop(1);

        assertEquals("testFilmOne", topFilms.get(0).getName());
    }
}
