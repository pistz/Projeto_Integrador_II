export interface FamilyTableForm{
    name:string
    age:number
    gender:Gender
    maritalStatus:MaritalStatus
    education:Education
    occupation:string
}

export type Gender = "MALE"| "FEMALE"| "OTHER"
export type MaritalStatus = "CASADO"| "SOLTEIRO"| "VIUVO"| "DIVORCIADO"| "SEPARADO"| "UNIAO_ESTAVEL"
export type Education =  "ANALFABETO"| "ENSINO_BASICO"| "ENSINO_MEDIO"| "ENSINO_SUPERIOR"| "POS_GRADUACAO" 