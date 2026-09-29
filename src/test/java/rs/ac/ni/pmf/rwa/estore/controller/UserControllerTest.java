package rs.ac.ni.pmf.rwa.estore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rs.ac.ni.pmf.rwa.estore.mapper.UserMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.UserResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CacheManager cacheManager;

    private UserResponse createUser(Long id, String firstname, String email) {
        UserResponse user = UserResponse.builder().id(id).firstName(firstname).email(email).build();

        return user;
    }

    private static final String USER_JSON = """
            {
                "firstname": "Jovan",
                "lastname": "Jovanović",
                "email": "Jovan@example.com",
                "address": "Adresa 1",
                "phone": "0641234567",
                "password": "tajna123"
            }
            """;

    @Test
    void getUserById_shouldReturnUser_whenExists() throws Exception {

        when(userService.getUserById(1L)).thenReturn((createUser(1L, "Jovan", "jovan@example.com")));
        //when(userService.getUserById(1L)).thenReturn(Optional.of(TestData.USERS.ADMIN));

        mockMvc.perform(get("/api/v1/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jovan"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}

/*
    @Test
    void shouldReturnAllUsers() throws Exception
    {
        when(userService.getAllUsers()).thenReturn(List.of(
                TestData.USERS.USER_ADMIN,
                TestData.USERS.USER_USER
        ));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value(TestData.USERS.USER_ADMIN.getUsername()))
                .andExpect(jsonPath("$[1].username").value(TestData.USERS.USER_USER.getUsername()));
    }

    private org.mockito.stubbing.OngoingStubbing<List<UserDto>> when(List<UserResponse> allUsers) {
        return null;
    }

    @Test
    void shouldReturnUserById() throws Exception
    {
        when(userService.getUserById(1L)).thenReturn(Optional.of(TestData.USERS.USER_ADMIN));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TestData.USERS.USER_ADMIN.getUsername()));

        verify(userService).getUserById(1L);
    }

}*/
