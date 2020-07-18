package org.rus.mir.controller;

import org.rus.mir.controller.util.ControllerUtils;
import org.rus.mir.domain.User;
import org.rus.mir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
        @RequestParam("password2") String password2,
        @Valid User user,
        BindingResult bindingResult,
        Model model
    ) {
        System.out.println(user);
        System.out.println(bindingResult.hasErrors());
        System.out.println(bindingResult.getAllErrors());
        boolean error = false;

        if (!Objects.equals(user.getPassword(), password2)) {
            model.addAttribute("passwordDiffError", "Passwords are different");
            error = true;
        }

        if (password2 == null || password2.equals("")) {
            model.addAttribute("password2", "Password confirmation can't be empty");
            error = true;
        }

        if (bindingResult.hasErrors() || error) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user);

            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists");
            model.addAttribute("user", user);
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
