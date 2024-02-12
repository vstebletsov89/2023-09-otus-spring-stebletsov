package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка контроля доступа для actuator")
@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("должен загружать actuator endpoints для ADMIN")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @ParameterizedTest
    @ValueSource(strings = {
            "/actuator",
            "/actuator/logfile",
            "/actuator/health",
            "/actuator/flyway",
            "/actuator/metrics"})
    void shouldHaveAccessToActuator(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("должен запрещать actuator endpoints для USER")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @ParameterizedTest
    @ValueSource(strings = {
            "/actuator",
            "/actuator/logfile",
            "/actuator/health",
            "/actuator/flyway",
            "/actuator/metrics"})
    void shouldDenyAccessToActuator(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
