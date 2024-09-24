import { Entity } from "../../Entity";

export interface MedicalRecord extends Entity{
    complaints:string
    createdAt:string
}