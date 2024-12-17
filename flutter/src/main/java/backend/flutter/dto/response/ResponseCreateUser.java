package backend.flutter.dto.response;

import backend.flutter.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResponseCreateUser {
    private long id;
    private String username;
    private String email;
    private GenderEnum gender;
    private int age;
    private String address;
    @JsonProperty("create_at")
    private Instant createdAt;
    private CompanyUser company;

    @Setter
    @Getter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
