package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends StorageData {
    @Email(message = "электронная почта не соответствует необходимому формату")
    private String email;
    @NotBlank(message = "логин не соответствует необходимому формату")
    @NotEmpty(message = "логин не соответствует необходимому формату")
    private String login;
    private String name;
    @Past(message = "дата рождения не должна быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Set<Long> FriendIds = new HashSet<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
