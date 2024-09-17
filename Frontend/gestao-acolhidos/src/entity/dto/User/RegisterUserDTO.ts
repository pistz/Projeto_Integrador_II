
import {Role} from "../../User/IUser.ts";

export interface RegisterUserDTO{
    email:string,
    password:string,
    role:Role
}