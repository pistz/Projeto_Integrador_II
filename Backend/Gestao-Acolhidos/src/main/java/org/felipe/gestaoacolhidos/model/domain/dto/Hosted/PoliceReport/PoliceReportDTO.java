package org.felipe.gestaoacolhidos.model.domain.dto.Hosted.PoliceReport;

public record PoliceReportDTO(
        String reportProtocol,
        String policeDepartment,
        String city,
        String reportInfo
) {
}
