package rs.ac.ni.pmf.rwa.estore.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.ac.ni.pmf.rwa.estore.TestData;
import rs.ac.ni.pmf.rwa.estore.mapper.UserMapper;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;

import java.util.List;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, userMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {


    }
}
