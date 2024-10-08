import axios from "axios";
import { configRoutes } from "../../routes/endpoints";
import Repository from "../base/Repository";
import { authHeader } from "../../services/Token";

export class ConfigRepository extends Repository{

    getCapacity = async():Promise<number> =>{
        try {
            const result = await axios.get(configRoutes.getCapacity, authHeader())
            return result.data
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    updateCapacity = async(value:number):Promise<void> =>{
        try {
            await axios.put(
                configRoutes.updateCapacity, 
                value, 
                {
                    ...authHeader(), 
                    headers: {
                        'Content-Type': 'application/json',
                        ...authHeader().headers, 
                    }
                }
            );
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }
}