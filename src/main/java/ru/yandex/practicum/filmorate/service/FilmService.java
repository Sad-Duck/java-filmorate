package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage storage;
    private final UserService userService;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Autowired
    public FilmService(FilmStorage storage,
                       UserService userService,
                       GenreService genreService,
                       MpaService mpaService) {
        this.storage = storage;
        this.userService = userService;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public Film create(Film data) {
        validate(data);
        Film newFilm = storage.create(data);

        if (!newFilm.getGenres().isEmpty()) {
            data.getGenres().stream()
                    .map(StorageData::getId)
                    .collect(Collectors.toSet())
                    .forEach(id -> genreService.addFilmGenre(newFilm.getId(), id)); // добавляем данные о жанрах
        }
        newFilm.setMpa(mpaService.get(newFilm.getMpa().getId()));

        return newFilm;
    }

    public Film update(Film data) {
        validate(data);
        Film filmUpdate = storage.update(data);
        Film oldFilm = get(data.getId());

        oldFilm.getGenres().forEach(g -> genreService.removeFilmGenre(oldFilm.getId(), g.getId())); //удаляем старые данные о жанрах
        if (data.getGenres().size() != 0) {
            data.getGenres().stream()
                    .map(StorageData::getId)
                    .collect(Collectors.toSet())
                    .forEach(id -> genreService.addFilmGenre(oldFilm.getId(), id)); // добавляем новые данные о жанрах

            filmUpdate.setGenres(genreService.getGenresByFilm(filmUpdate.getId()));
        }
        filmUpdate.setMpa(mpaService.get(filmUpdate.getMpa().getId()));

        return filmUpdate;
    }

    public List<Film> getAll() {
        List<Film> films = storage.getAll();
        films.forEach(film -> film.setMpa(mpaService.getMpaByFilm(film.getId())));
        films.forEach(film -> film.setGenres(genreService.getGenresByFilm(film.getId())));
        return films;
    }

    public Film get(long id) {
        Film film = storage.get(id);
        film.setMpa(mpaService.getMpaByFilm(film.getId()));
        film.setGenres(genreService.getGenresByFilm(film.getId()));
        return film;
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

    public void addLike(long filmId, long userId) {
        storage.addLike(get(filmId).getId(), userService.get(userId).getId());
    }

    public void removeLike(long filmId, long userId) {
        storage.removeLike(get(filmId).getId(), userService.get(userId).getId());
    }

    public List<Film> getPopular(int count) {
        List<Film> films = storage.getFilmsTop(count);
        films.forEach(film -> film.setMpa(mpaService.getMpaByFilm(film.getId())));
        return films;
    }
}
