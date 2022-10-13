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
public class Film extends StorageData {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> userIds = new HashSet<>();
    private long rate = 0;

    public void addLike(long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(Long userId) {
        userIds.remove(userId);
        rate = userIds.size();
    }
}
