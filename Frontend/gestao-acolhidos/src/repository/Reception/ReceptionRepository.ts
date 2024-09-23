import axios from "axios";
import { createReceptionDto } from "../../entity/dto/Reception/createReceptionDto";
import Repository from "../base/Repository";
import { receptionRoutes } from "../../routes/endpoints";
import { authHeader } from "../../services/Token";
import { Reception } from "../../entity/Reception/Reception";

export class ReceptionRepository extends Repository{

    findAll = async():Promise<Reception[]> =>{
        try{
            const result = await axios.get(receptionRoutes.findAll,authHeader());
            return result.data;
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    create = async(dto:createReceptionDto):Promise<void> =>{
        try{
            await axios.post(receptionRoutes.createReception,dto,authHeader())
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    delete = async(id:string):Promise<void> =>{
        try{
            await axios.delete(receptionRoutes.deleteReception+id,authHeader())
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }
}