package app;

import app.config.HibernateConfig;
import app.populators.HotelPopulator;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import jakarta.persistence.EntityManagerFactory;

public class Main
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        HotelPopulator.populate(emf);

        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(Routes.getRoutes(emf))
                .handleException()
                .startServer(7070);

    }

}
