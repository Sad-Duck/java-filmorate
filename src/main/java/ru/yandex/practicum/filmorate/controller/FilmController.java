package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Запрос на добавление фильма, id {}", film.getId());
        return service.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.info("Запрос на обновление фильма, id {}", film.getId());
        return service.update(film);
    }

    @GetMapping()
    public List<Film> getAll() {
        final List<Film> films = service.getAll();
        log.info("Получен запрос списка фильмов {}", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.info("Получен запрос фильма по id: {}", id);
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable Long id) {
        log.info("Запрос на удаление фильма {}", id);
        service.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос добавление лайка к фильму {} от пользователя {}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос удаление лайка к фильму {} от пользователя {}", id, userId);
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос списка популярных фильмов. Размер - {}", count);
        return service.getPopular(count);
    }
}
