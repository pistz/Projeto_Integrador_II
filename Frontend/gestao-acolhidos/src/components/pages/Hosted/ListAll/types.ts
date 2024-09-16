import { Hosted } from "../../../../entity/Hosted/Hosted";

export default interface IListActionsProps<T extends Hosted>{
    listQueryKey:string;
    getAllEntities():Promise<T[]>;
    deleteEntity?(id:string):Promise<void>;
    editEntity?(entity:T):Promise<void>;
}