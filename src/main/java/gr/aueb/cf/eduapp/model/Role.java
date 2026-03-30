package gr.aueb.cf.eduapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_capabilities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "capability_id"))
    private Set<Capability> capabilities = new HashSet<>();


    private Set<Capability> getAllCapabilities(){
        return Set.copyOf(capabilities);
    }

    private Set<User> getAllUsers(){
        return Set.copyOf(users);
    }

    private void addUser(User user){
        users.add(user);
        user.setRole(this);
    }

    private void removeUser(User user){
        users.remove(user);
        user.setRole(null);
    }

    private void addUsers(Collection<User> users){
        users.forEach(this::addUser);
    }

     private void addCapability(Capability capability){
        capabilities.add(capability);
        capability.getRoles().add(this);
     }

     private void removeCapability(Capability capability){
        capabilities.remove(capability);
        capability.getRoles().remove(this);
     }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return Objects.equals(getName(), role.getName());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
