package service;

import model.User;
import persistence.JsonStorageService;
import util.PasswordUtils;

import java.util.List;
import java.util.Optional;

public class AuthService {
    private final List<User> users;

    public AuthService() {
        users = JsonStorageService.loadUsers();
        ensureAdminExists();
    }

    // Cria o admin se não existir
    private void ensureAdminExists() {
        Optional<User> admin = users.stream()
                .filter(u -> u.getUsername().equals("Admin"))
                .findFirst();
        if (admin.isEmpty()) {
            User adminUser = User.builder()
                    .name("Administrador")
                    .username("Admin")
                    .email("admin@gmail.com")
                    .passwordHash(PasswordUtils.hashPassword("Admin@12345"))
                    .isAdmin(true)
                    .build();
            users.add(adminUser);
            JsonStorageService.saveUsers(users);
        }
    }

    public boolean register(User newUser, String plainPassword) {
        if (!newUser.isValid()) return false;
        boolean exists = users.stream().anyMatch(u ->
            u.getUsername().equalsIgnoreCase(newUser.getUsername()) ||
            u.getEmail().equalsIgnoreCase(newUser.getEmail())
        );
        if (exists) return false;

        newUser.setPasswordHash(PasswordUtils.hashPassword(plainPassword));
        users.add(newUser);
        JsonStorageService.saveUsers(users);
        return true;
    }

    public User login(String login, String password) {
        return users.stream()
                .filter(u -> (u.getUsername().equalsIgnoreCase(login) || u.getEmail().equalsIgnoreCase(login)) &&
                        PasswordUtils.verifyPassword(password, u.getPasswordHash()))
                .findFirst()
                .orElse(null);
    }
}
