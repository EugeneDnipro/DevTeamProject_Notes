package com.example.DevTeamProject_Notes.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private BindingResult result;

    @Test
    void save() {
        User user = getUser();
        when(userRepository.save(user)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("hash");
        userService.save(user);

        verify(userRepository, times(1)).save(user);
        assertNotEquals(user.getPassword(), "qwerty");
        assertEquals(user.getPassword(), "hash");
    }

    @Test
    void checkDuplicateUser() {
        User user = getUser();

        DuplicateKeyException ex = new DuplicateKeyException("Користувач з таким логіном вже існує");
        when(userRepository.save(user)).thenThrow(ex);

        String checked = userService.checkDuplicateUser(user, result);

        assertEquals(checked, "auth/register");

        String fieldName = "login";
        String errorMessage = "Користувач з таким логіном вже існує";
        verify(result, times(1)).addError(new FieldError("user", fieldName, errorMessage));
    }

    private static User getUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("qwerty");
        user.setRole(Role.ROLE_USER);
        return user;
    }
}