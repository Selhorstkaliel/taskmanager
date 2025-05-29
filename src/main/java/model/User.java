package model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String name;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isAdmin;

    public boolean isValid() {
        return name != null && !name.isBlank()
            && username != null && !username.isBlank()
            && email != null && email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")
            && passwordHash != null && !passwordHash.isBlank();
    }
}
