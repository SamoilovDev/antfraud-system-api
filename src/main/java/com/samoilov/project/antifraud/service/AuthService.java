package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.ChangeInfoDto;
import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.entity.UserEntity;
import com.samoilov.project.antifraud.enums.Authority;
import com.samoilov.project.antifraud.enums.LockState;
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

    public static final String CHANGING_ADMIN_STATE_EXCEPTION_REASON = "Impossible to change administrator's lock state";

    public AuthService(UserRepository userRepository, UserCredentialsMapper userCredentialsMapper) {
        this.userRepository = userRepository;
        this.userCredentialsMapper = userCredentialsMapper;
    }

    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userCredentialsMapper::mapToDto)
                .toList();
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        userDto.setRole(userRepository.findById(1L).isEmpty() ? Authority.ADMINISTRATOR : Authority.MERCHANT);
        userDto.setLockState(userDto.getRole().equals(Authority.ADMINISTRATOR) ? LockState.UNLOCK : LockState.LOCK);

        return userCredentialsMapper.mapToDto(
                userRepository.save(userCredentialsMapper.mapToEntity(userDto))
        );
    }

    public UserDto changeRole(ChangeInfoDto changeInfoDto) {
        UserEntity userEntity = this.getUserEntityByUsername(changeInfoDto.getUsername());
        Authority newRole = Authority
                .parse(changeInfoDto.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role"));

        if (newRole.equals(userEntity.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has this role");
        }

        userEntity.setRole(newRole);

        return userCredentialsMapper.mapToDto(userRepository.save(userEntity));
    }

    public Map<String, String> changeAccess(ChangeInfoDto changeInfoDto) {
        UserEntity userEntity = this.getUserEntityByUsername(changeInfoDto.getUsername());

        if (userEntity.getRole().equals(Authority.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CHANGING_ADMIN_STATE_EXCEPTION_REASON);
        }

        userEntity.setLockState(LockState.valueOf(changeInfoDto.getRole().trim().toUpperCase()));
        userRepository.save(userEntity);

        return Map.of(
                "status",
                "User %s %s!".formatted(userEntity.getUsername(), userEntity.getLockState().getLockState())
        );
    }

    public Map<String, String> deleteUser(String username) {
        userRepository.delete(this.getUserEntityByUsername(username));
        return Map.of("username", username, "status", "Deleted successfully!");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserEntityByUsername(username);
    }

    private UserEntity getUserEntityByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
