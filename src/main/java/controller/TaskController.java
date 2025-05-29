package controller;

import model.Task;
import org.springframework.web.bind.annotation.*;
import service.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService taskService = new TaskService();

    @GetMapping
    public List<Task> getTasks(
            @RequestParam String username,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String q
    ) {
        LocalDateTime fromDate = parseDate(from);
        LocalDateTime toDate = parseDate(to);
        return taskService.filterTasks(username, status, fromDate, toDate, q);
    }

    @PostMapping
    public Map<String, Object> addTask(@RequestParam String username, @RequestBody Task task) {
        boolean result = taskService.addTask(username, task);
        return Map.of("success", result);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateTask(@RequestParam String username, @PathVariable String id, @RequestBody Task task) {
        task.setId(id);
        boolean result = taskService.updateTask(username, task);
        return Map.of("success", result);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTask(@RequestParam String username, @PathVariable String id) {
        boolean result = taskService.deleteTask(username, id);
        return Map.of("success", result);
    }

    @PatchMapping("/{id}/complete")
    public Map<String, Object> completeTask(@RequestParam String username, @PathVariable String id) {
        boolean result = taskService.markCompleted(username, id);
        return Map.of("success", result);
    }

    @GetMapping("/urgent")
    public List<Task> urgentTasks(@RequestParam String username) {
        return taskService.getUrgentTasks(username);
    }

    @PostMapping("/import")
    public List<Task> importTasks(@RequestParam String username, @RequestBody List<Task> imported) {
        return taskService.importTasks(username, imported);
    }

    @GetMapping("/export")
    public List<Task> exportTasks(@RequestParam String username) {
        return taskService.exportTasks(username);
    }

    private LocalDateTime parseDate(String s) {
        try {
            return s == null ? null : LocalDateTime.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}