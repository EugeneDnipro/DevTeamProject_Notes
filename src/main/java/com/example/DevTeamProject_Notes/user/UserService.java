package com.example.DevTeamProject_Notes.user;

import com.example.DevTeamProject_Notes.security.AccountController;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        String loginInLowerCase = user.getLogin().toLowerCase();
        if (userRepository.existsByLogin(loginInLowerCase)){
            throw new DuplicateKeyException("User with this login already exists");
        }
        user.setLogin(loginInLowerCase);
        user.setRole(Role.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String checkDuplicateUser(User user, BindingResult result, AccountController accountController) {
        try {
            save(user);
        } catch (DuplicateKeyException ex) {
            String fieldName = "login";
            String errorMessage = "Користувач з таким логіном вже існує";
            result.addError(new FieldError("user", fieldName, errorMessage));
            return "auth/register";
        }
        return null;
    }
}
