package rs.ac.ni.pmf.rwa.estore;

import rs.ac.ni.pmf.rwa.estore.model.dto.UserDto;

public class TestData {

    public static class USERS {

            public static UserDto USER_ADMIN = UserDto.builder()
                                    .id(1L)
                                    .username("admin")
                                    .firstName("admin")
                                    .lastName("admin")
                                    .build();

            public static UserDto USER_USER = UserDto.builder()
                                    .id(1L)
                                    .username("user")
                                    .firstName("user")
                                    .lastName("user")
                                    .build();
    }
}
