package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface GenreStorage extends Storage<Genre> {
    List<Genre> getGenresByFilm(long FilmId);
    void addFilmGenre(long filmId, long genreId);
    void removeFilmGenre(long filmId, long genreId);
}
