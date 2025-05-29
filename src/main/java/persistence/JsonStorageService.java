package persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Task;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonStorageService {
    private static final String USER_FILE = "data/users.json";
    private static final String TASK_FILE_PREFIX = "data/tasks-";
    private static final ObjectMapper mapper = new ObjectMapper();

    // Carrega todos os usuários
    public static List<User> loadUsers() {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Salva todos os usuários
    public static void saveUsers(List<User> users) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_FILE), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega tarefas de um usuário
    public static List<Task> loadTasks(String username) {
        try {
            File file = new File(TASK_FILE_PREFIX + username + ".json");
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Salva tarefas de um usuário
    public static void saveTasks(String username, List<Task> tasks) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASK_FILE_PREFIX + username + ".json"), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}