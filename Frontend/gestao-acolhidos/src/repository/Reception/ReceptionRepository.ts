import axios from "axios";
import { createReceptionDto } from "../../entity/dto/Reception/createReceptionDto";
import Repository from "../base/Repository";
import { receptionRoutes } from "../../routes/endpoints";
import { authHeader } from "../../services/Token";

export class ReceptionRepository extends Repository{

    create = async(dto:createReceptionDto):Promise<void> =>{
        try{
            await axios.post(receptionRoutes.createReception,dto,authHeader())
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }
}