import React, { createContext, useState } from 'react';
import { IChildren, TablesContextData } from "../types";
import { Hosted } from '../../entity/Hosted/Hosted';
import { IUser } from '../../entity/User/IUser';
import { Reception } from '../../entity/Reception/Reception';
import { queryReceptionDto } from '../../entity/dto/Reception/queryReceptionDto';


const TableDataContext = createContext<TablesContextData>({} as TablesContextData);

export const TableDataProvider: React.FC<IChildren> = ({ children }:IChildren) => {

    const [hostedTableData, setHostedTableData] = useState<Hosted[]>([]);
    const [hostedEntity, setHostedEntity] = useState<Hosted>({} as Hosted)
    const [userTableData, setUserTableData] = useState<IUser[]>([]);
    const [receptionTableData, setReceptionTableData] = useState<Reception[]>([])
    const [reportReferenceDate, setReportReferenceDate] = useState<queryReceptionDto>({} as queryReceptionDto)

    return (
        <TableDataContext.Provider value={{
            hostedTableData,
            setHostedTableData,
            userTableData, 
            setUserTableData,
            hostedEntity,
            setHostedEntity,
            receptionTableData,
            setReceptionTableData,
            reportReferenceDate,
            setReportReferenceDate
        }}>
            {children}
        </TableDataContext.Provider>
    );

}

export default TableDataContext;