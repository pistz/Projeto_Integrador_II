import { Dispatch, SetStateAction } from "react";
import { Hosted } from "../entity/Hosted/Hosted";
import { IUser } from "../entity/User/IUser";
import { Reception } from "../entity/Reception/Reception";
import { queryReceptionDto } from "../entity/dto/Reception/queryReceptionDto";

export interface IChildren {
    children: React.ReactNode;
}

export interface AuthContextData {
    signed:boolean;
    userEmail:string;
    userRole:string;


    setUserEmail:Dispatch<SetStateAction<string>>;
    setSigned: Dispatch<SetStateAction<boolean>>;
    setUserRole:Dispatch<SetStateAction<string>>;

}

export interface TablesContextData {

    hostedTableData:Hosted[]
    userTableData:IUser[]
    hostedEntity:Hosted
    receptionTableData:Reception[]
    reportReferenceDate:queryReceptionDto
            

    setHostedTableData:Dispatch<SetStateAction<Hosted[]>>
    setUserTableData:Dispatch<SetStateAction<IUser[]>>
    setHostedEntity:Dispatch<SetStateAction<Hosted>>
    setReceptionTableData:Dispatch<SetStateAction<Reception[]>>
    setReportReferenceDate:Dispatch<SetStateAction<queryReceptionDto>>

}