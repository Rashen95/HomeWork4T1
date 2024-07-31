package ru.t1.HomeWork4T1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.HomeWork4T1.dto.SignUpRequest;
import ru.t1.HomeWork4T1.service.AuthenticationService;
import ru.t1.HomeWork4T1.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/reg")
    public ResponseEntity<String> registerUser(SignUpRequest signUpRequest) {
        if (userService.findByUserName(signUpRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>(
                    String.format("Пользователь с логином %s уже существует", signUpRequest.getUsername()),
                    HttpStatus.CONFLICT
            );
        } else {
            authenticationService.signUp(signUpRequest);
            return new ResponseEntity<>(
                    String.format("Пользователь с логином %s успешно зарегистрирован", signUpRequest.getUsername()),
                    HttpStatus.CREATED
            );
        }
    }
}