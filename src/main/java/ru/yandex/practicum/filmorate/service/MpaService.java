package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaByFilm(long filmId) {
        return mpaStorage.getMpaByFilm(filmId);
    }

    public Mpa get(long id) {
        return mpaStorage.get(id);
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }
}
