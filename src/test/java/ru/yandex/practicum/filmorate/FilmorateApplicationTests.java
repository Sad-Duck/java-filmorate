package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql")
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;

    private void createTestUserOne() {
        User testUserOne = new User();
        testUserOne.setName("testUserOne");
        testUserOne.setEmail("test@test.ru");
        testUserOne.setLogin("testLogin");
        testUserOne.setBirthday(LocalDate.of(2001, 1, 1));
        userStorage.create(testUserOne);
    }

    private void createTestUserTwo() {
        User testUserTwo = new User();
        testUserTwo.setName("testUserTwo");
        testUserTwo.setEmail("testTwo@test.ru");
        testUserTwo.setLogin("testLoginTwo");
        testUserTwo.setBirthday(LocalDate.of(2002, 2, 2));
        userStorage.create(testUserTwo);
    }

    private void createTestUserThree() {
        User testUserThree = new User();
        testUserThree.setName("testUserThree");
        testUserThree.setEmail("testTwo@test.ru");
        testUserThree.setLogin("testLoginThree");
        testUserThree.setBirthday(LocalDate.of(2003, 3, 3));
        userStorage.create(testUserThree);
    }

    private void createTestFilmOne() {
        Film testFilmOne = new Film();
        testFilmOne.setName("testFilmOne");
        testFilmOne.setDescription("testDescription");
        testFilmOne.setReleaseDate(LocalDate.of(2001, 1, 1));
        testFilmOne.setMpa(new Mpa(1L, "G"));
        testFilmOne.setDuration(100);
        testFilmOne.setRate(0);
        filmStorage.create(testFilmOne);
    }

    private void createTestFilmTwo() {
        Film testFilmTwo = new Film();
        testFilmTwo.setName("testFilmTwo");
        testFilmTwo.setDescription("testDescriptionTwo");
        testFilmTwo.setReleaseDate(LocalDate.of(2002, 2, 2));
        testFilmTwo.setMpa(new Mpa(3L, "PG_13"));
        testFilmTwo.setDuration(200);
        testFilmTwo.setRate(0);
        filmStorage.create(testFilmTwo);
    }

    private void createTestGenreOne() {
        Genre testGenreOne = new Genre("testGenreOne");
        genreStorage.create(testGenreOne);
    }

    private void createTestGenreTwo() {
        Genre testGenreTwo = new Genre("testGenreTwo");
        genreStorage.create(testGenreTwo);
    }

    // User

    @Test
    void testCreateUser() {
        createTestUserOne();

        User user = userStorage.get(1);
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testUserOne")
                                .hasFieldOrPropertyWithValue("login", "testLogin")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2001, 1, 1))
                );
    }

    @Test
    void testRemoveUser() {
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();
        userStorage.delete(3);

        List<User> users = userStorage.getAll();
        assertEquals(users.size(), 2);
    }

    @Test
    void testUpdateUser() {
        createTestUserOne();
        User user = userStorage.get(1);
        user.setLogin("testUpdate");
        userStorage.update(user);

        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testUserOne")
                                .hasFieldOrPropertyWithValue("login", "testUpdate")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2001, 1, 1))
                );
    }

    @Test
    public void testFindAllUsers() {
        createTestUserOne();
        createTestUserTwo();

        List<User> users = userStorage.getAll();
        assertEquals(users.size(), 2);
    }

    @Test
    public void testFindUserById() {
        createTestUserOne();
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddToFriends() {
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(3, 2);

        assertEquals(userStorage.getFriends(1).size(), 0);
        assertEquals(userStorage.getFriends(2).size(), 2);
    }

    @Test
    public void testRemoveFromFriends() {
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();
        userStorage.addToFriends(2, 1);
        userStorage.addToFriends(3, 1);
        userStorage.removeFromFriends(1, 3);

        assertEquals(userStorage.getFriends(1).size(), 1);
    }

    @Test
    public void testGetMutualFriends() {
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);

        assertEquals(userStorage.getMutualFriends(2, 3).size(), 1);
        assertEquals(userStorage.getMutualFriends(1, 3).size(), 0);
    }

    @Test
    public void getFriendsTest() {
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(3, 2);

        assertEquals(userStorage.getFriends(2).size(), 2);
    }

    // Film

    @Test
    public void createFilmTest() {
        createTestFilmOne();

        Film film = filmStorage.get(1);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("name", "testFilmOne")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2001, 1, 1))
                                .hasFieldOrPropertyWithValue("duration", 100)
                                .hasFieldOrPropertyWithValue("rate", 0)
                );

    }

    @Test
    void testRemoveFilm() {
        createTestFilmOne();
        createTestFilmTwo();
        filmStorage.delete(2);

        List<Film> films = filmStorage.getAll();
        assertEquals(films.size(), 1);
    }

    @Test
    void testUpdateFilm() {
        createTestFilmOne();

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
        createTestFilmOne();
        createTestFilmTwo();

        List<Film> films = filmStorage.getAll();
        assertEquals(films.size(), 2);
    }

    @Test
    public void testFindFilmById() {
        createTestFilmOne();
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.get(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddLike() {
        createTestFilmOne();
        createTestFilmTwo();
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(2, 1);
        List<Film> topFilms = filmStorage.getFilmsTop(2);

        assertEquals("testFilmOne", topFilms.get(0).getName());
    }

    @Test
    public void testRemoveLike() {
        createTestFilmOne();
        createTestFilmTwo();
        createTestUserOne();
        createTestUserTwo();
        createTestUserThree();

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
        createTestFilmOne();
        createTestFilmTwo();
        createTestUserOne();

        filmStorage.addLike(1, 1);

        List<Film> topFilms = filmStorage.getFilmsTop(1);

        assertEquals("testFilmOne", topFilms.get(0).getName());
    }

    @Test
    public void testGetFilmsByGenre() {
        createTestFilmOne();
        createTestFilmTwo();
        createTestGenreOne();
        createTestGenreTwo();

        genreStorage.addFilmGenre(1, 2);
        genreStorage.addFilmGenre(2, 1);

        List<Film> filmsByGenreTest = filmStorage.getFilmsByGenre(2);

        assertEquals("testFilmOne", filmsByGenreTest.get(0).getName());
    }

    //Genre

    @Test
    public void createGenreTest() {
        createTestGenreOne();

        Genre genre = genreStorage.get(1);
        assertThat(Optional.of(genre))
                .isPresent()
                .hasValueSatisfying(g ->
                        assertThat(g)
                                .hasFieldOrPropertyWithValue("name", "testGenreOne")
                );
    }

    @Test
    void testRemoveGenre() {
        createTestGenreOne();
        createTestGenreTwo();
        genreStorage.delete(2);

        List<Genre> genres = genreStorage.getAll();
        assertEquals(genres.size(), 1);
    }

    @Test
    void testUpdateGenre() {
        createTestGenreOne();

        Genre genre = genreStorage.get(1);
        genre.setName("genreUpdateTest");
        genreStorage.update(genre);

        assertThat(Optional.of(genre))
                .isPresent()
                .hasValueSatisfying(g ->
                        assertThat(g)
                                .hasFieldOrPropertyWithValue("name", "genreUpdateTest")
                );
    }

    @Test
    public void testFindAllGenres() {
        createTestGenreOne();
        createTestGenreTwo();

        List<Genre> genres = genreStorage.getAll();
        assertEquals(genres.size(), 2);
    }

    @Test
    public void testFindGenreById() {
        createTestGenreOne();
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.get(1));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddFilmGenre() {
        createTestGenreOne();
        createTestGenreTwo();
        createTestFilmOne();
        genreStorage.addFilmGenre(1, 1);
        genreStorage.addFilmGenre(1, 2);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 2);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

    @Test
    public void testRemoveFilmGenre() {
        createTestGenreOne();
        createTestGenreTwo();
        createTestFilmOne();
        genreStorage.addFilmGenre(1, 1);
        genreStorage.addFilmGenre(1, 2);
        genreStorage.removeFilmGenre(1, 2);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 1);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

    @Test
    public void testGetGenresByFilm() {
        createTestGenreOne();
        createTestFilmOne();
        genreStorage.addFilmGenre(1, 1);
        List<Genre> genreList = genreStorage.getGenresByFilm(1);

        assertEquals(genreList.size(), 1);
        assertEquals(genreList.get(0).getName(), "testGenreOne");
    }

    //Mpa

    @Test
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.get(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> allMpa = mpaStorage.getAll();
        assertEquals(allMpa.size(), 5);
    }

    @Test
    public void testGetMpaByFilm() {
        createTestFilmOne();
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaByFilm(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "G"));
    }

}


