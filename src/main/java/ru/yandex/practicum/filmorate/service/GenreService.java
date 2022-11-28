package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre get(long id) {
        return genreStorage.get(id);
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public List<Genre> getGenresByFilm(long filmId) {
        return genreStorage.getGenresByFilm(filmId);
    }

    public void addFilmGenre(long filmId, long genreId) {
        genreStorage.addFilmGenre(filmId, genreId);
    }

    public void removeFilmGenre(long filmId, long genreId) {
        genreStorage.removeFilmGenre(filmId, genreId);
    }
}
