package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre get(long id);
    List<Genre> getAll();
    List<Genre> getGenresByFilm(long FilmId);
    void addFilmGenre(long filmId, long genreId);
    void removeFilmGenre(long filmId, long genreId);
}
