
import {Role} from "../IUser.ts";

export interface RegisterUserDTO{
    email:string,
    password:string,
    role:Role
}