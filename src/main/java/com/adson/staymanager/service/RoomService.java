package com.adson.staymanager.service;

import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.exception.RoomNotFoundException;
import com.adson.staymanager.repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(Room room) {
        if (repository.existsByNumber(room.getNumber())) {
            throw new BusinessRuleException("Já existe um quarto com esse número");
        }

        return repository.save(room);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Quarto não encontrado"));
    }

    public Room update(Long id, Room updated) {
        Room room = findById(id);

        room.setNumber(updated.getNumber());
        room.setFloor(updated.getFloor());
        room.setDailyRate(updated.getDailyRate());

        return repository.save(room);
    }

    public Room changeStatus(Long id, RoomStatus newStatus) {
        Room room = findById(id);

        validateStatusTransition(room.getStatus(), newStatus);

        room.setStatus(newStatus);
        return repository.save(room);
    }

    private void validateStatusTransition(RoomStatus current, RoomStatus next) {

        if (current == next) return;
        
        switch (current) {
        
            case AVAILABLE -> {
                if (next != RoomStatus.RESERVED && next != RoomStatus.MAINTENANCE) {
                    throw invalidTransition(current, next,
                            "De AVAILABLE você só pode mudar para RESERVED ou MAINTENANCE.");
                }
            }
        
            case RESERVED -> {
                if (next != RoomStatus.OCCUPIED && next != RoomStatus.AVAILABLE) {
                    throw invalidTransition(current, next,
                            "De RESERVED você só pode mudar para OCCUPIED (check-in) ou AVAILABLE (cancelamento/liberação).");
                }
            }
        
            case OCCUPIED -> {
                if (next != RoomStatus.AVAILABLE) {
                    throw invalidTransition(current, next,
                            "De OCCUPIED você só pode mudar para AVAILABLE (check-out).");
                }
            }
        
            case MAINTENANCE -> {
                if (next != RoomStatus.AVAILABLE) {
                    throw invalidTransition(current, next,
                            "De MAINTENANCE você só pode mudar para AVAILABLE (manutenção finalizada).");
                }
            }
        }
    }

    private BusinessRuleException invalidTransition(RoomStatus current, RoomStatus next, String hint) {
        return new BusinessRuleException(
            "Transição de status não permitida: " + current + " -> " + next + ". " + hint
        );
    }
}