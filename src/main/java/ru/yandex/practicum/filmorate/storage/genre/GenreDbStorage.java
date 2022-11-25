package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.persistence.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            return jdbcTemplate.queryForObject(sqlQuery, GenreDbStorage::makeGenre, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    public Genre create(Genre data) {
        String sqlQuery = "INSERT INTO genres(genre) " +
                "VALUES (?)";
        jdbcTemplate.update(sqlQuery,
                data.getName());
        return data;
    }

    public void delete(long id) {
        String sqlQuery = "DELETE FROM genres WHERE genre_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Genre update(Genre data) {
        String sql = "UPDATE genres SET genre = ? WHERE genre_id = ?";
        jdbcTemplate.update(sql,
                data.getName(), data.getId());
        return data;
    }

    @Override
    public List<Genre> getGenresByFilm(long filmId) {
        String sqlQuery = "SELECT * FROM genres WHERE GENRE_ID IN (SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        try {
            return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, filmId);
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

    private static Genre makeGenre(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("GENRE_ID");
        String name = rs.getString("GENRE");
        return new Genre(id, name);
    }
}