package org.felipe.gestaoacolhidos.model.controller.Hosted;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.service.Hosted.HostedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hosted")
@Tag(name = "Endpoints de Acolhidos")
public class HostedController {

    @Autowired
    HostedService hostedService;

    @PostMapping("/create")
    public ResponseEntity<HostedResponseCreatedDTO> createHosted(@RequestBody HostedCreateNewDTO dto){
        var created = hostedService.create(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(created);
    }
}
