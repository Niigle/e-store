package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.UserMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.UserDto;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.UpdatePasswordRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.UserRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.UserResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserResponse createUser(UserRequest userRequest) {

        final UserEntity userEntity = userMapper.toEntity(userRequest);

        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userMapper.toResponse(userRepository.save(userEntity));
    }

    public UserResponse updateUser(Long id, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik sa id " + id + " nije pr onađen"));


        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setPhone(userDto.getPhone());

        return userMapper.toResponse(userRepository.save(userEntity));
    }

    public void changePassword(Long id, UpdatePasswordRequest updatePasswordRequest) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik sa id " + id + " nije pronađen"));

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Stara lozinka nije ispravna");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik sa id " + id + " nije pronađen"));
        userRepository.delete(userEntity);
    }
}