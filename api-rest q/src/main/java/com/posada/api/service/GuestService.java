package com.posada.api.service;

import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class GuestService {

    // Simulación de base de datos en memoria
    private final Map<String, GuestResponse> database = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(3); // Empieza en 3 para no chocar con los precargados

    // Datos precargados — disponibles desde que arranca la app
    @PostConstruct
    void init() {
        GuestResponse g1 = new GuestResponse();
        g1.setId("1");
        g1.setName("Juan Perez");
        g1.setPhone("4421234567");
        g1.setEmail("juan@gmail.com");
        database.put("1", g1);

        GuestResponse g2 = new GuestResponse();
        g2.setId("2");
        g2.setName("Maria Lopez");
        g2.setPhone("4429876543");
        g2.setEmail("maria@gmail.com");
        database.put("2", g2);
    }

    // POST — Crear huésped
    public GuestResponse createGuest(Guest guest) {
        System.out.println("Service - Creando huésped: " + guest.getName());

        String newId = String.valueOf(idCounter.getAndIncrement());

        GuestResponse response = new GuestResponse();
        response.setId(newId);
        response.setName(guest.getName());
        response.setPhone(guest.getPhone());
        response.setEmail(guest.getEmail());

        database.put(newId, response);

        return response;
    }

    // GET — Obtener todos los huéspedes
    public List<GuestResponse> getAllGuests() {
        System.out.println("Service - Obteniendo todos los huéspedes");
        return new ArrayList<>(database.values());
    }

    // GET por ID — Obtener huésped por ID
    public GuestResponse getGuestById(String guestId) {
        System.out.println("Service - Buscando huésped con ID: " + guestId);
        return database.get(guestId);
    }

    // PUT — Actualizar huésped
    public GuestResponse updateGuest(String guestId, Guest guest) {
        System.out.println("Service - Actualizando huésped con ID: " + guestId);

        if (!database.containsKey(guestId)) {
            return null;
        }

        GuestResponse updated = new GuestResponse();
        updated.setId(guestId);
        updated.setName(guest.getName());
        updated.setPhone(guest.getPhone());
        updated.setEmail(guest.getEmail());

        database.put(guestId, updated);

        return updated;
    }

    // DELETE — Eliminar huésped
    public boolean deleteGuest(String guestId) {
        System.out.println("Service - Eliminando huésped con ID: " + guestId);

        if (!database.containsKey(guestId)) {
            return false;
        }

        database.remove(guestId);
        return true;
    }
}
