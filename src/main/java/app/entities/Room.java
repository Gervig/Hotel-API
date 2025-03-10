package app.entities;

import app.dtos.RoomDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int number;
    private BigDecimal price;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    private Hotel hotel;

    public Room(RoomDTO roomDTO)
    {
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();

        this.hotel = Hotel.builder()
                .id(roomDTO.getHotelId())
                .build();

    }
}
