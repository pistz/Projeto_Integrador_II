import axios from "axios";
import { Hosted } from "../../entity/Hosted/Hosted";
import Repository from "../base/Repository";
import { hostedRoutes } from "../../routes/endpoints";
import { createHostedDto } from "../../entity/dto/Hosted/createHostedDto";
import { authHeader } from "../../services/Token";
import { updateDocsHostedDto } from "../../entity/dto/Hosted/updateDocsHostedDto";
import { updateHostedRefAddressDto } from "../../entity/dto/Hosted/updateRefAddressDto";
import { updateHostedPoliceReportDto } from "../../entity/dto/Hosted/updatePoliceReportDto";
import { updateFamilyCompositionDTO } from "../../entity/dto/Hosted/updateFamilyCompositionHostedDto";

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

    findById = async(id:string):Promise<Hosted> =>{
        try {
            const result = await axios.get(hostedRoutes.findById+id, authHeader())
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

    edit = async(dto:createHostedDto, id:string):Promise<void> =>{
        try {
            await axios.put(hostedRoutes.edit+id,dto,authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    deleteHosted = async(id:string): Promise<void> =>{
        try {
            await axios.delete(hostedRoutes.deleteHosted+id, authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    updateDocs = async(dto:updateDocsHostedDto, id:string):Promise<void> =>{
        try {
            await axios.put(hostedRoutes.updateDocs+id,dto,authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    updateRefAddress = async(dto:updateHostedRefAddressDto, id:string):Promise<void> =>{
        try {
            await axios.put(hostedRoutes.updateRefAddress+id,dto,authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    updatePoliceReport = async(dto:updateHostedPoliceReportDto, id:string):Promise<void> =>{
        try {
            await axios.put(hostedRoutes.updatePoliceReport+id,dto,authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    updateFamilyComposition = async(dto:updateFamilyCompositionDTO, id:string):Promise<void> =>{
        try {
            await axios.put(hostedRoutes.updateFamilyComposition+id,dto,authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }



}