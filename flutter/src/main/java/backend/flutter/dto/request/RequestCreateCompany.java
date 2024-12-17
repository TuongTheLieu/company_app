package backend.flutter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateCompany {

    @NotBlank(message = "ten khong duoc de trong")
    private String name;
    private String description;
    private String address;
    private String logo;

}
