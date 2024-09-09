package org.felipe.gestaoacolhidos.model.controller.NightReception;

import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

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

    @GetMapping("/all")
    @Operation(description = "Lista todos os eventos já criados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada, pode ser vazia"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<NightReceptionDTO>> getAll(){
        List<NightReceptionDTO> response = nightReceptionService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
