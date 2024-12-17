package backend.flutter.entity;

import backend.flutter.service.ModelService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

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
