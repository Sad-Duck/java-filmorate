package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {
    private User user;

    private static UserController userController;

    @Autowired
    public UserControllerTest(UserService userService) {
        userController = new UserController(userService);
    }

    @BeforeEach
    public void BeforeEach() {
        user = new User();
        user.setName("Имя");
        user.setId(0L);
        user.setLogin("loginExample");
        user.setEmail("emailExample@email.com");
        user.setBirthday(LocalDate.of(2000, 10, 10));
    }

    @Test
    void shouldNotValidateWhenEmailIsBlank() {
        user.setEmail("");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "не верный формат Email");
    }

    @Test
    void shouldNotValidateWhenEmailHasNoAtSign() {
        user.setEmail("emailWithoutAtSign");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "не верный формат Email");
    }

    @Test
    void shouldNotValidateWhenLoginIsBlank() {
        user.setLogin("");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "не верный формат логина");
    }

    @Test
    void shouldNotValidateWhenLoginContainsEmptySpace() {
        user.setLogin("login with empty space");
        Throwable thrown = assertThrows(NotValidException.class, () -> userController.createUser(user));
        assertEquals(thrown.getMessage(), "не верный формат логина");
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
        assertEquals(thrown.getMessage(), "не верно указана дата рождения");
    }

}
