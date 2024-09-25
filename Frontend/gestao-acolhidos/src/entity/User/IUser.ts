import {Entity} from "../Entity.ts";

export interface IUser extends Entity{
    email:string,
    role:string
}

export type Role = 'ADMIN'|'BOARD'|'SECRETARY';
