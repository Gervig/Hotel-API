package app.populators;

import app.security.daos.RoleDAO;
import app.security.daos.UserDAO;
import app.security.entities.Role;
import app.security.entities.User;
import jakarta.persistence.EntityManagerFactory;

public class UserPopulator
{
    public static void populate(EntityManagerFactory emf)
    {
        UserDAO userDAO = UserDAO.getInstance(emf);
        RoleDAO roleDAO = RoleDAO.getInstance(emf);
        User user = new User("Holger","Password123");
        Role admin = new Role("admin");
        roleDAO.createRole(admin);
        user.addRole(admin);
        userDAO.create(user);
    }
}
