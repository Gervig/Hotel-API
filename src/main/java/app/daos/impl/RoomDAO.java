package app.daos.impl;

import app.daos.IDAO;
import app.entities.Hotel;
import app.entities.Room;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class RoomDAO implements IDAO<Room, Integer>
{
    //TODO use a generic DAO for basic CRUD instead of multiple DAOs
    private static EntityManagerFactory emf;
    private static RoomDAO instance;

    // singleton **
    public RoomDAO()
    {
    }

    public static RoomDAO getInstance(EntityManagerFactory _emf)
    {
        if(emf == null)
        {
            emf = _emf;
            instance = new RoomDAO();
        }
        return instance;
    }
    // ** singleton

    @Override
    public Room create(Room room) {
        if (room.getHotel() == null) {
            throw new ApiException(400, "Room must be associated with a Hotel before persisting.");
        }

        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.persist(room);
                em.getTransaction().commit();
                return room;
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw new ApiException(401, "Error creating Room", e);
            }
        }
    }

    @Override
    public Room read(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Room room = em.find(Room.class, id);
            if (room == null)
            {
                throw new NullPointerException();
            }
            return room;
        } catch (Exception e)
        {
            throw new ApiException(404, "Error Room not found", e);
        }
    }

    @Override
    public List<Room> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT r FROM Room r ORDER BY r.id", Room.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error finding Rooms", e);
        }
    }

    @Override
    public Room update(Room room)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Room updatedRoom = em.merge(room);
            em.getTransaction().commit();
            return updatedRoom;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating room", e);
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
                Room room = em.find(Room.class, id);
                if (room == null)
                {
                    em.getTransaction().rollback();
                    throw new NullPointerException();
                }
                em.remove(room);
                em.getTransaction().commit();
            } catch (Exception e)
            {
                throw new ApiException(401, "Error deleting Room", e);
            }
        }
    }
}
