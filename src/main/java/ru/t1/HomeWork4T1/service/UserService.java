package ru.t1.HomeWork4T1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.t1.HomeWork4T1.model.User;
import ru.t1.HomeWork4T1.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public Optional<User> deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (userRepository.findById(id).isPresent() && id != 1) {
            userRepository.deleteById(id);
        }
        return user;
    }
}