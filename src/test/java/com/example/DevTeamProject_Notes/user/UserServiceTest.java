package com.example.DevTeamProject_Notes.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Test
    void save() {
        User user = new User();

        user.setRole(Role.ROLE_USER);
        user.setPassword("qwerty");
        user.setLogin("testuser");
        when(userRepository.save(user)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("hash");
        userService.save(user);

        verify(userRepository, times(1)).save(user);
        assertNotEquals(user.getPassword(), "qwerty");
        assertEquals(user.getPassword(), "hash");
    }
}