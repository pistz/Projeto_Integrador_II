export interface DocsForm {
    generalRegisterRG:string,
    dateOfIssueRG:string,
    hasLicense:boolean,
    driversLicenseNumber:string,
    birthCertificate:{
        certificateNumber:number,
        sheets:string,
        book:number
    }
}