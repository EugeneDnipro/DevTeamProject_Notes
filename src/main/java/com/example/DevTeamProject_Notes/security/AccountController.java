package com.example.DevTeamProject_Notes.security;

import com.example.DevTeamProject_Notes.user.User;
import com.example.DevTeamProject_Notes.user.UserRepository;
import com.example.DevTeamProject_Notes.user.UserService;
import com.example.DevTeamProject_Notes.user.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserValidation userValidation;


    @GetMapping("")
    public String beginning(@AuthenticationPrincipal CustomUserDetails loggedUser) {
        return "redirect:/note/list";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/register/save")
    public String registration(@Validated @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {

        if (!result.hasErrors()) {
            if (userValidation.validate(user, result, this))
                return "auth/register";
        } else {
            return "auth/register";
        }
        userService.save(user);
        System.out.println("userRepository.findAll() = " + userRepository.findAll());
        model.addAttribute("user", user);
        return "redirect:/login";
    }

}