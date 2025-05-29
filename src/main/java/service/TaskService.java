package service;

import model.Task;
import persistence.JsonStorageService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {

    public List<Task> getAllTasks(String username) {
        List<Task> tasks = JsonStorageService.loadTasks(username);
        tasks.sort(Comparator.comparing(Task::getDueDate)); // ordenação automática
        return tasks;
    }

    public Task getTaskById(String username, String id) {
        return JsonStorageService.loadTasks(username).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean addTask(String username, Task task) {
        if (!task.isValid()) return false;
        task.setId(UUID.randomUUID().toString());
        task.setUser(username);

        List<Task> tasks = JsonStorageService.loadTasks(username);
        tasks.add(task);
        JsonStorageService.saveTasks(username, tasks);
        return true;
    }

    public boolean updateTask(String username, Task updated) {
        List<Task> tasks = JsonStorageService.loadTasks(username);
        Optional<Task> existing = tasks.stream()
                .filter(t -> t.getId().equals(updated.getId()))
                .findFirst();

        if (existing.isEmpty()) return false;
        updated.setUser(username);

        tasks.remove(existing.get());
        tasks.add(updated);
        JsonStorageService.saveTasks(username, tasks);
        return true;
    }

    public boolean deleteTask(String username, String id) {
        List<Task> tasks = JsonStorageService.loadTasks(username);
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) JsonStorageService.saveTasks(username, tasks);
        return removed;
    }

    public boolean markCompleted(String username, String id) {
        List<Task> tasks = JsonStorageService.loadTasks(username);
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setStatus("concluída");
                JsonStorageService.saveTasks(username, tasks);
                return true;
            }
        }
        return false;
    }

    public List<Task> filterTasks(String username, String status, LocalDateTime from, LocalDateTime to, String query) {
        return JsonStorageService.loadTasks(username).stream()
                .filter(t -> status == null || t.getStatus().equalsIgnoreCase(status))
                .filter(t -> from == null || !t.getDueDate().isBefore(from))
                .filter(t -> to == null || !t.getDueDate().isAfter(to))
                .filter(t -> query == null || t.getTitle().toLowerCase().contains(query.toLowerCase())
                        || t.getDescription().toLowerCase().contains(query.toLowerCase()))
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Task> getUrgentTasks(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(24);
        return JsonStorageService.loadTasks(username).stream()
                .filter(t -> t.getDueDate().isBefore(limit) && t.getStatus().equals("pendente"))
                .collect(Collectors.toList());
    }

    public List<Task> importTasks(String username, List<Task> importedTasks) {
        List<Task> existing = JsonStorageService.loadTasks(username);
        Set<String> existingIds = existing.stream().map(Task::getId).collect(Collectors.toSet());

        for (Task task : importedTasks) {
            if (!existingIds.contains(task.getId())) {
                task.setUser(username);
                existing.add(task);
            }
        }

        JsonStorageService.saveTasks(username, existing);
        return existing;
    }

    public List<Task> exportTasks(String username) {
        return JsonStorageService.loadTasks(username);
    }
}