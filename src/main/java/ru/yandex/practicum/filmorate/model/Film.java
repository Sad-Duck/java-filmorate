package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Film extends StorageData {

    @NotEmpty(message = "название не может быть пустым")
    private String name;
    @Size(max = 200, message = "максимальный размер описания = 200")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private int duration;
    private Set<Long> userIds = new HashSet<>();
    private int rate = 0;
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
}
