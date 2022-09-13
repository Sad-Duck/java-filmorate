package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    public void BeforeEach() {
        userController = new UserController();
        user = new User();
        user.setName("Имя");
        user.setId(0);
        user.setLogin("loginExample");
        user.setEmail("emailExample@email.com");
        user.setBirthday(LocalDate.of(2000, 10, 10));
    }

    @Test
    void shouldNotValidateWhenEmailIsBlank() {
        user.setEmail("");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "Входящие данные не прошли валидацию");
    }

    @Test
    void shouldNotValidateWhenEmailHasNoAtSign() {
        user.setEmail("emailWithoutAtSign");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "Входящие данные не прошли валидацию");
    }

    @Test
    void shouldNotValidateWhenLoginIsBlank() {
        user.setLogin("");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "Входящие данные не прошли валидацию");
    }

    @Test
    void shouldNotValidateWhenLoginContainsEmptySpace() {
        user.setLogin("login with empty space");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "Входящие данные не прошли валидацию");
    }

    @Test
    void shouldReplaceEmptyNameWithLogin() {
        user.setName(null);
        userController.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldNotValidateWhenDateOfBirthIsInFuture() {
        user.setBirthday(LocalDate.of(3000, 10, 10));
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "Входящие данные не прошли валидацию");
    }

}
