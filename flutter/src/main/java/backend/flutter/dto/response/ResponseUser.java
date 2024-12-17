package backend.flutter.dto.response;

import backend.flutter.entity.Company;
import backend.flutter.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUser {
    private long id;
    private String username;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    @JsonProperty("create-at")
    private Instant createdAt;
    @JsonProperty("update-at")
    private Instant updatedAt;

    private CompanyUser company;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
