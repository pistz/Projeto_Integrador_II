package org.felipe.gestaoacolhidos.model.domain.service.Capacity;

import org.felipe.gestaoacolhidos.model.domain.entity.Capacity.Capacity;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class CapacityServiceImpl implements CapacityService{

    private final CapacityReposity capacityReposity;

    public CapacityServiceImpl(CapacityReposity capacityReposity) {
        this.capacityReposity = capacityReposity;
    }

    @Override
    public int getCapacity() {
        return capacityReposity.findCurrentMaxCapacity().get();
    }

    @Override
    @Transactional
    public void updateCapacity(int capacity) {
        if(capacity <= 0){
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        Capacity currentCapacity = checkExists();
        if(currentCapacity == null) {
            currentCapacity = new Capacity(
                    UUID.randomUUID(),
                    capacity,
                    LocalDate.now()
            );
            capacityReposity.save(currentCapacity);
            return;
        }
        currentCapacity.setMaxCapacity(capacity);
        currentCapacity.setUpdatedAt(LocalDate.now());
        capacityReposity.save(currentCapacity);
    }

    private Capacity checkExists(){
        Optional<Capacity> currentCapacity = capacityReposity.findCurrentConfig();
        return currentCapacity.orElse(null);
    }
}
