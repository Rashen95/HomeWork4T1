package ru.t1.HomeWork4T1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.HomeWork4T1.dto.SignInRequest;
import ru.t1.HomeWork4T1.service.AuthenticationService;
import ru.t1.HomeWork4T1.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @PostMapping("/log")
    public ResponseEntity<String> loginUser(SignInRequest signInRequest) {
        if (userService.findByUserName(signInRequest.getUsername()).isPresent()
                && passwordEncoder.matches(signInRequest.getPassword(),
                userService.findByUserName(signInRequest.getUsername()).get().getPassword())
        ) {
            return ResponseEntity.ok(authenticationService.signIn(signInRequest).getToken());
        } else {
            return new ResponseEntity<>("Пользователь с таким логином и паролем не найден", HttpStatus.NOT_FOUND);
        }
    }
}