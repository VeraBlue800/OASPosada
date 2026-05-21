package com.posada.api.service;

import java.util.List;
import com.posada.api.entity.RoomEntity;
import com.posada.api.mapper.RoomMapper;
import com.posada.api.model.Room;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RoomService {

    private static final Logger LOG = Logger.getLogger(RoomService.class);

    @Inject
    EntityManager em;

    public Room createRoom(Room room) {
        LOG.infof("Service - Intentando crear habitación número: %d", room.getNumber());

        RoomEntity existing = em.find(RoomEntity.class, room.getNumber());
        if (existing != null) {
            LOG.warnf("Service - Conflicto: ya existe la habitación número %d", room.getNumber());
            throw new jakarta.ws.rs.ClientErrorException(
                    "Ya existe una habitación con el número " + room.getNumber(),
                    Response.Status.CONFLICT);
        }

        RoomEntity entity = RoomMapper.toEntity(room);
        persistRoom(entity);

        LOG.infof("Service - Habitación %d creada exitosamente", entity.getNumber());
        return RoomMapper.toModel(entity);
    }

    @Transactional
    void persistRoom(RoomEntity entity) {
        em.persist(entity);
    }

    public List<Room> getRooms() {
        LOG.info("Service - Obteniendo lista de habitaciones");
        List<RoomEntity> entities = em.createQuery("FROM RoomEntity", RoomEntity.class).getResultList();
        LOG.infof("Service - Total habitaciones encontradas: %d", entities.size());
        return entities.stream().map(RoomMapper::toModel).toList();
    }

    public Room getRoomById(String roomId) {
        LOG.infof("Service - Buscando habitación con ID: %s", roomId);

        if (roomId == null || !roomId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", roomId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido");
        }

        int id = Integer.parseInt(roomId);
        RoomEntity entity = em.find(RoomEntity.class, id);

        if (entity == null) {
            LOG.warnf("Service - Habitación no encontrada con número: %d", id);
            throw new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId);
        }

        return RoomMapper.toModel(entity);
    }

    @Transactional
    public Room updateRoom(String roomId, Room room) {
        LOG.infof("Service - Actualizando habitación con ID: %s", roomId);

        if (roomId == null || !roomId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", roomId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + roomId);
        }

        int id = Integer.parseInt(roomId);
        RoomEntity entity = em.find(RoomEntity.class, id);

        if (entity == null) {
            LOG.warnf("Service - Habitación no encontrada con número: %d", id);
            throw new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId);
        }

        entity.setType(RoomEntity.Type.valueOf(room.getType().getValue().replace(" ", "_")));
        entity.setPrice(room.getPrice());
        entity.setStatus(RoomEntity.Status.valueOf(room.getStatus().getValue().replace(" ", "_")));

        LOG.infof("Service - Habitación %d actualizada exitosamente", entity.getNumber());
        return RoomMapper.toModel(entity);
    }
}