package backend.flutter.entity;

import backend.flutter.service.ModelService;
import backend.flutter.util.constant.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Tên không được để trống")
    private String username;
    @NotBlank(message = "Password không được để trống")
    private String password;
    @NotBlank(message = "Email không được để trống")
    private String email;

    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private Instant createAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @PrePersist
    public void handleBeforeCreate() {
        createdBy = ModelService.getCurrentUserLogin().isPresent() == true ?
                ModelService.getCurrentUserLogin().get() : "";
        this.createAt = Instant.now();

    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = ModelService.getCurrentUserLogin().isPresent() == true ?
                ModelService.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }

}
