import { Entity } from "../../Entity";

export interface SituationalRisk extends Entity{
    lookUp:Lookup
    migrant:Migrant
    homeless:Homeless
    updatedAt:string
}

type Lookup = "FORMAL"| "ESPONTANEA"| "ABORDAGEM_DE_RUA"
type Migrant =  "FIXAR_RESIDENCIA"| "PASSAGEM"| "OUTRO"
type Homeless = "PERNOITE"| "REFEICAO"| "OUTRO" 