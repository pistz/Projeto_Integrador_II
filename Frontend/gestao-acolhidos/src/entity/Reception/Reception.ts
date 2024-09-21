import { HostedReceptionDto } from "../dto/Hosted/hostedReceptionDto";

export interface Reception{
    receptionId:string,
    date:string,
    hostedList:HostedReceptionDto[]
}