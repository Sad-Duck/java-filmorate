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
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> confirmedFriendIds = new HashSet<>();
    private Set<Long> notConfirmedFriendIds = new HashSet<>();
}
