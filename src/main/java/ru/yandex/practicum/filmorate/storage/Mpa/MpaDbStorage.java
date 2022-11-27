package ru.yandex.practicum.filmorate.storage.Mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, MpaDbStorage::makeMpa, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY MPA_ID";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa);
    }

    @Override
    public Mpa getMpaByFilm(long filmId) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id IN (SELECT mpa_id FROM films WHERE film_id = ?)";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, MpaDbStorage::makeMpa, filmId);
        } catch (DataAccessException e) {
            throw new NotFoundException("Mpa для фильма не найден");
        }
    }

    private static Mpa makeMpa(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("MPA_ID");
        String rating = rs.getString("RATING");
        return new Mpa(id, rating);
    }

}
