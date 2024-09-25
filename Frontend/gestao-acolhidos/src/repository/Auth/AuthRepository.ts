import {UserLoginDTO} from "../../entity/dto/User/UserLoginDTO.ts";
import {authRoutes} from "../../routes/endpoints.ts";
import axios from "axios";
import Repository from "../base/Repository.ts";
import { authHeader } from "../../services/Token.ts";

const loginUrl:string = authRoutes.login;
const roleUrl:string = authRoutes.userRole;
export class AuthRepository extends Repository{

    authenticateUser = async (user:UserLoginDTO):Promise<string> =>{

        const body:UserLoginDTO = {
            email:user.email,
            password:user.password
        }
        try {
            const result = await axios.post(loginUrl, body)
            return result.data;
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    getRoleFromToken = async (token:string):Promise<string|void> =>{
        try {
            const result = await axios.post(roleUrl, {"token":token.trim()}, authHeader())
            return result.data;
        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }
}