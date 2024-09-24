import axios from "axios";
import { IUser } from "../../entity/User/IUser";
import { userRoutes } from "../../routes/endpoints";
import { authHeader } from "../../services/Token";
import Repository from "../base/Repository";
import { RegisterUserDTO } from "../../entity/dto/User/RegisterUserDTO";

export class UserRepository extends Repository{

    findAllUsers = async ():Promise<IUser[]> =>{
        try {
            const users = await axios.get(userRoutes.getAll, authHeader());
            return users.data
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    register = async (dto:RegisterUserDTO):Promise<void> =>{
        try {
            await axios.post(userRoutes.register, dto, authHeader());
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    delete = async (id:string):Promise<void> =>{
        try {
            await axios.delete(userRoutes.delete+id, authHeader())
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }
}