package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User data) {
        if (userStorage.get(data.getId()) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        validate(data);
        return userStorage.update(data);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(long id) {
        return userStorage.get(id);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public void addFriend(long userId, long friendId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(friendId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.addToFriends(friendId, userId);
        //если какой-то пользователь оставил вам заявку в друзья, то он будет в списке ваших друзей, а вы в его — нет.
    }

    public void removeFriend(long userId, long friendId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(friendId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getMutualFriends(long userId, long otherId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(otherId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getMutualFriends(userId, otherId);
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    protected void validate(User user) {
        if (user.getEmail() == null) {
            throw new NotValidException("Email не указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new NotValidException("не верный формат Email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new NotValidException("не верный формат логина");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new NotValidException("не верно указана дата рождения");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
