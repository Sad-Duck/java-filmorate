package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa get(long id);
    List<Mpa> getAll();
    Mpa getMpaByFilm(long filmId);

}
