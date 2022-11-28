package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTest.sql")

public class MpaControllerTest {

    private final MpaDbStorage mpaStorage;

    @Test
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.get(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> allMpa = mpaStorage.getAll();
        assertEquals(allMpa.size(), 5);
    }

    @Test
    public void testGetMpaByFilm() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaByFilm(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "G"));
    }

}
