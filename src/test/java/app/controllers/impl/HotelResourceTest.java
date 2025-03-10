package app.controllers.impl;

import app.config.HibernateConfig;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import app.populators.HotelPopulator;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class HotelResourceTest
{
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private String contentType = "application/json";

    @BeforeEach
    void setup()
    {
        HotelPopulator.populate(emf); // populates database, only necessary due to hibernate is set to "create". Remove this line when set to "update"
        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(Routes.getRoutes(emf))
                .startServer(7777);
        RestAssured.baseURI = "http://localhost:7777/api";
    }

    @AfterEach
    void tearDown()
    {
        ApplicationConfig.stopServer();
    }

    @Test
    @DisplayName("Test getting Hotel by ID")
    void HotelByIdTest()
    {
        given()
                .when()
                .get("/hotel/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @DisplayName("Test posting a new Hotel")
    void createHotelTest()
    {
        HotelDTO hotelDTO = getTestHotelDTO();

        try
        {
            String json = objectMapper.writeValueAsString(hotelDTO);
            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .post("/hotel")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Hotel test"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test updating an existing Hotel")
    void updateHotelTest()
    {
        HotelDTO hotelDTO = getTestHotelDTO();

        try
        {
            String json = objectMapper.writeValueAsString(hotelDTO);
            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .put("hotel/1")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Hotel test"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test deleting an existing Hotel")
    void deleteHotelTest()
    {
        when()
                .delete("/hotel/1")
                .then()
                .statusCode(204);
    }

    // Helper method for creating test DTOs
    private HotelDTO getTestHotelDTO()
    {

        Room r1 = Room.builder()
                .price(BigDecimal.valueOf(25.00))
                .number(1)
                .build();
        Room r2 = Room.builder()
                .price(BigDecimal.valueOf(42.00))
                .number(2)
                .build();

        List<Room> rooms = List.of(r1, r2);

        Hotel hotel = Hotel.builder()
                .name("Hotel test")
                .address("Test address")
                .rooms(rooms)
                .build();

        rooms.forEach(room -> room.setHotel(hotel));

        HotelDTO hotelDTO = new HotelDTO(hotel);

        return hotelDTO;
    }
}