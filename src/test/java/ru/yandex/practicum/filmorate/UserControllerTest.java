package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql")
public class UserControllerTest {

    private final UserDbStorage userStorage;

    @Test
    void testCreateUser() {

        User testUserOne = new User();
        testUserOne.setName("testCreateUser");
        testUserOne.setEmail("test@test.ru");
        testUserOne.setLogin("testLogin");
        testUserOne.setBirthday(LocalDate.of(2004, 4, 4));
        userStorage.create(testUserOne);

        User user = userStorage.get(4);
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 4L)
                                .hasFieldOrPropertyWithValue("name", "testCreateUser")
                                .hasFieldOrPropertyWithValue("login", "testLogin")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2004, 4, 4))
                );
    }

    @Test
    void testRemoveUser() {
        userStorage.delete(3);

        List<User> users = userStorage.getAll();
        assertEquals(users.size(), 2);
    }

    @Test
    void testUpdateUser() {
        User user = userStorage.get(1);
        user.setLogin("testUpdate");
        userStorage.update(user);

        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testUserOne")
                                .hasFieldOrPropertyWithValue("login", "testUpdate")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2001, 1, 1))
                );
    }

    @Test
    public void testFindAllUsers() {

        List<User> users = userStorage.getAll();
        assertEquals(users.size(), 3);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddToFriends() {
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(3, 2);

        assertEquals(userStorage.getFriends(1).size(), 0);
        assertEquals(userStorage.getFriends(2).size(), 2);
    }

    @Test
    public void testRemoveFromFriends() {
        userStorage.addToFriends(2, 1);
        userStorage.addToFriends(3, 1);
        userStorage.removeFromFriends(1, 3);

        assertEquals(userStorage.getFriends(1).size(), 1);
    }

    @Test
    public void testGetMutualFriends() {
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);

        assertEquals(userStorage.getMutualFriends(2, 3).size(), 1);
        assertEquals(userStorage.getMutualFriends(1, 3).size(), 0);
    }

    @Test
    public void getFriendsTest() {
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(3, 2);

        assertEquals(userStorage.getFriends(2).size(), 2);
    }

}
