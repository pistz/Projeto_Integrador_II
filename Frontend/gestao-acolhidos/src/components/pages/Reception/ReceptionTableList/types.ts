import { Reception } from "../../../../entity/Reception/Reception";

export default interface IListActionsProps<T extends Reception>{
    listQueryKey:string;
    getAllEntities():Promise<T[]>;
    allEntities:T[]
    deleteEntity(id:string):Promise<void>;
    editEntity?(entity:T):Promise<void>;
}