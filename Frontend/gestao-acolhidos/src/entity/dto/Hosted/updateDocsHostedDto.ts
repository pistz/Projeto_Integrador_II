
export interface updateDocsHostedDto{
    generalRegisterRG:string,
    dateOfIssueRG:string,
    driversLicenseNumber:string,
    birthCertificate:BirthCert,
}

type BirthCert = {
    certificateNumber:number,
    sheets:string,
    book:number
}