import { Entity } from "../../Entity";

export interface FamilyComposition extends Entity{
    hasFamily:boolean
    hasFamilyBond:boolean
    updatedAt:string
}