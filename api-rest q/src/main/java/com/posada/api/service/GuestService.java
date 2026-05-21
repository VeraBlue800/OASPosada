package com.posada.api.service;

import com.posada.api.entity.GuestEntity;
import com.posada.api.mapper.GuestMapper;
import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class GuestService {

    private static final Logger LOG = Logger.getLogger(GuestService.class);

    @Inject
    EntityManager em;

    public GuestResponse createGuest(Guest guest) {
        LOG.infof("Service - Creando huésped: %s", guest.getName());

        GuestEntity entity = GuestMapper.toEntity(guest);
        persistGuest(entity);

        LOG.infof("Service - Huésped creado con ID: %d", entity.getId());
        return GuestMapper.toModel(entity);
    }

    @Transactional
    void persistGuest(GuestEntity entity) {
        em.persist(entity);
    }

    public List<GuestResponse> getAllGuests() {
        LOG.info("Service - Obteniendo todos los huéspedes");
        List<GuestEntity> entities = em.createQuery("FROM GuestEntity", GuestEntity.class).getResultList();
        LOG.infof("Service - Total huéspedes encontrados: %d", entities.size());
        return entities.stream().map(GuestMapper::toModel).toList();
    }

    public GuestResponse getGuestById(String guestId) {
        LOG.infof("Service - Buscando huésped con ID: %s", guestId);

        GuestEntity entity = em.find(GuestEntity.class, Integer.parseInt(guestId));
        if (entity == null) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
            return null;
        }

        return GuestMapper.toModel(entity);
    }

    public GuestResponse updateGuest(String guestId, Guest guest) {
        LOG.infof("Service - Actualizando huésped con ID: %s", guestId);

        GuestEntity entity = em.find(GuestEntity.class, Integer.parseInt(guestId));
        if (entity == null) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
            return null;
        }

        updateGuestEntity(entity, guest);

        LOG.infof("Service - Huésped actualizado: %s", entity.getName());
        return GuestMapper.toModel(entity);
    }

    @Transactional
    void updateGuestEntity(GuestEntity entity, Guest guest) {
        entity.setName(guest.getName());
        entity.setPhone(guest.getPhone());
        entity.setEmail(guest.getEmail());
        em.merge(entity);
    }

    public boolean deleteGuest(String guestId) {
        LOG.infof("Service - Eliminando huésped con ID: %s", guestId);

        GuestEntity entity = em.find(GuestEntity.class, Integer.parseInt(guestId));
        if (entity == null) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", guestId);
            return false;
        }

        deleteGuestEntity(entity);
        LOG.infof("Service - Huésped eliminado con ID: %s", guestId);
        return true;
    }

    @Transactional
    void deleteGuestEntity(GuestEntity entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}