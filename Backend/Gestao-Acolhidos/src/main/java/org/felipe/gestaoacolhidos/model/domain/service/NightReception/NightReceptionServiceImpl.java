package org.felipe.gestaoacolhidos.model.domain.service.NightReception;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResumedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.entity.NightReception.NightReception;
import org.felipe.gestaoacolhidos.model.logs.UserLoggingInterceptor;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.felipe.gestaoacolhidos.model.repository.nightReception.NightReceptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class NightReceptionServiceImpl implements NightReceptionService {

    private final NightReceptionRepository nightReceptionRepository;

    private final HostedRepository hostedRepository;

    private final CapacityReposity capacityReposity;

    private final UserLoggingInterceptor userLoggingInterceptor;

    public NightReceptionServiceImpl(NightReceptionRepository nightReceptionRepository,
                                     HostedRepository hostedRepository,
                                     CapacityReposity capacityReposity,
                                     UserLoggingInterceptor userLoggingInterceptor) {
        this.nightReceptionRepository = nightReceptionRepository;
        this.hostedRepository = hostedRepository;
        this.capacityReposity = capacityReposity;
        this.userLoggingInterceptor = userLoggingInterceptor;
    }

    @Override
    @Transactional
    public NightReceptionResponseDTO createEvent(NightReceptionCreateDTO dto){
        if(dateIsRepeated(dto.date())){
            throw new IllegalArgumentException("Já foi criado um evento para esta data");
        }
        checkRequestValidity(dto);
        NightReception nightReception = new NightReception();
        nightReception.setId(UUID.randomUUID());
        nightReception.setEventDate(dto.date());
        List<Hosted> hostedList = convertHostedDtoToHosted(dto);
        nightReception.setHosteds(hostedList);
        nightReception.setUpdatedBy(userLoggingInterceptor.getRegisteredUser());
        nightReceptionRepository.save(nightReception);
        return new NightReceptionResponseDTO("Evento criado com sucesso!");
    }

    @Override
    @Transactional
    public NightReceptionResponseDTO deleteEvent(UUID eventId) {
        if(eventId == null){
            throw new NoSuchElementException("Dados inexistentes");
        }
        NightReception nightReception = nightReceptionRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        nightReceptionRepository.delete(nightReception);
        return new NightReceptionResponseDTO("Evento removido com sucesso!");
    }

    @Override
    @Transactional
    public NightReceptionResponseDTO updateEvent(UUID eventId, NightReceptionCreateDTO dto) {
        Optional<NightReception> event = nightReceptionRepository.findById(eventId);
        if(event.isEmpty()){
            throw new NoSuchElementException("Evento inexistente");
        }
        if(dateIsRepeated(dto.date())){
            var result = this.findByEventDate(dto.date());
            if(result.receptionId() != eventId){
                throw new IllegalArgumentException("Já foi criado um evento para esta data");
            }
        }
        checkRequestValidity(dto);
        NightReception nightReception = event.get();
        List<Hosted> newHostedList = convertHostedDtoToHosted(dto);
        List<Hosted> mutableHosteds = new ArrayList<>(nightReception.getHosteds());
        mutableHosteds.clear();
        mutableHosteds.addAll(newHostedList);
        nightReception.setHosteds(mutableHosteds);;

        nightReception.setUpdatedBy(userLoggingInterceptor.getRegisteredUser());
        nightReceptionRepository.save(nightReception);
        return new NightReceptionResponseDTO("Evento atualizado com sucesso!");
    }

    @Override
    public List<NightReceptionDTO> findAll() {
        List<NightReception> query = nightReceptionRepository.findAll();
        return query.stream()
                .map(this::extractNightReceptionDTO)
                .toList();
    }

    @Override
    public NightReceptionDTO findByEventDate(LocalDate date) {
        NightReception query = nightReceptionRepository.findByEventDate(date).orElseThrow(NoSuchElementException::new);
        return extractNightReceptionDTO(query);
    }

    @Override
    public List<NightReceptionDTO> findAllEventsByMonthAndYear(int month, int year) {
        List<NightReception> query = nightReceptionRepository.findByMonthAndYear(month, year);
        return query.stream()
                .map(this::extractNightReceptionDTO)
                .toList();
    }

    @Override
    public List<NightReceptionDTO> findAllEventsByYear(int year) {
        List<NightReception> query = nightReceptionRepository.findByYear(year);
        return query.stream()
                .map(this::extractNightReceptionDTO)
                .toList();
    }

    @Override
    public NightReceptionDTO findById(UUID id) {
        NightReception query = nightReceptionRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return extractNightReceptionDTO(query);
    }

    private NightReceptionDTO extractNightReceptionDTO(NightReception nightReception) {
        List<HostedResumedDTO> hostedList = nightReception.getHosteds().stream()
                .map(hosted -> new HostedResumedDTO(
                        hosted.getId(),
                        hosted.getFirstName(),
                        hosted.getLastName(),
                        hosted.getSocialSecurityNumber()
                ))
                .collect(Collectors.toList());

        return new NightReceptionDTO(
                nightReception.getId(),
                nightReception.getEventDate(),
                hostedList
        );
    }

    private void checkRequestValidity(NightReceptionCreateDTO dto){
        if(dto == null){
            throw new NoSuchElementException("Dados inexistentes");
        }
        int currentCapacity = capacityReposity.findCurrentMaxCapacity().get();
        if(dto.hostedList().size() > currentCapacity){
            throw new IllegalArgumentException("A quantidade de acolhidos é maior do que a capacidade do albergue");
        }
    }

    private List<Hosted> convertHostedDtoToHosted(NightReceptionCreateDTO dto){
        return dto.hostedList().stream()
                .map(hostedRepository::findById)
                .map(Optional::get)
                .toList();
    }

    private boolean dateIsRepeated(LocalDate date){
        try {
            var result = this.findByEventDate(date);
            return result != null;
        }catch (NoSuchElementException e){
            return false;
        }
    }
}
