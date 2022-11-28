package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.ModelMapper;

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
            return jdbcTemplate.queryForObject(sqlQuery, ModelMapper::makeMpa, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY MPA_ID";
        return jdbcTemplate.query(sqlQuery, ModelMapper::makeMpa);
    }

    @Override
    public Mpa getMpaByFilm(long filmId) {
        String sqlQuery = "SELECT * FROM MPA LEFT OUTER JOIN FILMS F ON MPA.MPA_ID = F.MPA_ID WHERE FILM_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, ModelMapper::makeMpa, filmId);
        } catch (DataAccessException e) {
            throw new NotFoundException("Mpa для фильма не найден");
        }
    }
}
