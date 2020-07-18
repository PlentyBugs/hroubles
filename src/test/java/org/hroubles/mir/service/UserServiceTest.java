package org.hroubles.mir.service;

import org.hamcrest.CoreMatchers;
import org.hroubles.mir.domain.enums.Role;
import org.hroubles.mir.domain.User;
import org.hroubles.mir.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailService mailService;

    @Test
    public void addUser() {
        User user = new User();

        user.setEmail("123@mail.ru");

        assertTrue(userService.addUser(user));
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailService, Mockito.times(1))
                .send(ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.endsWith("Activation Code"),
                        ArgumentMatchers.contains("Welcome to Mir"));
    }

    @Test
    public void addUserFailTest() {
        User user = new User();

        user.setUsername("Mock");

        Mockito.doReturn(new User()).when(userRepository).findByUsername("Mock");

        assertFalse(userService.addUser(user));

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailService, Mockito.times(0))
                .send(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString());
    }

    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("bingo!");
        Mockito.doReturn(user).when(userRepository).findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFalse() {
        assertFalse(userService.activateUser("activate me"));
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}
