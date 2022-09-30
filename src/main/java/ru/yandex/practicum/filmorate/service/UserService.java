package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private long counter = 0L;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validate(user);
        user.setId(++counter);
        userStorage.create(user);
        return user;
    }

    public User update(User data) {
        userStorage.get(data.getId());
        data.setId(data.getId());
        validate(data);
        userStorage.update(data);
        return data;
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
        userStorage.get(userId).getFriendIds().add(friendId);
        userStorage.get(friendId).getFriendIds().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(friendId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.get(userId).getFriendIds().remove(friendId);
        userStorage.get(friendId).getFriendIds().remove(userId);
    }

    public ArrayList<User> getMutualFriends(long userId, long otherId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(otherId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Long friendId : userStorage.get(userId).getFriendIds()) {
            if (userStorage.get(otherId).getFriendIds().contains(friendId)) {
                mutualFriends.add(userStorage.get(friendId));
            }
        }
        return mutualFriends;
    }

    public Collection<User> findFriends(Long userId) {
        return userStorage.getAll().stream().filter(p ->
                userStorage.get(userId).getFriendIds().contains(p.getId())).collect(Collectors.toList());
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
