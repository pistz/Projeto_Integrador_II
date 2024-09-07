package org.felipe.gestaoacolhidos.model.domain.service.NightStand;

import org.felipe.gestaoacolhidos.model.domain.dto.NightStand.NightStandCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightStand.NightStandResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.NightStand.NightStand;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface NightStandService {

    NightStandResponseDTO createEvent(NightStandCreateDTO dto);
    NightStandResponseDTO deleteEvent(UUID id);
    NightStandResponseDTO updateEvent(UUID id, NightStandCreateDTO dto);
    List<NightStand> findAll();
    NightStand findByEventDate(LocalDate date);
    NightStand findById(UUID uuid);


}
