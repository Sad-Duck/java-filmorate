package ru.yandex.practicum.filmorate;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void BeforeEach() {
        film = new Film();
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        film.setId(0L);
        film.setName("testName");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(2000, 10, 10));
        film.setDuration(120);
    }

    @Test
    void shouldNotValidateWhenNameIsBlank() {
        film.setName("");
        Throwable thrown = assertThrows(NotValidException.class, () -> filmController.create(film));
        assertEquals(thrown.getMessage(), "Имя фильма неверное");
    }

    @Test
    void shouldNotValidateWhenDescriptionIsLongerThan200() {
        String longString = StringUtils.repeat("*", 201);
        film.setDescription(longString);
        Throwable thrown = assertThrows(NotValidException.class, () -> filmController.create(film));
        assertEquals(thrown.getMessage(), "Описание слишком длинное");
    }

    @Test
    void shouldNotValidateWhenReleaseDateIsBeforeFirstFilm() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Throwable thrown = assertThrows(NotValidException.class, () -> filmController.create(film));
        assertEquals(thrown.getMessage(), "Дата выхода неверна");
    }

    @Test
    void shouldNotValidateWhenDurationIsNegative() {
        film.setDuration(-1);
        Throwable thrown = assertThrows(NotValidException.class, () -> filmController.create(film));
        assertEquals(thrown.getMessage(), "Продолжительность неверна");
    }

}
