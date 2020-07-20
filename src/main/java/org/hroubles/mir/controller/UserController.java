package org.hroubles.mir.controller;

import org.hroubles.mir.domain.User;
import org.hroubles.mir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
