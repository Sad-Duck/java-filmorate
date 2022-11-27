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

}
