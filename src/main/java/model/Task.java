package model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private String id;
    private String user; // username do dono da task
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status; // pendente, concluída
    private String priority; // baixa, média, alta

    public boolean isValid() {
        return title != null && !title.isBlank()
            && dueDate != null
            && (status.equals("pendente") || status.equals("concluída"))
            && (priority.equals("baixa") || priority.equals("média") || priority.equals("alta"));
    }
}