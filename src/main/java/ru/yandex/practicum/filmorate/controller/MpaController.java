package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable long id) {
        log.info("Получен запрос рейтинга MPA по id: {}", id);
        return service.get(id);
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Получен запрос списка MPA");
        return service.getAll();
    }

}
