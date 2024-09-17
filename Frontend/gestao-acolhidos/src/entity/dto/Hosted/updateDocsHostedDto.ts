
export interface updateDocsHostedDto{
    generalRegisterRG:string,
    dateOfIssueRG:string,
    hasLicense:boolean,
    driversLicenseNumber:string,
    birthCertificate:BirthCert,
}

type BirthCert = {
    certificateNumber:number,
    sheets:string,
    book:number
}