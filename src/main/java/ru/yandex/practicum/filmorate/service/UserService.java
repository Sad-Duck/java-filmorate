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
        if (isCrossFriend(userId, friendId)) { //если пользователь userId уже есть в друзьях у friendId то добавляемся в подтвержденные друзья у обоих юзеров
            userStorage.get(userId).getConfirmedFriendIds().add(friendId);
            userStorage.get(friendId).getConfirmedFriendIds().add(userId);
            userStorage.get(friendId).getNotConfirmedFriendIds().remove(userId);
        } else {
            userStorage.get(userId).getNotConfirmedFriendIds().add(friendId); // иначе добавляем в неподтвержденные
        }
    }

    public void removeFriend(long userId, long friendId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(friendId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userStorage.get(userId).getConfirmedFriendIds().contains(friendId) ||
                userStorage.get(userId).getNotConfirmedFriendIds().contains(friendId)) { // проверяем есть ли friendId в одном из списков
            if (isCrossFriend(userId, friendId)) { // если дружба подтвержденная то удаляем у userId и переносим в неподтвержденные у friendId
                userStorage.get(userId).getConfirmedFriendIds().remove(friendId);
                userStorage.get(friendId).getConfirmedFriendIds().remove(userId);
                userStorage.get(friendId).getNotConfirmedFriendIds().add(userId);
            } else {
                userStorage.get(userId).getNotConfirmedFriendIds().remove(friendId); // иначе просто удаляем из списка неподтвержденных
            }
        } else {
            throw new NotFoundException("Друг, которого требовалось удалить не обнаружен");
        }
    }

    public ArrayList<User> getMutualFriends(long userId, long otherId) {
        if ((userStorage.get(userId)) == null || (userStorage.get(otherId)) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Long friendId : userStorage.get(userId).getNotConfirmedFriendIds()) {
            if (userStorage.get(otherId).getConfirmedFriendIds().contains(friendId)) {
                mutualFriends.add(userStorage.get(friendId));
            }
        }
        return mutualFriends;
    } // метод проверит наличие общих друзей, только если они находятся в списке подтвержденных.

    public Collection<User> findFriends(Long userId) {
        return userStorage.getAll().stream().filter(p ->
                userStorage.get(userId).getConfirmedFriendIds().contains(p.getId())).collect(Collectors.toList());
    } // метод вернет только подтвержденных друзей.

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

    private boolean isCrossFriend(long userId, long friendId) {
        return userStorage.get(friendId).getNotConfirmedFriendIds().contains(userId); // если у friendId в списке неподтвержденных друзей есть userId - вернет true
    }

}
