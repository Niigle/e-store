package rs.ac.ni.pmf.rwa.estore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rs.ac.ni.pmf.rwa.estore.TestData;
import rs.ac.ni.pmf.rwa.estore.model.dto.UserDto;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.UserResponse;
import rs.ac.ni.pmf.rwa.estore.service.UserService;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

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
/*
    @Test
    void shouldReturnUserById() throws Exception
    {
        when(userService.getUserById(1L)).thenReturn(Optional.of(TestData.USERS.USER_ADMIN));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TestData.USERS.USER_ADMIN.getUsername()));

        verify(userService).getUserById(1L);
    }*/

}
