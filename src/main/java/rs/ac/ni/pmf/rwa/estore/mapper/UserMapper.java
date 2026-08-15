package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.UserRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.UserResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

@Component
public class UserMapper {

    public UserResponse toResponse(final UserEntity userEntity) {

        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhone())
                .build();
    }

    public UserEntity toEntity(final UserRequest userRequest) {

        return UserEntity.builder()
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                //.password(userRequest.getPassword())
                .build();

    }
}
