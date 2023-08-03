package com.example.DevTeamProject_Notes.user;

import com.example.DevTeamProject_Notes.security.AccountController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserService userService;
    private Pattern pattern;

    @PostConstruct
    public void init() {
        pattern = Pattern.compile("^[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    }

    public boolean validate(User user, BindingResult result, AccountController accountController) {
        return checkDuplicateUser(user, result, accountController) || validateUsername(user, result, accountController);
    }

    public boolean checkDuplicateUser(User user, BindingResult result, AccountController accountController) {
        String errorMessage = "Користувач з таким логіном вже існує";
        if (userService.isUserDuplicated(user)) {
            return setErrorString(result, errorMessage);
        }
        return false;
    }

    public boolean validateUsername(User user, BindingResult result, AccountController accountController) {
        Matcher matcher = pattern.matcher(user.getLogin());
        String errorMessage = "Використовуйте тільки символи латиниці та цифри";
        if (!matcher.matches()) {
            return setErrorString(result, errorMessage);
        }
        return false;
    }

    private static boolean setErrorString(BindingResult result, String errorMessage) {
        String fieldName = "login";
        result.addError(new FieldError("user", fieldName, errorMessage));
        return true;
    }
}
