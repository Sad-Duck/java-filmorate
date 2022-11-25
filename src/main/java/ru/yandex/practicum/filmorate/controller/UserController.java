package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<User> getUsers() {
        final List<User> users = service.getAll();
        log.info("Получен запрос списка пользователей {}", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Получен запрос пользователя по id: {}", id);
        return service.get(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Запрос на добавление пользователя, id {}", user.getId());
        return service.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя, id {}", user.getId());
        return service.update(user);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Запрос на получение общих друзей у {} и {}", id, otherId);
        return service.getMutualFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable long id) {
        log.info("Запрос на получение друзей у {}", id);
        return service.getFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Запрос на удаление друга {} от {}", friendId, id);
        service.removeFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя {}", id);
        service.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Запрос на добавления в друзья {} от {}", friendId, id);
        service.addFriend(id, friendId);
    }
}

