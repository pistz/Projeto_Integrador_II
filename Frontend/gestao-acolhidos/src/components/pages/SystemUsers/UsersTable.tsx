import { Button, Space, Spin, Table, TableColumnsType } from 'antd'
import { IUser } from '../../../entity/User/IUser'
import IListActionsProps from './types'
import { useTableData } from '../../../hooks/useTableData';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { notifyError, notifySuccess } from '../../shared/PopMessage/PopMessage';
import { ColumnsType } from 'antd/es/table';
import { useEffect } from 'react';
import { DeleteButton } from '../../shared/Button/DeleteButton/DeleteButton';

export const UsersTable = ({listQueryKey, getAllEntities, deleteEntity}:IListActionsProps<IUser>) => {
    const {userTableData, setUserTableData} = useTableData();
    const queryClient = useQueryClient();


    const columnData:TableColumnsType<IUser> = [
        {
            title:'E-mail',
            dataIndex:'email',
            key:'email'
        },
        {
            title:'Nível de Acesso',
            dataIndex:'role',
            key:'role'
        },
    ]
    
    const { isLoading, isError, error } = useQuery({
        queryKey: [listQueryKey],
        queryFn: () => getAllEntities()
    });

    const removeEntity = useMutation({
        mutationFn: (entity : IUser) => {
        return deleteEntity(entity.id);
        },
        onSuccess: async () =>{
            notifySuccess("Entrada removida")
            queryClient.invalidateQueries({ queryKey: [listQueryKey] });
            const tableData:IUser[] = await getAllEntities();
            setUserTableData(tableData)
        },
        onError: (error)=>{
            notifyError(`${error}`);
        }
    });

    if(isError){
        notifyError(`${error}`);
    }

    useEffect(()=>{
        const getTableData = async () => {
            const tableData:IUser[] = await getAllEntities();
            if(tableData) setUserTableData(tableData)
        }
            getTableData();
        },[getAllEntities, setUserTableData]);
        

    const actionColumns:ColumnsType<IUser> = [
        ...columnData,
        {
            title:'Opções',
            render:(_,record) => (
                <Space size={'small'}>
                    <Button shape='round' type='default' onClick={()=> console.log(record, ' update btn')}>
                        Alterar Acesso
                    </Button>

                    <DeleteButton removeMethod={()=> removeEntity.mutate(record)} />                      
                </Space>
            )
        }
    ]

    return (
        <Spin spinning={isLoading}>
            <Table 
                rowKey="id"
                dataSource={userTableData} 
                columns={actionColumns}
                size='small'
                style={{display:'flex', position:"relative"}}
                tableLayout='auto'
            />
        </Spin>
    )
}
