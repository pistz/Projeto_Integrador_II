import { Entity } from "../../Entity";

export interface ReferenceAddress extends Entity{
    street:string
    city:string
    neighborhood:string
    number:number
    cep:string
    phoneNumber:number
    updatedAt:string
}