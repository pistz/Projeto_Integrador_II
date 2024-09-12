import { Dispatch, SetStateAction } from "react";

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