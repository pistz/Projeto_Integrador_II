export default class HttpError extends Error{
    statusCode?:number;
    statusDescription?:string;
    response?:Response;

    constructor(statusCode?:number,statusDescription?:string,response?:Response){
        super()
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.response = response;

        Object.setPrototypeOf(this, HttpError.prototype);
    }
}