package org.felipe.gestaoacolhidos.model.domain.service.NightStand;

import org.felipe.gestaoacolhidos.model.domain.dto.NightStand.NightStandCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightStand.NightStandResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Attendance.Attendance;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.entity.NightStand.NightStand;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.felipe.gestaoacolhidos.model.repository.nightStand.NightStandRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class NightStandServiceImpl implements NightStandService {

    private final NightStandRepository nightStandRepository;

    private final HostedRepository hostedRepository;

    private final CapacityReposity capacityReposity;

    public NightStandServiceImpl(NightStandRepository nightStandRepository, HostedRepository hostedRepository, CapacityReposity capacityReposity) {
        this.nightStandRepository = nightStandRepository;
        this.hostedRepository = hostedRepository;
        this.capacityReposity = capacityReposity;
    }

    @Override
    public NightStandResponseDTO createEvent(NightStandCreateDTO dto){
        if(dto == null){
            throw new NoSuchElementException("Dados inexistentes");
        }
        NightStand nightStand = new NightStand();
        if(dto.hostedList().size() > capacityReposity.findCurrentMaxCapacity()){
            throw new IllegalArgumentException("A quantidade de acolhidos é maior do que a capacidade do albergue");
        }
        nightStand.setId(UUID.randomUUID());
        nightStand.setEventDate(dto.date());
        List<Hosted> hostedList = dto.hostedList().stream()
                        .map(hostedRepository::findById)
                        .map(hosted -> hosted.get())
                        .toList();
        nightStand.setHosteds(hostedList);
        hostedList.stream()
                .peek(hosted -> {
                    List<Attendance> attendance = hosted.getAttendance();
                    if (attendance == null) {
                        attendance = new ArrayList<>();
                    }
                    attendance.add(new Attendance(nightStand.getId(), nightStand.getEventDate()));
                    hosted.setAttendance(attendance);
                })
                .forEach(hostedRepository::save);
        nightStandRepository.save(nightStand);
        return new NightStandResponseDTO("Evento criado com sucesso!");
    }

    @Override
    public NightStandResponseDTO deleteEvent(UUID eventId) {
        if(eventId == null){
            throw new NoSuchElementException("Dados inexistentes");
        }
        NightStand nightStand = nightStandRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        nightStandRepository.delete(nightStand);
        return new NightStandResponseDTO("Evento removido com sucesso!");
    }

    @Override
    public NightStandResponseDTO updateEvent(UUID id, NightStandCreateDTO dto) {
        return null;
    }

    @Override
    public List<NightStand> findAll() {
        return nightStandRepository.findAll();
    }

    @Override
    public NightStand findByEventDate(LocalDate date) {
        return nightStandRepository.findByEventDate(date).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public NightStand findById(UUID id) {
        return nightStandRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
