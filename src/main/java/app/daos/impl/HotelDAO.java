package app.daos.impl;

import app.daos.IDAO;
import app.entities.Hotel;
import app.entities.Room;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HotelDAO implements IDAO<Hotel, Integer>
{
    //TODO use a generic DAO for basic CRUD instead of multiple DAOs
    private static EntityManagerFactory emf;
    private static HotelDAO instance;

    // singleton **
    public HotelDAO()
    {
    }

    public static HotelDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new HotelDAO();
            emf = _emf;
        }
        return instance;
    }
    // ** singleton

    @Override
    public Hotel create(Hotel hotel)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                em.persist(hotel);
                em.getTransaction().commit();
                return hotel;
            } catch (Exception e)
            {
                em.getTransaction().rollback();
                throw new ApiException(400, "Error creating Hotel", e);
            }
        }
    }


    @Override
    public Hotel read(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Hotel hotel = em.find(Hotel.class, id);
            if (hotel == null)
            {
                throw new NullPointerException();
            }
            return hotel;
        } catch (Exception e)
        {
            throw new ApiException(404, "Error Hotel not found", e);
        }
    }

    @Override
    public List<Hotel> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT h FROM Hotel h ORDER BY h.id", Hotel.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding Hotels", e);
        }
    }

    @Override
    public Hotel update(Hotel hotel)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Hotel updatedHotel = em.merge(hotel);
            em.getTransaction().commit();
            return updatedHotel;
        } catch (Exception e)
        {
            throw new ApiException(400, "Error updating hotel", e);
        }
    }

    @Override
    public void delete(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                Hotel hotel = em.find(Hotel.class, id);
                if (hotel == null)
                {
                    em.getTransaction().rollback();
                    throw new NullPointerException();
                }
                em.remove(hotel);
                em.getTransaction().commit();
            } catch (Exception e)
            {
                throw new ApiException(400, "Error deleting Hotel", e);
            }
        }
    }
}
