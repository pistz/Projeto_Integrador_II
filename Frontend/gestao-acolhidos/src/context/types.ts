import { Dispatch, SetStateAction } from "react";
import { Hosted } from "../entity/Hosted/Hosted";

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

    setHostedTableData:Dispatch<SetStateAction<Hosted[]>>

}