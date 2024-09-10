package org.felipe.gestaoacolhidos.model.domain.service.NightReception;

import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface NightReceptionService {

    NightReceptionResponseDTO createEvent(NightReceptionCreateDTO dto);
    NightReceptionResponseDTO deleteEvent(UUID id);
    NightReceptionResponseDTO updateEvent(UUID id, NightReceptionCreateDTO dto);
    List<NightReceptionDTO> findAll();
    NightReceptionDTO findByEventDate(LocalDate date);
    List<NightReceptionDTO> findAllEventsByMonthAndYear(int month, int year);
    List<NightReceptionDTO> findAllEventsByYear(int year);
    NightReceptionDTO findById(UUID uuid);


}
