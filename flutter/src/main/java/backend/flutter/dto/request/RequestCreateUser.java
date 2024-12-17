package backend.flutter.dto.request;

import lombok.Data;

@Data
public class RequestCreateUser {
    private String username;
    private String password;
    private String email;
}
