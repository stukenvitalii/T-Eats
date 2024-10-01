package org.tinkoff.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tinkoff.userservice.entity.User;
import org.tinkoff.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User save(User entity) {
        return userRepository.save(entity);
    }

    public List<User> saveAll(Iterable<User> entities) {
        return userRepository.saveAll(entities);
    }
}
