package org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition;

import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;

import java.util.Objects;

public record FamilyTableMemberDTO(
        String name,
        int age,
        Gender gender,
        MaritalStatus maritalStatus,
        Education education,
        String occupation
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyTableMemberDTO that = (FamilyTableMemberDTO) o;
        return Objects.equals(name, that.name) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender);
    }
}
