package org.felipe.gestaoacolhidos.model.controller.Capacity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.felipe.gestaoacolhidos.model.domain.service.Capacity.CapacityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capacity")
@Tag(name = "Endpoint de configuração de capacidade do Albergue")
public class CapacityController {

    private final CapacityService capacityService;

    public CapacityController(CapacityService capacityService) {
        this.capacityService = capacityService;
    }

    @GetMapping("/get")
    @Operation(description = "Busca a capacidade total do Albergue", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacidade máxima recuperada")
    })
    public ResponseEntity<Integer> getMaxCapacity(){
        var capacity = capacityService.getCapacity();
        return ResponseEntity.ok().body(capacity);
    }

    @PutMapping("/update")
    @Operation(description = "Atualiza a capacidade total do Albergue", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capacidade máxima atualizada")
    })
    public ResponseEntity updateCurrentCapacity(@RequestBody int capacity){
        capacityService.updateCapacity(capacity);
        return ResponseEntity.status(HttpStatus.CREATED).body("new capacity: " + capacity);
    }
}
