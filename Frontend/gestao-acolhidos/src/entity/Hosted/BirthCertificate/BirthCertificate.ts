import { Entity } from "../../Entity";

export interface BirthCertificate extends Entity{
    certificateNumber:number,
    sheets:string,
    book:number
}