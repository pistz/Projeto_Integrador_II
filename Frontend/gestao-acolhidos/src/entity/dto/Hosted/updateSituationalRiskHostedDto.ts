
export interface updateSituationalRiskHostedDto{
    lookUp:Lookup
    migrant:Migrant
    homeless:Homeless
}

type Lookup = "FORMAL"| "ESPONTANEA"| "ABORDAGEM_DE_RUA"
type Migrant =  "FIXAR_RESIDENCIA"| "PASSAGEM"| "OUTRO"
type Homeless = "PERNOITE"| "REFEICAO"| "OUTRO" 