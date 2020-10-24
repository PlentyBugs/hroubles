package org.hroubles.mir.controller;

import org.hroubles.mir.domain.User;
import org.hroubles.mir.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getUser(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            Model model
    ) {
        Optional<User> userOptional = userService.findById(id);
        User user = userOptional.orElse(new User());
        model.addAttribute("user", user);
        model.addAttribute("isCurrentUser", currentUser.getId().equals(user.getId()));

        return "userPage";
    }

    @PostMapping("/{id}")
    public String updateUser(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestParam String email,
            @RequestParam String password
    ) {
        Optional<User> userOptional = userService.findById(id);
        User user;

        if (userOptional.isPresent() && (user = userOptional.get()).getId().equals(currentUser.getId())) {
            userService.updateProfile(user, password, email);
        }

        return "redirect:/user/" + id;
    }
}
