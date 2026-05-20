package com.posada.api.service;

import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class GuestService {

    private static final Logger LOG = Logger.getLogger(GuestService.class);

    private final Map<String, GuestResponse> database = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(3);

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

        LOG.info("Service - Datos precargados: 2 huéspedes registrados");
    }

    public GuestResponse createGuest(Guest guest) {
        LOG.infof("Service - Creando huésped: %s", guest.getName());

        String newId = String.valueOf(idCounter.getAndIncrement());

        GuestResponse response = new GuestResponse();
        response.setId(newId);
        response.setName(guest.getName());
        response.setPhone(guest.getPhone());
        response.setEmail(guest.getEmail());

        database.put(newId, response);

        LOG.infof("Service - Huésped creado con ID: %s", newId);
        return response;
    }

    public List<GuestResponse> getAllGuests() {
        LOG.infof("Service - Obteniendo todos los huéspedes, total: %d", database.size());
        return new ArrayList<>(database.values());
    }

    public GuestResponse getGuestById(String guestId) {
        LOG.infof("Service - Buscando huésped con ID: %s", guestId);
        GuestResponse guest = database.get(guestId);
        if (guest == null) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
        }
        return guest;
    }

    public GuestResponse updateGuest(String guestId, Guest guest) {
        LOG.infof("Service - Actualizando huésped con ID: %s", guestId);

        if (!database.containsKey(guestId)) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
            return null;
        }

        GuestResponse updated = new GuestResponse();
        updated.setId(guestId);
        updated.setName(guest.getName());
        updated.setPhone(guest.getPhone());
        updated.setEmail(guest.getEmail());

        database.put(guestId, updated);

        LOG.infof("Service - Huésped actualizado: %s", updated.getName());
        return updated;
    }

    public boolean deleteGuest(String guestId) {
        LOG.infof("Service - Eliminando huésped con ID: %s", guestId);

        if (!database.containsKey(guestId)) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
            return false;
        }

        database.remove(guestId);
        LOG.infof("Service - Huésped eliminado con ID: %s", guestId);
        return true;
    }
}