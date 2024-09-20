
export interface SituationalRiskForm{
    lookUp:Lookup
    migrant:Migrant
    homeless:Homeless
}

export type Lookup = "FORMAL"| "ESPONTANEA"| "ABORDAGEM_DE_RUA"
export type Migrant =  "FIXAR_RESIDENCIA"| "PASSAGEM"| "OUTRO"
export type Homeless = "PERNOITE"| "REFEICAO"| "OUTRO" 