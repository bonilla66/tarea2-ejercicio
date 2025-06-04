package com.twitter.demo.services;

import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.RegisterDto;
import com.twitter.demo.entities.dto.UserDto;
import com.twitter.demo.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(RegisterDto userInfo) {

        if (userRepository.findUserByEmail(userInfo.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }
        User user = new User();
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        String rawPassword = userInfo.getPassword();
        String encoded = passwordEncoder.encode(rawPassword);
        user.setPassword(encoded);
        userRepository.save(user);
    }

    public User getUser(String id){
        UUID userId = UUID.fromString(id);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get();
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    public UserDto getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return new UserDto(optionalUser.get().getId(),
                optionalUser.get().getName(), optionalUser.get().getEmail());
    }
}
