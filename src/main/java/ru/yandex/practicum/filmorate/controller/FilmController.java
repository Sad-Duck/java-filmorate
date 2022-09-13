package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        log.info("получен GET запрос в /films");
        return films.values();
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        log.info("получен POST запрос в /films");
        if (validate(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавили фильм {}", film);
            return film;
        } else {
            log.error("Входящие данные не прошли валидацию");
            throw new NotValidException("Входящие данные не прошли валидацию");
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("получен PUT запрос в /films");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновили фильм " + film.getName());
        } else {
            log.error("Id фильма не найдено");
            throw new NotValidException("Id фильма не найдено");
        }
        return film;
    }

    private boolean validate(Film film) {
        return !film.getName().isBlank() &&
                film.getDescription().length() <= 200 &&
                !film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE) &&
                film.getDuration() > 0;
    }
}
