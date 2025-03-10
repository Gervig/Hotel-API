package app.controllers;

import java.util.List;

public interface IHotel<T, U, I>
{
    List<T> getAllHotels();
    T getHotelById(I id);
    T createHotel(T hotel);
    T updateHotel(T hotel);
    void deleteHotel(I id);
    void addRoom(T hotel, U room);
    void removeRoom(T hotel, U room);
    List<U> getRoomsForHotel(T hotel);
}
