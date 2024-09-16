import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Space, Spin, Table, TableColumnsType } from 'antd'
import type { ColumnsType } from 'antd/es/table';
import IListActionsProps from './types';
import { useQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { notifyError } from '../../../shared/PopMessage/PopMessage';
import { useTableData } from '../../../../hooks/useTableData';


export const ListAll = ({listQueryKey, getAllEntities}:IListActionsProps<Hosted>) => {
    const {hostedTableData, setHostedTableData  } = useTableData();

    const columnData:TableColumnsType<Hosted> = [
        {
            title:'Nome',
            dataIndex:'firstName',
            key:'fistName',
            width: '30vh',
            filters: hostedTableData.map((item) => ({
                text: item.firstName,
                value: item.firstName
            })),
            filterSearch:true,
            onFilter: (value, record) => record.firstName.startsWith(value as string),
            sorter: (a, b) => a.firstName.localeCompare(b.firstName),
        },
        {
            title:'Sobrenome',
            dataIndex:'lastName',
            key:'lastName',
            width: '30vh',
            filters: hostedTableData.map((item) => ({
                text: item.lastName,
                value: item.lastName
            })),
            filterSearch:true,
            onFilter: (value, record) => record.lastName.startsWith(value as string),
            sorter: (a, b) => a.lastName.localeCompare(b.lastName),
        },
        {
            title:'CPF',
            dataIndex:'socialSecurityNumber',
            key:'socialSecurityNumber',
            filters: hostedTableData.map((item) => ({
                text: item.socialSecurityNumber,
                value: item.socialSecurityNumber
            })),
            filterSearch:true,
            onFilter: (value, record) => record.socialSecurityNumber.startsWith(value as string),
            width: '30vh',
        
        },
        {
            title:'Idade',
            dataIndex:'age',
            key:'age'
        },
        {
            title:'Prontuário',
            dataIndex:'paperTrail',
            key:'paperTrail',
            filters: hostedTableData.map((item) => ({
                text: item.paperTrail,
                value: item.paperTrail
            })),
            filterSearch:true,
            onFilter: (value, record) => record.paperTrail === value,
            width: '10%',
        }
        
    ]

    const { isLoading, isError, error } = useQuery({
        queryKey: [listQueryKey],
        queryFn: () => getAllEntities()
    });

    if(isError){
        notifyError(`${error}`);
    }

    useEffect(()=>{
        const getTableData = async () => {
            const tableData:Hosted[] = await getAllEntities();
            if(tableData) setHostedTableData(tableData)
        }
            getTableData();
        },[getAllEntities, setHostedTableData]);
        

    const dataColumns:ColumnsType<Hosted> = [
        ...columnData,
        {
        title: 'Registro Completo',
        render: (_,record) => (
            <Space size="small">
                <Button onClick={()=> console.log(record)}>Abrir</Button>
            </Space>
        ),
        }
    ]

    return (
        <Spin spinning={isLoading}>
            <Table 
                rowKey="id"
                dataSource={hostedTableData} 
                columns={dataColumns}
                size='small'
        />
        </Spin>
    )
}
