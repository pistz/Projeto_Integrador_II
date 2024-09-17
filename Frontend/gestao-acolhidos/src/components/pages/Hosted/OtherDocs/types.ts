export interface DocsForm {
    generalRegisterRG:string,
    dateOfIssueRG:string,
    driversLicenseNumber:string,
    birthCertificate:{
        certificateNumber:number,
        sheets:string,
        book:number
    }
}