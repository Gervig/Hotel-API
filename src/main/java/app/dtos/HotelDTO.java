package app.dtos;

import app.entities.Hotel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO
{
    private int id;
    private String name;
    private String address;
    private List<RoomDTO> rooms;

    public HotelDTO(Hotel hotel)
    {
        if(hotel.getId()!=null)
        {
            this.id = hotel.getId();
        }
        this.name = hotel.getName();
        this.address = hotel.getAddress();

        this.rooms = hotel.getRooms()
                .stream()
                .map(RoomDTO::new)
                .collect(Collectors.toList());
    }
}
