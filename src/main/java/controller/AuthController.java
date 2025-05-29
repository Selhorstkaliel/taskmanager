package controller;

import model.User;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService = new AuthService();

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");

        User user = User.builder()
                .name(name)
                .username(username)
                .email(email)
                .isAdmin(false)
                .build();

        boolean success = authService.register(user, password);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        String login = payload.get("login");
        String password = payload.get("password");

        User user = authService.login(login, password);
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("username", user.getUsername());
            response.put("isAdmin", user.isAdmin());
        } else {
            response.put("success", false);
        }
        return response;
    }
}