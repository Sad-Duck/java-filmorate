package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User data) {
        String sql = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, data.getEmail());
            stmt.setString(2, data.getLogin());
            stmt.setString(3, data.getName());
            stmt.setDate(4, Date.valueOf(data.getBirthday()));
            return stmt;
        }, keyHolder);
        data.setId((Objects.requireNonNull(keyHolder.getKey()).longValue()));

        return data;
    }

    @Override
    public User update(User data) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                data.getEmail(),
                data.getLogin(),
                data.getName(),
                data.getBirthday(),
                data.getId());
        return data;
    }

    @Override
    public User get(long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, UserDbStorage::makeUser, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser);
    }

    public void addToFriends(long id, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, friendId, id);
    }
    // если какой-то пользователь оставил вам заявку в друзья, то он будет в списке ваших друзей, а вы в его — нет

    public void removeFromFriends(long id, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    public List<User> getMutualFriends(long id, long otherId) {
        String sqlQuery =
                "SELECT * FROM USERS WHERE USER_ID IN " +
                        "(SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? " +
                        "AND FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?))";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id, otherId);
    }

    public List<User> getFriends(long id) {
        String sqlQuery = "SELECT * FROM users JOIN FRIENDS ON USERS.USER_ID = FRIENDS.FRIEND_ID " +
                "WHERE FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
    }

    private static User makeUser(ResultSet rs, int rownum) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
