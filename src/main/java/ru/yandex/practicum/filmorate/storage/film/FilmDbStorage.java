package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.ModelMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film create(Film data) {
        String sql = "INSERT INTO FILMS(name, description, mpa_id, release_date, duration, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, data.getName());
            stmt.setString(2, data.getDescription());
            if (data.getMpa() != null) {
                stmt.setLong(3, data.getMpa().getId());
            }
            stmt.setDate(4, Date.valueOf(data.getReleaseDate()));
            stmt.setInt(5, data.getDuration());
            stmt.setInt(6, data.getRate());
            return stmt;
        }, keyHolder);
        data.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return data;
    }

    @Override
    public Film update(Film data) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, mpa_id = ?, release_date = ?, duration = ?, rate = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                data.getName(),
                data.getDescription(),
                data.getMpa().getId(),
                data.getReleaseDate(),
                data.getDuration(),
                data.getRate(),
                data.getId());
        return data;
    }

    @Override
    public Film get(long id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, ModelMapper::makeFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не найден");
        }
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, ModelMapper::makeFilm);
    }

    public void addLike(long id, long userId) {
        String sqlQuery = "INSERT INTO LIKES(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public void removeLike(long id, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public List<Film> getFilmsTop(int count) {

        String sqlQuery = "SELECT films.* FROM films LEFT JOIN likes ON films.film_id = likes.film_id " +
                "GROUP BY films.film_id ORDER BY COUNT(LIKES.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, ModelMapper::makeFilm, count);
    }
}
