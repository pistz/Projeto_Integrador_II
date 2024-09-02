package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyComposition;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable.FamilyTable;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hosted_composicao_familiar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FamilyComposition {

    @Id
    private UUID id;

    @Column(nullable = false, name = "possui_familia")
    private boolean hasFamily;

    @Column(nullable = false, name = "possui_vinculo_familiar")
    private boolean hasFamilyBond;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
