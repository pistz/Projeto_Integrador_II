import axios from "axios";
import { createReceptionDto } from "../../entity/dto/Reception/createReceptionDto";
import Repository from "../base/Repository";
import { receptionRoutes } from "../../routes/endpoints";
import { authHeader } from "../../services/Token";
import { Reception } from "../../entity/Reception/Reception";
import { queryReceptionDto } from "../../entity/dto/Reception/queryReceptionDto";

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

    findByMonthAndYear = async(dto:queryReceptionDto):Promise<Reception[]>=>{
        const query = `?month=${dto.month}&year=${dto.year}`
        try{
            const result = await axios.get(receptionRoutes.findByMonthAndYear+query,authHeader());
            return result.data;
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    findByYear = async(dto:queryReceptionDto):Promise<Reception[]>=>{
        const query = `?year=${dto.year}`
        try{
            const result = await axios.get(receptionRoutes.findByYear+query,authHeader());
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