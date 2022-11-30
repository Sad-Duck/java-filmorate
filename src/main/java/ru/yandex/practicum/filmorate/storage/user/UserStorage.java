package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface UserStorage extends Storage<User> {

    void addToFriends(long id, long friendId);

    void removeFromFriends(long id, long friendId);

    List<User> getMutualFriends(long id, long otherId);

    List<User> getFriends(long id);

}
