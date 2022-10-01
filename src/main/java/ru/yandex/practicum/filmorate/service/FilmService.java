package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long counter = 0L;
    private final Storage<Film> storage;
    private final Storage<User> userStorage;
    private static final Comparator<Film> FILM_COMPARATOR = (o1, o2) -> Integer.compare(o2.getUserIds().size(), o1.getUserIds().size());

    @Autowired
    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validate(film);
        film.setId(++counter);
        storage.create(film);
        return film;
    }

    public Film update(Film data) {
        storage.get(data.getId());
        data.setId(data.getId());
        validate(data);
        storage.update(data);
        return data;
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film get(long id) {
        return storage.get(id);
    }

    public void delete(long id) {
        storage.delete(id);
    }

    protected void validate(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            throw new NotValidException("Имя фильма неверное");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new NotValidException("Описание слишком длинное");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)) {
            throw new NotValidException("Дата выхода неверна");
        }
        if (film.getDuration() <= 0) {
            throw new NotValidException("Продолжительность неверна");
        }
    }

    public void addLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
    }

    public void removeLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

}
