import {UserLoginDTO} from "../../entity/User/dto/UserLoginDTO.ts";
import {endpoints} from "../../routes/endpoints.ts";
import axios from "axios";
import Repository from "../base/Repository.ts";

const tokenId:string = String(process.env.TOKEN_ID);
const loginUrl:string = endpoints.host+endpoints.login;
const roleUrl:string = endpoints.host+endpoints.userRole;
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
            const result = await axios.post(roleUrl, {token}, this.authHeader())
            return result.data;

        } catch (error) {
            Repository.checkError(error)
            throw Error("error: " + error);
        }
    }

    getTokenFromLocalStorage(){
        const getToken = sessionStorage.getItem(tokenId);
        if(getToken){
            const token = JSON.parse(getToken);
            return token;
        }
    }

    authHeader = () => {
        const token = this.getTokenFromLocalStorage();
        return {
            headers: {
                Authorization: "Bearer " + token,
            },
        };
    };
}