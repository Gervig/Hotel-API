package app.populators;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import app.daos.impl.HotelDAO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;

public class HotelPopulator
{
    public static void populate(EntityManagerFactory emf)
    {
        HotelDAO hotelDAO = HotelDAO.getInstance(emf);

        // Create 3 Hotels with 5 Rooms each
        List<Hotel> hotels = IntStream.rangeClosed(1, 3)
                .mapToObj(i ->
                {
                    Hotel hotel = Hotel.builder()
                            .name("Hotel " + i)
                            .address("Address " + i)
                            .build();

                    List<Room> rooms = createRooms(hotel, 5);
                    hotel.setRooms(rooms);

                    return hotel;
                })
                .toList();

        // Persist hotels (rooms will be saved automatically due to CascadeType.ALL)
        hotels.forEach(hotelDAO::create);
    }

    private static List<Room> createRooms(Hotel hotel, int roomCount)
    {
        return IntStream.rangeClosed(1, roomCount)
                .mapToObj(i -> Room.builder()
                        .number(i)
                        .price(BigDecimal.valueOf(100 + (i * 20.0))) // Different prices
                        .hotel(hotel) // Assign the hotel
                        .build())
                .toList();
    }
}
