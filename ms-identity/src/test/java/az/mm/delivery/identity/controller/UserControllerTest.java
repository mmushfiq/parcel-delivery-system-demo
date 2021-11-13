package az.mm.delivery.identity.controller;

import az.mm.delivery.identity.annotations.EnabledIfRedisReachable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnabledIfRedisReachable(host = "localhost", port = 6379)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "mushfiqazeri@gmail.com", authorities = "view_user_list")
    void getAllUsers_whenPermissionHas_thenReturnUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hasanov.ceyhun@gmail.com")
    void getAllUsers_whenPermissionHasNot_thenThrowAccessDeniedException() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(403)))
                .andExpect(jsonPath("$.message", is("Access is denied")));
    }

    @Test
    void getAllUsers_whenUnauthorizedUser_thenThrowAuthenticationException() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.message", is("Full authentication is required to access this resource")));
    }

    @Test
    @WithMockUser(username = "mushfiqazeri@gmail.com", authorities = "view_user")
    void getUser_whenCorrectUserId_thenReturnAppropriateUser() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("mushfiqazeri@gmail.com")));
    }

}