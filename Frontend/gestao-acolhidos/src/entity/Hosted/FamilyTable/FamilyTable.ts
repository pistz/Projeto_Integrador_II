import { Entity } from "../../Entity";

export interface FamilyTable extends Entity{
    name:string
    age:number
    gender:Gender
    maritalStatus:MaritalStatus
    education:Education
    occupation:string
    updatedAt:string
}

type Gender = "MALE"| "FEMALE"| "OTHER"
type MaritalStatus = "CASADO"| "SOLTEIRO"| "VIUVO"| "DIVORCIADO"| "SEPARADO"| "UNIAO_ESTAVEL"
type Education =  "ANALFABETO"| "ENSINO_BASICO"| "ENSINO_MEDIO"| "ENSINO_SUPERIOR"| "POS_GRADUACAO" 