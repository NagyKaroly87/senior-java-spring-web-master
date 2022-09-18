package hu.ponte.hr.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cu")
    private String createUser;

    @Column(name = "mu")
    private String modifyUser;

    @Column(name = "cd")
    private Instant createDate;

    @Column(name = "md")
    private Instant modifyDate;

    @PrePersist
    public void setCuCdPrePersist() {
        setCreateDate(Instant.now());
    }

    @PreUpdate
    public void setMuCdPreUpdate() {
        setModifyDate(Instant.now());
    }

}
