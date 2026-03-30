package gr.aueb.cf.eduapp.model.static_data;

import gr.aueb.cf.eduapp.model.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.ast.tree.expression.Collation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "region",fetch = FetchType.LAZY)
    HashSet<Teacher> teachers = new HashSet<>();

    public Set<Teacher> getAllTeachers(){return Collections.unmodifiableSet(teachers);}

    public void addTeacher(Teacher teacher){
        if(teachers == null) teachers = new HashSet<>();

        teachers.add(teacher);
        teacher.setRegion(this);
    }

    public void  removeTeacher(Teacher teacher){
        if(teachers == null) return;
        teachers.remove(teacher);
        teacher.setRegion(null);

    }

}
