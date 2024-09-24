import { Entity } from "../../Entity";

export interface PoliceReport extends Entity{
    reportProtocol:string,
    policeDepartment:string,
    city:string,
    reportInfo:string,
    createdAt:string,
}
