package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mpa extends StorageData {
    private String name;

    public Mpa(Long id, String name) {
        super(id);
        this.name = name;
    }
}

