import { IUser } from "../../../entity/User/IUser";

export default interface IListActionsProps<T extends IUser>{
    listQueryKey:string;
    getAllEntities():Promise<T[]>;
    deleteEntity(id:string):Promise<void>;
    editEntity?(entity:T):Promise<void>;
}