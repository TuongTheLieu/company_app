package backend.flutter.dto.request;


import lombok.Data;

@Data
public class RequestUpdateUser {
    private String username;
    private String password;
}
