package rs.ac.ni.pmf.rwa.estore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import rs.ac.ni.pmf.rwa.estore.repository.StoreRepository;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser(roles = "Admin")
class StoreIntegrationTest {

    private static final String BASE_URL = "/api/v1/stores";

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanStores() {
        storeRepository.deleteAll();
    }

    private Long categoryId() {
        var ids = jdbcTemplate.queryForList("SELECT id FROM category LIMIT 1", Long.class);
        if (!ids.isEmpty()) {
            return ids.get(0);
        }
        jdbcTemplate.update("INSERT INTO category (name) VALUES (?)", "Test Category");
        return jdbcTemplate.queryForObject("SELECT id FROM category LIMIT 1", Long.class);
    }

    private Long managerId() {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> userRepository.save(
                        UserEntity.builder()
                                .username("test_manager")
                                .email("manager@test.rs")
                                .password("password123")
                                .firstName("Test")
                                .lastName("Manager")
                                .phone("018000000")
                                .address("Test Address 1")
                                .build()))
                .getId();
    }

    private Map<String, Object> storeBody(String name, String address, String phone, boolean active) {
        return storeBody(name, address, phone, active, categoryId(), managerId());
    }

    private Map<String, Object> storeBody(String name, String address, String phone, boolean active,
                                          Long categoryId, Long managerId) {
        return Map.of(
                "name", name,
                "address", address,
                "phone", phone,
                "isActive", active,
                "categoryId", categoryId,
                "managerId", managerId);
    }

    private Map<String, Object> updateBody(String name, String address, String phone, boolean active) {
        return storeBody(name, address, phone, active);
    }

    private long createStore(String name, String address, String phone, boolean active) throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeBody(name, address, phone, active))))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    @DisplayName("POST /stores - kreira prodavnicu i cuva je u bazi")
    void createStore_persistsInDatabase() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                storeBody("Tech Shop", "Nikole Pasica 1, Nis", "018123456", true))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Tech Shop"))
                .andExpect(jsonPath("$.address").value("Nikole Pasica 1, Nis"))
                .andExpect(jsonPath("$.phone").value("018123456"))
                .andExpect(jsonPath("$.isActive").value(true));

        assertThat(storeRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("POST /stores - 404 kada menadzer ne postoji")
    void createStore_unknownManager_returnsNotFound() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                storeBody("Ghost Shop", "Nowhere 1", "000", true, categoryId(), 999_999L))))
                .andExpect(status().isNotFound());

        assertThat(storeRepository.count()).isZero();
    }

    @Test
    @DisplayName("GET /stores/{id} - vraca postojecu prodavnicu")
    void getStoreById_returnsStore() throws Exception {
        long id = createStore("Book Store", "Obrenoviceva 10", "018111222", true);

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Book Store"))
                .andExpect(jsonPath("$.address").value("Obrenoviceva 10"));
    }

    @Test
    @DisplayName("GET /stores/{id} - 404 za nepostojeci id")
    void getStoreById_notFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /stores - paginirana lista, sortirana po imenu")
    void getAllStores_returnsPageSortedByName() throws Exception {
        createStore("B Store", "Adresa B", "222", true);
        createStore("A Store", "Adresa A", "111", true);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("A Store"))
                .andExpect(jsonPath("$.content[1].name").value("B Store"));
    }

    @Test
    @DisplayName("GET /stores/active - vraca samo aktivne prodavnice")
    void getActiveStores_returnsOnlyActive() throws Exception {
        createStore("Active Shop", "Adresa 1", "111", true);
        createStore("Inactive Shop", "Adresa 2", "222", false);

        mockMvc.perform(get(BASE_URL + "/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Active Shop"));
    }

    @Test
    @DisplayName("PUT /stores/{id} - azurira prodavnicu u bazi")
    void updateStore_updatesDatabase() throws Exception {
        long id = createStore("Old Name", "Old Address", "111", true);

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                updateBody("New Name", "New Address", "999", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.address").value("New Address"))
                .andExpect(jsonPath("$.phone").value("999"))
                .andExpect(jsonPath("$.isActive").value(false));

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    @DisplayName("PUT /stores/{id} - 404 za nepostojeci id")
    void updateStore_notFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}", 999_999L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody("X", "Y", "0", true))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /stores/{id}/active - menja status aktivnosti")
    void setActiveStatus_updatesFlag() throws Exception {
        long id = createStore("Toggle Shop", "Adresa", "111", true);

        mockMvc.perform(put(BASE_URL + "/{id}/active", id)
                        .with(csrf())
                        .param("active", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false));

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    @DisplayName("PUT /stores/{id}/active - 404 za nepostojeci id")
    void setActiveStatus_notFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}/active", 999_999L)
                        .with(csrf())
                        .param("active", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /stores/{id} - brise prodavnicu iz baze")
    void deleteStore_removesFromDatabase() throws Exception {
        long id = createStore("To Delete", "Some Address", "111", true);

        mockMvc.perform(delete(BASE_URL + "/{id}", id).with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(storeRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("DELETE /stores/{id} - 404 za nepostojeci id")
    void deleteStore_notFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 999_999L).with(csrf()))
                .andExpect(status().isNotFound());
    }
}
