package app.controllers.impl;

import app.controllers.IHotel;
import app.daos.impl.HotelDAO;
import app.daos.impl.RoomDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HotelController implements IHotel<HotelDTO, RoomDTO, Integer>
{
    private static EntityManagerFactory emf;
    private HotelDAO hotelDAO;

    // singleton
    public HotelController(EntityManagerFactory _emf)
    {
        if (emf == null)
        {
            emf = _emf;
        }
        this.hotelDAO = HotelDAO.getInstance(emf);
    }

    @Override
    public List<HotelDTO> getAllHotels()
    {
        List<Hotel> hotels = hotelDAO.readAll();

        List<HotelDTO> hotelDTOS = hotels.stream()
                .map(HotelDTO::new)
                .toList();

        return hotelDTOS;
    }

    @Override
    public HotelDTO getHotelById(Integer id)
    {
        Hotel hotel = hotelDAO.read(id);
        if (hotel == null)
        {
            return null;
        }
        HotelDTO hotelDTO = new HotelDTO(hotel);
        return hotelDTO;
    }

    @Override
    public HotelDTO createHotel(HotelDTO hotelDTO)
    {
        Hotel hotel = new Hotel(hotelDTO);
        Hotel createdHotel = hotelDAO.create(hotel);
        return new HotelDTO(createdHotel);
    }


    @Override
    public HotelDTO updateHotel(HotelDTO hotelDTO)
    {
        Hotel hotel = new Hotel(hotelDTO);
        hotel = hotelDAO.update(hotel);
        return new HotelDTO(hotel);
    }

    @Override
    public void deleteHotel(Integer id)
    {
        hotelDAO.delete(id);
    }

    @Override
    public void addRoom(HotelDTO hotelDTO, RoomDTO roomDTO)
    {
        Hotel hotel = new Hotel(hotelDTO);
        Room room = new Room(roomDTO);
        hotel.addRoom(room);
    }

    @Override
    public void removeRoom(HotelDTO hotelDTO, RoomDTO roomDTO)
    {
        Hotel hotel = new Hotel(hotelDTO);
        Room room = new Room(roomDTO);
        hotel.removeRoom(room);
    }

    @Override
    public List<RoomDTO> getRoomsForHotel(HotelDTO hotelDTO)
    {
        List<RoomDTO> roomDTOS = hotelDTO.getRooms();
        return roomDTOS;
    }
}
