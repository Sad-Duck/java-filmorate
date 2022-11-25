package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody Genre genre) {
        log.info("Запрос на добавление жанра, id {}", genre.getId());
        service.create(genre);
    }

    @PutMapping
    public Genre update(@RequestBody Genre genre) {
        log.info("Запрос на обновление жанра, id {}", genre.getId());
        return service.update(genre);
    }

    @GetMapping
    public List<Genre> getAll() {
        final List<Genre> genres = service.getAll();
        log.info("Получен запрос списка жанров {}", genres.size());
        return genres;
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable long id) {
        log.info("Получен запрос жанра по id: {}", id);
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void removeGenre(@PathVariable Long id) {
        log.info("Получен запрос на удаление жанра: {}", id);
        service.delete(id);
    }

    @GetMapping("/film-genres/{filmId}")
    public List<Genre> getByFilm(@PathVariable long filmId) {
        log.info("Запрос на получение жанров фильма {}", filmId);
        return service.getGenresByFilm(filmId);
    }

    @PutMapping("/film-genres/{filmId}")
    public void addGenreToFilm(@PathVariable long filmId, long genreId) {
        log.info("Получен запрос на добавление жанра {} к фильму {}", genreId, filmId);
        service.addFilmGenre(filmId, genreId);
    }

    @DeleteMapping("/film-genres/{filmId}")
        public void removeGenreFromFilm(@PathVariable long filmId, long genreId) {
            log.info("Получен запрос на удаление жанра {} к фильму {}", genreId, filmId);
            service.removeFilmGenre(filmId, genreId);
        }
}
