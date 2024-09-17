import axios from "axios";
import { Hosted } from "../../entity/Hosted/Hosted";
import Repository from "../base/Repository";
import { hostedRoutes } from "../../routes/endpoints";
import { createHostedDto } from "../../entity/dto/Hosted/createHostedDto";
import { authHeader } from "../../services/Token";

export class HostedRepository extends Repository{

    findAll = async():Promise<Hosted[]> =>{
        try {
            const result = await axios.get(hostedRoutes.findAll, authHeader())
            return result.data;
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    create = async(dto:createHostedDto):Promise<void> =>{
        try{
            await axios.post(hostedRoutes.create,dto,authHeader())
        }catch(error){
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

}