package org.udgtl.controller;

import org.eclipse.microprofile.faulttolerance.*;
import org.udgtl.model.Libro;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Path("/libros")
public class LibroController {

    List<Libro> libroList = new ArrayList<>();
    Logger LOGGER = Logger.getLogger("Demologer");


    @GET
    @Timeout(value = 5000L)
    @Retry(maxRetries = 4)
    @CircuitBreaker(failureRatio = 0.1, delay = 15000L)
    @Bulkhead(value = 1)
    @Fallback(fallbackMethod = "getLibroFallbackList")
    public List<Libro> getLibroList(){
        LOGGER.info("Devolviendo libros");
        return this.libroList;
    }

    @GET
    @Path("/first")
    @Timeout(value = 5000L)
    @Retry(maxRetries = 4)
    @CircuitBreaker(failureRatio = 0.1, delay = 15000L)
    @Bulkhead(value = 1)
    @Fallback(fallbackMethod = "getLibroFallbackList")
    public List<Libro> getFirstLibro(){
        LOGGER.info("obtieniendo la primer el primer libro");
        return List.of(this.libroList.get(0));
    }


    public List<Libro> getLibroFallbackList(){
        LOGGER.warning("Se produjo un error");
        var libro = new Libro(-1l, "Default", "Autor por defecto");
        return List.of(libro);
    }
}
