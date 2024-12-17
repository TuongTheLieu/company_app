package backend.flutter.entity;


import backend.flutter.service.ModelService;
import backend.flutter.util.constant.LevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String updatedBy;
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skills",joinColumns = @JoinColumn(name = "job_id"),inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;



    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = ModelService.getCurrentUserLogin().isPresent() == true ?
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
