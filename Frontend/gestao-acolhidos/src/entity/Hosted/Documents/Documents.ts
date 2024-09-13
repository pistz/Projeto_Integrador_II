import { Entity } from "../../Entity";
import { BirthCertificate } from "../BirthCertificate/BirthCertificate";

export interface Documents extends Entity{
    generalRegisterRG:string,
    dateOfIssueRG:string,
    hasLicense:boolean,
    driversLicenseNumber:string,
    birthCertificate:BirthCertificate,
    updatedAt:string
}