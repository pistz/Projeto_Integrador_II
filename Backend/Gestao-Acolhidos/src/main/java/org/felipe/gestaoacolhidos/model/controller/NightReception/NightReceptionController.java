package org.felipe.gestaoacolhidos.model.controller.NightReception;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.service.NightReception.NightReceptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/night-reception")
@Tag(name = "Endpoints de Acolhimentos noturnos")
public class NightReceptionController {

    private final NightReceptionService nightReceptionService;

    public NightReceptionController(NightReceptionService nightReceptionService) {
        this.nightReceptionService = nightReceptionService;
    }

    @PostMapping("/create")
    @Operation(description = "Cria um evento de acolhimento noturno", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado"),
            @ApiResponse(responseCode = "404", description = "Dados vazios ou inexistentes"),
            @ApiResponse(responseCode = "400", description = "Capacidade do albergue é menor do que a quantidade de nomes enviados"),
            @ApiResponse(responseCode = "400", description = "Já foi criado um evento nesta data"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<NightReceptionResponseDTO> createEvent(@RequestBody NightReceptionCreateDTO dto) {
        NightReceptionResponseDTO response = nightReceptionService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/find/all")
    @Operation(description = "Lista todos os eventos já criados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada, pode ser vazia"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<NightReceptionDTO>> getAll(){
        List<NightReceptionDTO> response = nightReceptionService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/find/{id}")
    @Operation(description = "Busca um evento por seu Id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um evento nesta data"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<NightReceptionDTO> getEventById(@PathVariable("id") UUID id){
        NightReceptionDTO response = nightReceptionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/find/date")
    @Operation(description = "Busca um evento por sua data", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um evento nesta data"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<NightReceptionDTO> getEventByDate(@RequestParam LocalDate date){
        NightReceptionDTO response = nightReceptionService.findByEventDate(date);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/find/month-year")
    @Operation(description = "Busca todos os eventos de um mês específico", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um eventos neste mês"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<NightReceptionDTO>> getAllEventsByMonthAndYear(@RequestParam int month, @RequestParam int year){
        List<NightReceptionDTO> response = nightReceptionService.findAllEventsByMonthAndYear(month, year);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/find/year")
    @Operation(description = "Busca todos os eventos de um ano específico", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um eventos neste mês"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<NightReceptionDTO>> getAllEventsByYear(@RequestParam int year){
        List<NightReceptionDTO> response = nightReceptionService.findAllEventsByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Apaga um evento registrado por seu Id", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento apagado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um evento com este Id"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<NightReceptionResponseDTO> deleteEvent(@PathVariable("id") UUID id){
        NightReceptionResponseDTO response = nightReceptionService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
    @PutMapping("/update/{id}")
    @Operation(description = "Atualiza um evento registrado por seu Id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não há um evento com este Id"),
            @ApiResponse(responseCode = "404", description = "Não há dados na requisição"),
            @ApiResponse(responseCode = "400", description = "Capacidade do albergue é menor do que a quantidade de nomes enviados"),
            @ApiResponse(responseCode = "400", description = "Já foi criado um evento nesta data"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<NightReceptionResponseDTO> updateEvent(@PathVariable("id") UUID id, @RequestBody NightReceptionCreateDTO dto){
        NightReceptionResponseDTO response = nightReceptionService.updateEvent(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
