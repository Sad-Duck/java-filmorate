package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends StorageData {
    String email;
    String login;
    String name;
    LocalDate birthday;
    private Set<Long> friendIds = new HashSet<>();
}
