package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mpa {
    private Integer id;
    private Rating rating;
}

enum Rating {
    G,
    PG,
    PG_13,
    R,
    NC_17
}
