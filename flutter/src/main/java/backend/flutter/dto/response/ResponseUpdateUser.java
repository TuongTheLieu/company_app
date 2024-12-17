package backend.flutter.dto.response;

import backend.flutter.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResponseUpdateUser {
    private int id;
    private String username;
    private String age;
    private GenderEnum gender;
    private String address;
    @JsonProperty("update_at")
    private Instant updatedAt;
    private CompanyUser company;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
