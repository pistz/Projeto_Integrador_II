import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Drawer, Space, Spin, Table, TableColumnsType } from 'antd'
import type { ColumnsType } from 'antd/es/table';
import IListActionsProps from './types';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { useTableData } from '../../../../hooks/useTableData';
import { MainInfo } from '../MainInfo/MainInfo';
import { DeleteButton } from '../../../shared/Button/DeleteButton/DeleteButton';


export const ListAll = ({listQueryKey, getAllEntities, deleteEntity}:IListActionsProps<Hosted>) => {
    const queryClient = useQueryClient();

    const {hostedTableData, setHostedTableData  } = useTableData();

    const [hosted, setHosted] = useState<Hosted>({}as Hosted)

    const [openMainInfo, setOpenMainInfo] = useState<boolean>(false);
    

    const onOpenMainInfo = (value:Hosted)=>{
        setHosted(value)
        setOpenMainInfo(true);
    }
    const onCloseMainInfo = () =>{
        setHosted({} as Hosted)
        setOpenMainInfo(false)
    }

    const columnData:TableColumnsType<Hosted> = [
        {
            title:'Nome',
            dataIndex:'firstName',
            key:'firstName',
            width: '13rem',
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
            width: '13rem',
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
            width: '8rem',
        
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
        mutationFn: (entity : Hosted) => {
        return deleteEntity(entity.id);
        },
        onSuccess: async () =>{
            notifySuccess("Entrada removida")
            queryClient.invalidateQueries({ queryKey: [listQueryKey] });
            const tableData:Hosted[] = await getAllEntities();
            setHostedTableData(tableData)
        },
        onError: (error)=>{
            notifyError(`${error}`);
        }
    });

    useEffect(()=>{
        const getTableData = async () => {
            const tableData:Hosted[] = await getAllEntities();
            if(tableData) setHostedTableData(tableData)
        }
            getTableData();
        },[getAllEntities, setHostedTableData]);
        

    const actionColumns:ColumnsType<Hosted> = [
        ...columnData,
        {
            title: 'Documentos',
            render: (value) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}} key={value.id}>
                    <Button type='primary' onClick={()=>onOpenMainInfo(value)}>Abrir</Button>
                    <Drawer width={700} height={1000} placement='top' closable={true} onClose={onCloseMainInfo} open={openMainInfo} destroyOnClose>
                        <MainInfo entity={hosted}/>
                    </Drawer>
                </Space>
            ),
        },
        {
            title: 'Situação de Risco',
            render: (_,record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='default' onClick={()=> console.log(record)}>Abrir</Button>
                </Space>
            ),
        },
        {
            title: 'Família',
            render: (_,record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='dashed' onClick={()=> console.log(record)}>Abrir</Button>
                </Space>
            ),
        },
        {
            title: 'Programa Social',
            render: (_,record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='default' onClick={()=> console.log(record)}>Abrir</Button>
                </Space>
            ),
        },
        {
            title: 'Saúde',
            render: (_,record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='default' onClick={()=> console.log(record)}>Abrir</Button>
                </Space>
            ),
        },
        {
            title: 'Plano de Atendimento',
            render: (_,record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='default' onClick={()=> console.log(record)}>Abrir</Button>
                </Space>
            ),
        },
        {
            title: '',
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
                rowKey={hosted.id}
                dataSource={hostedTableData} 
                columns={actionColumns}
                size='small'
                style={{display:'flex', position:"relative"}}
                tableLayout='auto'
        />
        </Spin>
    )
}
