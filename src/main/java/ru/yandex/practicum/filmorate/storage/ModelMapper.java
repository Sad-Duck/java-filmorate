package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModelMapper {

    public static Film makeFilm(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        int duration = rs.getInt("DURATION");
        int rate = rs.getInt("RATE");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        return new Film(id, name, description, releaseDate, duration, rate);
    }

    public static User makeUser(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    public static Genre makeGenre(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("GENRE_ID");
        String name = rs.getString("NAME");
        return new Genre(id, name);
    }

    public static Mpa makeMpa(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("MPA_ID");
        String rating = rs.getString("RATING");
        return new Mpa(id, rating);
    }

}
