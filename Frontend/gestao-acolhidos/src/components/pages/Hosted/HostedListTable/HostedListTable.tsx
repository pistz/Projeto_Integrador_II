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
import { CalendarOutlined, DoubleLeftOutlined, HeartOutlined, SolutionOutlined} from '@ant-design/icons';
import { MedicalRecordComponent } from '../MedicalRecord/MedicalRecordComponent';
import { TreatmentsComponent } from '../Treatments/TreatmentsComponent';
import { IndividualReceptions } from '../IndividualReceptions/IndividualReceptions';


export const HostedListTable = ({listQueryKey, getAllEntities, deleteEntity}:IListActionsProps<Hosted>) => {
    const queryClient = useQueryClient();

    const {hostedTableData, setHostedTableData } = useTableData();

    const [hosted, setHosted] = useState<Hosted>({}as Hosted)

    const [hostedId, setHostedId] = useState<string>("");

    const [openMainInfo, setOpenMainInfo] = useState<boolean>(false);

    const [openMedicalRecords, setOpenMedicalRecords] = useState<boolean>(false)

    const [openCustomTreatments, setOpenCustomTreatments] = useState<boolean>(false)

    const [openReceptions, setOpenReceptions] = useState<boolean>(false)
    

    //Atua no menu de Cadastro Completo
    const onOpenMainInfo = (value:Hosted)=>{
        setHosted(value)
        setOpenMainInfo(true);
    }
    const onCloseMainInfo = () =>{
        setHosted({} as Hosted)
        setOpenMainInfo(false)
    }

    //Atua no menu de Saúde
    const onOpenMedicalRecord = (value:Hosted)=>{
        setHosted(value)
        setOpenMedicalRecords(true);
    }
    const onCloseMedicalRecord = () =>{
        setHosted({} as Hosted)
        setOpenMedicalRecords(false)
    }

    //Atua no menu de Plano de Atendimentos
    const onOpenCustomTreatments = (value:Hosted)=>{
        setHosted(value)
        setOpenCustomTreatments(true);
    }
    const onCloseCustomTreatments = () =>{
        setHosted({} as Hosted)
        setOpenCustomTreatments(false)
    }

    //Acolhimentos Individuais
    const onOpenReceptions = (id:string)=>{
        setHostedId(id)
        setOpenReceptions(true)
    }

    const onCloseReceptions =()=>{
        setHostedId("")
        setOpenReceptions(false)
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
            align:'center',
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
            key:'age',
            align:'center'
        },
        {
            title:'Prontuário',
            dataIndex:'paperTrail',
            key:'paperTrail',
            align:'center',
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
            title: 'Cadastro',
            key:'hostedTable',
            align:'center',
            render: (record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}} key={record.id}>
                    <Button type='primary' icon={<DoubleLeftOutlined/>} onClick={()=>onOpenMainInfo(record)} />
                    <Drawer width={1800} height={700} placement='right' closable={true} onClose={onCloseMainInfo} open={openMainInfo} destroyOnClose>
                        <MainInfo entity={hosted}/>
                    </Drawer>
                </Space>
            ),
        },
        {
            title: 'Histórico de Saúde',
            align:'center',
            render: (record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='dashed' danger size='middle' icon={<HeartOutlined />} onClick={()=> onOpenMedicalRecord(record)} />
                    <Drawer width={1800} height={700} placement='right' closable={true} onClose={onCloseMedicalRecord} open={openMedicalRecords} destroyOnClose>
                        <MedicalRecordComponent entity={hosted}/>
                    </Drawer>
                </Space>
            ),
        },
        {
            title: 'Plano Personalizado',
            align:'center',
            render: (record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='dashed' onClick={()=> onOpenCustomTreatments(record)} icon={<SolutionOutlined />}/>
                    <Drawer width={1800} height={700} placement='right' closable={true} onClose={onCloseCustomTreatments} open={openCustomTreatments} destroyOnClose>
                        <TreatmentsComponent entity={hosted}/>
                    </Drawer>
                </Space>
            ),
        },
        {
            title: 'Acolhimentos',
            align:'center',
            render: (record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'center'}}>
                    <Button type='default' onClick={()=> onOpenReceptions(record.id)} icon={<CalendarOutlined />}/>
                    <Drawer width={500} placement='right' closable={true} onClose={onCloseReceptions} open={openReceptions} destroyOnClose>
                        <IndividualReceptions entity={hostedId}/>
                    </Drawer>
                </Space>
            ),
        },
        {
            title: 'Apagar',
            width:'10rem',
            align:'end',
            render: (record) => (
                <Space size="small" style={{display:'flex', alignItems:'center', justifyContent:'flex-end'}}>
                    <DeleteButton removeMethod={()=> removeEntity.mutate(record)}></DeleteButton>
                </Space>
            ),
        }
    ]

    return (
        <Spin spinning={isLoading}>
            <Table 
                rowKey={(e) => e.id}
                dataSource={hostedTableData} 
                columns={actionColumns}
                size='small'
                tableLayout='auto'
        />
        </Spin>
    )
}
