package app.security.daos;

import app.config.HibernateConfig;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import app.security.entities.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserDAO
{
    private static EntityManagerFactory emf;
    private static UserDAO instance;

    // singleton **
    public UserDAO()
    {
    }

    public static UserDAO getInstance(EntityManagerFactory _emf)
    {
        if(emf == null)
        {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }
    // ** singleton


    public User create(User user){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public User read(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            User user = em.find(User.class, id);
            if (user == null)
            {
                throw new NullPointerException();
            }
            return user;
        } catch (Exception e)
        {
            throw new ApiException(404, "Error user not found", e);
        }
    }

    public List<User> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error finding users", e);
        }
    }

    public User update(User user)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating user", e);
        }
    }

    public void delete(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                User user = em.find(User.class, id);
                if (user == null)
                {
                    em.getTransaction().rollback();
                    throw new NullPointerException();
                }
                em.remove(user);
                em.getTransaction().commit();
            } catch (Exception e)
            {
                throw new ApiException(401, "Error deleting User", e);
            }
        }
    }

}
