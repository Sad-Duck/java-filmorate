package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface MpaStorage extends Storage<Mpa> {

    Mpa getMpaByFilm(long filmId);

}
