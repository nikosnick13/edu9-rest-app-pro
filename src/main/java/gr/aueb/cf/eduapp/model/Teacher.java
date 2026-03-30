package gr.aueb.cf.eduapp.model;


import gr.aueb.cf.eduapp.model.static_data.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "teachers")
public class Teacher extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false,updatable = false,columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(unique = true)
    private String vat;

    private String firstname;

    private String lastname;

    @PrePersist
    public void initialUUID(){
        this.uuid = UUID.randomUUID();
    };

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="region_id")
    private Region region;


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id",nullable = false, unique = true)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(uuid, teacher.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
