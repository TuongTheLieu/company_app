package backend.flutter.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestLogin {
    @NotBlank(message = "Email không được để trống")
    private String username;
    @NotBlank(message = "Password không được để trống")
    private String password;
}
