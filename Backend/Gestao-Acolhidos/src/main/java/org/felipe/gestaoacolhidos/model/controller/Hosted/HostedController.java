package org.felipe.gestaoacolhidos.model.controller.Hosted;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.service.Hosted.HostedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hosted")
@Tag(name = "Endpoints de Acolhidos")
public class HostedController {

    @Autowired
    HostedService hostedService;

    @GetMapping("/find/all")
    @Operation(description = "Retorna uma lista de Acolhidos", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada")
    })
    public ResponseEntity<List<Hosted>> findAll(){
        var all = hostedService.findAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(all);
    }

    @GetMapping("/find/{id}")
    @Operation(description = "Retorna um Acolhido", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Id não existente")
    })
    public ResponseEntity<Hosted> findById(@PathVariable UUID id){
        var hosted = hostedService.findById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(hosted);
    }

    @PostMapping("/create")
    @Operation(description = "Cria e registra um Acolhido", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Acolhido criado e registrado"),
            @ApiResponse(responseCode = "406", description = "Acolhido já existe e não pode ser duplicado"),
            @ApiResponse(responseCode = "400", description = "O CPF usado no cadastro é inválido")
    })
    public ResponseEntity<HostedResponseCreatedDTO> createHosted(@RequestBody HostedCreateNewDTO dto){
        var created = hostedService.create(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(created);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta permanentemente um registro de Acolhido - Não recomendado", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro deletado"),
            @ApiResponse(responseCode = "404", description = "ID do registro não existe")
    })
    public ResponseEntity<HostedResponseDeletedDTO> deleteHosted(@PathVariable UUID id){
        var deleted = hostedService.delete(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).body(deleted);
    }

    @PutMapping("/update-main-info/{id}")
    @Operation(description = "Atualiza os dados principais do Acolhido", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro atualizado"),
            @ApiResponse(responseCode = "404", description = "Acolhido não existe")
    })
    public ResponseEntity<HostedResponseUpdatedDTO> updateHostedMainInformation(@PathVariable UUID id, @RequestBody HostedCreateNewDTO dto){
        var updated = hostedService.updateIdentification(id, dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(updated);
    }

    @PutMapping("/update-docs/{id}")
    @Operation(description = "Atualiza os documentos do Acolhido", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro atualizado"),
            @ApiResponse(responseCode = "404", description = "Acolhido não existe")
    })
    public ResponseEntity<HostedResponseUpdatedDTO> updateHostedDocuments(@PathVariable UUID id, @RequestBody DocumentsUpdateDTO dto){
        var updated = hostedService.updateDocuments(id, dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(updated);
    }

    @PutMapping("/update-risk/{id}")
    @Operation(description = "Atualiza o risco situacional do Acolhido", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro atualizado"),
            @ApiResponse(responseCode = "404", description = "Acolhido não existe")
    })
    public ResponseEntity<HostedResponseUpdatedDTO> updateHostedSituationalRisk(@PathVariable UUID id, @RequestBody SituationalRiskUpdateDTO dto){
        var updated = hostedService.updateSituacionalRisk(id, dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(updated);
    }
}
