package app.entities;

import app.dtos.HotelDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;

import java.util.List;
import java.util.stream.Collectors;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
public class Hotel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Room> rooms;

    public Hotel(HotelDTO hotelDTO)
    {
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();

        this.rooms = hotelDTO.getRooms().stream()
                .map(Room::new)
                .collect(Collectors.toList());
        this.rooms.forEach(room -> room.setHotel(this));
    }

    public void addRoom(Room room)
    {
        this.rooms.add(room);
    }

    public void removeRoom(Room room)
    {
        this.rooms.remove(room);
    }
}
