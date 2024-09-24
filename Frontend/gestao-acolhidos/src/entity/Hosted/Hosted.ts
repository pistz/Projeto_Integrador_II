import { Entity } from "../Entity";
import { CustomTreatments } from "./CustomTreatments/CustomTreatments";
import { Documents } from "./Documents/Documents";
import { FamilyComposition } from "./FamilyComposition/FamilyComposition";
import { FamilyTable } from "./FamilyTable/FamilyTable";
import { MedicalRecord } from "./MedicalRecords/MedicalRecord";
import { PoliceReport } from "./PoliceReport/PoliceReport";
import { ReferenceAddress } from "./ReferenceAddress/ReferenceAddress";
import { SituationalRisk } from "./SituationalRisk/SituationalRisk";
import { SocialPrograms } from "./SocialPrograms/SocialPrograms";


export interface Hosted extends Entity{
    firstName: string,
    lastName: string,
    socialSecurityNumber: string,
    age:number,
    birthDay: string,//formato YYYY-mm-dd
    paperTrail: number,
    fathersName: string,
    mothersName: string,
    occupation: string,
    cityOrigin: string,
    stateOrigin: string,
    otherDocuments:Documents,
    policeReport:PoliceReport[],
    referenceAddress:ReferenceAddress,
    situationalRisk:SituationalRisk,
    socialPrograms:SocialPrograms,
    medicalRecord:MedicalRecord[],
    customTreatments:CustomTreatments[],
    familyComposition:FamilyComposition,
    familyTable:FamilyTable[],
    createdAt:string,
    updatedAt:string,
    updatedBy:string

}
