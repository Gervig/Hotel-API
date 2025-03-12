package app.security.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="roles")
public class Role {
    @Id
    String name;
    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    Set<User> users = new HashSet<>();

    public Role(String name){
        this.name = name;
    }
    public Role(){}
}
