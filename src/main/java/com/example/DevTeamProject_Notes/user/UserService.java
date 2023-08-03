package com.example.DevTeamProject_Notes.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        String loginInLowerCase = user.getLogin().toLowerCase();
        user.setLogin(loginInLowerCase);
        user.setRole(Role.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    boolean isUserDuplicated(User user) {
        return userRepository.existsByLogin(user.getLogin().toLowerCase());
    }

}
