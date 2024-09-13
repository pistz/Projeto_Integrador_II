import { Entity } from "../../Entity";

export interface SocialPrograms extends Entity{
    hasPasseDeficiente:boolean
    hasPasseIdoso:boolean
    hasRendaCidada:boolean
    hasAcaoJovem:boolean
    hasVivaLeite:boolean
    hasBPC_LOAS:boolean
    hasBolsaFamilia:boolean
    hasPETI:boolean
    others:string
    howLong:number
    wage:number
    updatedAt:string
    }