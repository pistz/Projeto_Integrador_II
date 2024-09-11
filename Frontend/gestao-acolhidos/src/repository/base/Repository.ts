import axios, {AxiosResponse} from "axios";
import HttpError from "../error/HttpError";


export default abstract class Repository{

    protected static checkError(error:unknown):void{
        if(axios.isAxiosError(error)){
            const response = error.response as AxiosResponse;
            throw new HttpError(error.status,error.message,response.data)
        }
        else{
            throw error;
        }
    }
    
}