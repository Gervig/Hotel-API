package app.security.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User implements ISecurityUser
{
    //Username og password er bare minimum når man skal kunne oprette sig

    @Id
    String username;
    String password;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    public User(String username, String password){
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User(){}

    @Override
    public Set<String> getRolesAsStrings()
    {
        return Set.of();
    }

    @Override
    public boolean verifyPassword(String pw)
    {
        return BCrypt.checkpw(pw, this.password);
    }

    @Override
    public void addRole(Role role)
    {
        roles.add(role);
        role.users.add(this);
    }

    @Override
    public void removeRole(String role)
    {
        //roles.removeIf(roleEntity->roleEntity.name.equals(role));
        for(Role roleEntity : roles){
            if(roleEntity.name.equals(role)){
                roles.remove(roleEntity);
                roleEntity.users.remove(this);
            }
        }
    }
}
