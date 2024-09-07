package org.felipe.gestaoacolhidos.model.domain.service.Capacity;

import org.felipe.gestaoacolhidos.model.domain.entity.Capacity.Capacity;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CapacityServiceImpl implements CapacityService{

    private final CapacityReposity capacityReposity;

    public CapacityServiceImpl(CapacityReposity capacityReposity) {
        this.capacityReposity = capacityReposity;
    }

    @Override
    public int getCapacity() {
        return capacityReposity.findCurrentMaxCapacity();
    }

    @Override
    public void updateCapacity(int capacity) {
        Capacity currentCapacity = capacityReposity.findCurrentConfig();
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
}
