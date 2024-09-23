import React, { createContext, useState } from 'react';
import { IChildren, TablesContextData } from "../types";
import { Hosted } from '../../entity/Hosted/Hosted';
import { IUser } from '../../entity/User/IUser';
import { Reception } from '../../entity/Reception/Reception';


const TableDataContext = createContext<TablesContextData>({} as TablesContextData);

export const TableDataProvider: React.FC<IChildren> = ({ children }:IChildren) => {

    const [hostedTableData, setHostedTableData] = useState<Hosted[]>([]);
    const [hostedEntity, setHostedEntity] = useState<Hosted>({} as Hosted)
    const [userTableData, setUserTableData] = useState<IUser[]>([]);
    const [receptionTableData, setReceptionTableData] = useState<Reception[]>([])

    return (
        <TableDataContext.Provider value={{
            hostedTableData,
            setHostedTableData,
            userTableData, 
            setUserTableData,
            hostedEntity,
            setHostedEntity,
            receptionTableData,
            setReceptionTableData
        }}>
            {children}
        </TableDataContext.Provider>
    );

}

export default TableDataContext;