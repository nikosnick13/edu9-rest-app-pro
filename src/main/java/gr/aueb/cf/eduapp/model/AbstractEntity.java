package gr.aueb.cf.eduapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.Instant;

@MappedSuperclass //Δήλωση ότι είναι μόνο super class και ότι μόνο κληρονομήτε και δεν φτιαχνeu  table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AbstractEntity.class)

public abstract class AbstractEntity {

    @CreatedDate //  Δήλωση ημερομηνίας μετά το sava του Entity
    @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant createAt;  // UTC

    @LastModifiedDate //Update του Entity ημερομηνίας
    @Column(name = "update_at", nullable = false,columnDefinition = "DATETIME")
    private Instant updateAt;

    @Column(nullable = false)
    private  boolean deleted;

    @Column(name = "delete_at",nullable = false,columnDefinition = "DATETIME")
    private Instant deleteAt;

    public void softDelete(){
        this.deleted = true;
        this.deleteAt = Instant.now();
    }
}
