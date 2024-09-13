import { Entity } from "../../Entity";

export interface ReferenceAddress extends Entity{
    street:string
    cidade:string
    neighborhood:string
    number:number
    cep:string
    phoneNumber:number
    updatedAt:string
}