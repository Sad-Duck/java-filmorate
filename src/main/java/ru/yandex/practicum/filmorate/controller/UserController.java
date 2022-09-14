package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("получен GET запрос в /users");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("получен POST запрос в /users");
        if (validate(user)) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавили пользователя {}", user);
            return user;
        } else {
            log.error("Входящие данные не прошли валидацию");
            throw new NotValidException("Входящие данные не прошли валидацию");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("получен PUT запрос в /users");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновили пользователя " + user.getLogin());
        } else {
            log.error("Id пользователя не найдено");
            throw new NotValidException("Id пользователя не найдено");
        }
        return user;
    }

    private boolean validate(User user) {
        return user.getEmail() != null &&
                user.getEmail().contains("@") &&
                !user.getLogin().contains(" ") &&
                !user.getLogin().isBlank() &&
                !user.getBirthday().isAfter(LocalDate.now());
    }

}
