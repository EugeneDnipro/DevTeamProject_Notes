package com.example.DevTeamProject_Notes.security;

import com.example.DevTeamProject_Notes.user.Role;
import com.example.DevTeamProject_Notes.user.User;
import com.example.DevTeamProject_Notes.user.UserValidation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AccountControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private UserValidation userValidation;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void beginning() {
        mvc.perform(get(""))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/note/list"));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void login() {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void showRegistrationForm() {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attribute("user", new User()));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void registration() {
        User user = getUser();
        when(userValidation.validate(any(User.class), any(BindingResult.class), any(AccountController.class)))
                .thenReturn(true);
        mvc.perform(post("/register/save")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void registrationNotDuplicatedUser() {
        User user = getUser();
        when(userValidation.validate(any(User.class), any(BindingResult.class), any(AccountController.class)))
                .thenReturn(false);
        mvc.perform(post("/register/save")
                        .flashAttr("user", user))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    private static User getUser() {
        User user = new User();

        user.setRole(Role.ROLE_USER);
        user.setPassword("qwerty123");
        user.setLogin("testuser");
        return user;
    }
}