import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Reception } from '../../../../entity/Reception/Reception'
import IListActionsProps from './types'
import { Space, Spin, Table, TableColumnsType } from 'antd';
import dayjs from 'dayjs';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { useEffect, useState } from 'react';
import { ColumnsType } from 'antd/es/table';
import { DeleteButton } from '../../../shared/Button/DeleteButton/DeleteButton';

export const ReceptionTableList = ({listQueryKey, getAllEntities, deleteEntity}:IListActionsProps<Reception>) =>{

    const queryClient = useQueryClient();
    const [receptionTableData, setTableData] = useState<Reception[]>()

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };

    const columnData:TableColumnsType<Reception> = [
        {
            title:'Data de Acolhimento',
            dataIndex:'date',
            key:'date',
            render: (_,record) => changeDateFormatVisualization(record.date)
        },
        {
            title:'Quantidade do dia',
            dataIndex:'hostedList',
            key:'hostedList',
            render: (_,record) => record.hostedList.length
        }
    ]

    const { isLoading, isError, error } = useQuery({
        queryKey: [listQueryKey],
        queryFn: () => getAllEntities()
    });

    if(isError){
        notifyError(`${error}`);
    }

    const removeEntity = useMutation({
        mutationFn: (entity : Reception) => {
        return deleteEntity(entity.receptionId);
        },
        onSuccess: async () =>{
            notifySuccess("Entrada removida")
            queryClient.invalidateQueries({ queryKey: [listQueryKey] });
            const tableData:Reception[] = await getAllEntities();
            setTableData(tableData)
        },
        onError: (error)=>{
            notifyError(`${error}`);
        }
    });

    useEffect(()=>{
        const getTableData = async () => {
            const tableData:Reception[] = await getAllEntities();
            if(tableData) setTableData(tableData)
        }
            getTableData();
        },[getAllEntities, setTableData]);

        const actionColumns:ColumnsType<Reception> = [
            ...columnData,
            {
                title: 'Apagar',
                render: (_,record) => (
                    <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                        <DeleteButton removeMethod={()=> removeEntity.mutate(record)}></DeleteButton>
                    </Space>
                ),
            }
        ]

    return (
        <Spin spinning={isLoading}>
            <Table 
                rowKey={(e) => e.receptionId}
                dataSource={receptionTableData} 
                columns={actionColumns}
                size='small'
                tableLayout='auto'
        />
        </Spin>
    )
}
