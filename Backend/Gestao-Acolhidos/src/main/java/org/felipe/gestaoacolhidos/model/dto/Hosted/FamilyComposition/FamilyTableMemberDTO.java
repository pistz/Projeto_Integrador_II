package org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition;

import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;


public record FamilyTableMemberDTO(
        String name,
        int age,
        Gender gender,
        MaritalStatus maritalStatus,
        Education education,
        String occupation
) {}
