package com.example.DevTeamProject_Notes.user;

import com.example.DevTeamProject_Notes.security.AccountController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private Pattern pattern;

    @PostConstruct
    public void init() {
        pattern = Pattern.compile("^[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    }

    public void save(User user) {
        String loginInLowerCase = user.getLogin().toLowerCase();
        if (userRepository.existsByLogin(loginInLowerCase)) {
            throw new DuplicateKeyException("User with this login already exists");
        }
        user.setLogin(loginInLowerCase);
        user.setRole(Role.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String checkDuplicateUser(User user, BindingResult result, AccountController accountController) {
        String errorMessage = "Користувач з таким логіном вже існує";
        try {
            save(user);
        } catch (DuplicateKeyException ex) {
            return setErrorString(result, errorMessage);
        }
        return null;
    }

    public String validateUsername(User user, BindingResult result, AccountController accountController) {
        Matcher matcher = pattern.matcher(user.getLogin());
        String errorMessage = "Використовуйте тільки символи латиниці та цифри";
        if (!matcher.matches()) {
            return setErrorString(result, errorMessage);
        }
        return null;
    }

    private static String setErrorString(BindingResult result, String errorMessage) {
        String fieldName = "login";
        result.addError(new FieldError("user", fieldName, errorMessage));
        return "auth/register";
    }

}
