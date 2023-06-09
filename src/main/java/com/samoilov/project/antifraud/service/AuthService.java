package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.mapper.UserCredentialsMapper;
import com.samoilov.project.antifraud.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserCredentialsMapper userCredentialsMapper;

    public AuthService(UserRepository userRepository, UserCredentialsMapper userCredentialsMapper) {
        this.userRepository = userRepository;
        this.userCredentialsMapper = userCredentialsMapper;
    }

    public List<UserDto> getAllUsers() {
        return userRepository
                .getAll()
                .stream()
                .map(userCredentialsMapper::mapToDto)
                .toList();
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return userCredentialsMapper.mapToDto(
                userRepository.save(userCredentialsMapper.mapToEntity(userDto))
        );
    }

    public Map<String, String> deleteUser(String username) {
        userRepository
                .delete(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        return Map.of("username", username, "status", "Deleted successfully!");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
