package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Вы не указали имя.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из букв")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Вы не указали фамилию.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из букв")
    private String surname;

    @Column(name = "email")
    @NotEmpty(message = "Вы не указали свой email.")
    @Email(message = "Введите корректный email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Вы не ввели пароль.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]():;',?/*~$^+=<>]).{8,}$",
            message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nДлина пароля должна быть не менее 8 символов. ")
    private String password;

    @Column(name = "role")
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
