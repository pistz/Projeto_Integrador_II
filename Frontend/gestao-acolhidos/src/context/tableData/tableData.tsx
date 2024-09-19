import React, { createContext, useState } from 'react';
import { IChildren, TablesContextData } from "../types";
import { Hosted } from '../../entity/Hosted/Hosted';
import { IUser } from '../../entity/User/IUser';


const TableDataContext = createContext<TablesContextData>({} as TablesContextData);

export const TableDataProvider: React.FC<IChildren> = ({ children }:IChildren) => {

    const [hostedTableData, setHostedTableData] = useState<Hosted[]>([]);
    const [hostedEntity, setHostedEntity] = useState<Hosted>({} as Hosted)
    const [userTableData, setUserTableData] = useState<IUser[]>([]);

    return (
        <TableDataContext.Provider value={{
            hostedTableData,
            setHostedTableData,
            userTableData, 
            setUserTableData,
            hostedEntity,
            setHostedEntity,
        }}>
            {children}
        </TableDataContext.Provider>
    );

}

export default TableDataContext;