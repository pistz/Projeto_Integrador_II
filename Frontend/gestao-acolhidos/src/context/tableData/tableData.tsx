import React, { createContext, useState } from 'react';
import { IChildren, TablesContextData } from "../types";
import { Hosted } from '../../entity/Hosted/Hosted';


const TableDataContext = createContext<TablesContextData>({} as TablesContextData);

export const TableDataProvider: React.FC<IChildren> = ({ children }:IChildren) => {

    const [hostedTableData, setHostedTableData] = useState<Hosted[]>([]);

    return (
        <TableDataContext.Provider value={{
            hostedTableData,
            setHostedTableData
        }}>
            {children}
        </TableDataContext.Provider>
    );

}

export default TableDataContext;