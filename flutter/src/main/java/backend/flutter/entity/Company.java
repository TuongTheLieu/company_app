package backend.flutter.entity;

import backend.flutter.service.ModelService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank (message = " name không được để trống")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;
    private String logo;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;
    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    @JsonIgnore
    List<Job> jobs;

    @PrePersist
    public void handleBeforeCreate() {
        createdBy = ModelService.getCurrentUserLogin().isPresent() == true ?
                ModelService.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();

    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = ModelService.getCurrentUserLogin().isPresent() == true ?
                ModelService.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }

}
