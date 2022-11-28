package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, ModelMapper::makeGenre, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, ModelMapper::makeGenre);
    }

    @Override
    public List<Genre> getGenresByFilm(long filmId) {
        String sqlQuery = "SELECT * FROM genres " +
                "LEFT OUTER JOIN FILM_GENRES FG on GENRES.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        try {
            return jdbcTemplate.query(sqlQuery, ModelMapper::makeGenre, filmId);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("Указанный фильм не найден");
        }
    }

    @Override
    public void addFilmGenre(long filmId, long genreId) {
        String sqlQuery = "INSERT INTO film_genres(film_id, genre_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void removeFilmGenre(long filmId, long genreId) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }
}
