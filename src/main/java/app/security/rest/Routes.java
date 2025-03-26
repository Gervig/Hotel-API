package app.security.rest;

import app.controllers.impl.HotelController;
import app.dtos.ErrorMessage;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.security.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Routes
{
    private static HotelController hotelController;
    private static ISecurityController securityController = new SecurityController();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(Routes.class);

    public static EndpointGroup getRoutes(EntityManagerFactory emf)
    {
        hotelController = new HotelController(emf);

        return () ->
        {
            path("hotel", () ->
            {
                get("/", (ctx) ->
                {
                    logger.info("Information about the resource that was accessed: " + ctx.path());
                    List<HotelDTO> hotelDTOS = hotelController.getAllHotels();
                    ctx.json(hotelDTOS);
                });
                get("/{id}", (ctx) ->
                {
                    try
                    {
                        int id = Integer.parseInt(ctx.pathParam("id"));
                        HotelDTO hotelDTO = hotelController.getHotelById(id);
                        if (hotelDTO == null)
                        {
                            throw new NullPointerException();
                        }
                        ctx.json(hotelDTO);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No Hotel with that ID");
                        ctx.status(404).json(error);
                    }
                });
                get("/{id}/rooms", (ctx) ->
                {
                    try
                    {
                        int id = Integer.parseInt(ctx.pathParam("id"));
                        HotelDTO hotelDTO = hotelController.getHotelById(id);
                        if (hotelDTO == null)
                        {
                            throw new NullPointerException();
                        }
                        List<RoomDTO> roomDTOS = hotelDTO.getRooms();
                        ctx.json(roomDTOS);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No hotel with that ID");
                        ctx.status(404).json(error);
                    }
                });
                post("/", (ctx) ->
                {
                    try
                    {
                        HotelDTO incomingHotel = ctx.bodyAsClass(HotelDTO.class);
                        HotelDTO returnedHotel = hotelController.createHotel(incomingHotel);
                        ctx.json(returnedHotel);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    }
                });
                put("/{id}", (ctx) ->
                {
                    int id = Integer.parseInt(ctx.pathParam("id"));

                    try
                    {
                        HotelDTO test = hotelController.getHotelById(id);
                        if (test == null)
                        {
                            throw new NullPointerException();
                        }
                        HotelDTO incomingHotel = ctx.bodyAsClass(HotelDTO.class);
                        HotelDTO returnedHotel = hotelController.updateHotel(incomingHotel);
                        ctx.json(returnedHotel);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No hotel with that ID");
                        ctx.status(404).json(error);
                    }
                });
                delete("/{id}", (ctx) ->
                {
                    int id = Integer.parseInt(ctx.pathParam("id"));

                    try
                    {
                        HotelDTO test = hotelController.getHotelById(id);
                        if (test == null)
                        {
                            throw new NullPointerException();
                        }
                        hotelController.deleteHotel(id);
                        ctx.status(204).json("Successfully deleted hotel");
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No hotel with that ID");
                        ctx.status(404).json(error);
                    }
                });
            });
            path("auth", () ->
            {
                post("register", securityController.register(), Role.ANYONE);
                post("login", securityController.login(), Role.ANYONE);
            });
            path("secured", () -> {
                get("demo", ctx -> {
                    Set<RouteRole> assignedRoles = ctx.routeRoles();
                    System.out.println("Assigned roles inside route: " + assignedRoles);
                    ctx.json(objectMapper.createObjectNode().put("msg", "Success"));
                }, Role.USER);
            });
        };
    }
}