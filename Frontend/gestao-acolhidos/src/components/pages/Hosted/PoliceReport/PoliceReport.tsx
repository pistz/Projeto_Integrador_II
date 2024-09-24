import React, { useCallback, useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { FormProps } from 'antd/es/form/Form';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { PoliceReportForm } from './types';
import { updateHostedPoliceReportDto } from '../../../../entity/dto/Hosted/updatePoliceReportDto';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { Button, Collapse, Divider, Form, Input, Space, Switch } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { PoliceReport } from '../../../../entity/Hosted/PoliceReport/PoliceReport';
import dayjs from 'dayjs';

const hostedRepository = new HostedRepository();

export const PoliceReportComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [form] = Form.useForm();
    const [edit, setEdit] = useState<boolean>(false);

    const [reports, setReports] = useState<PoliceReport[]>(entity.policeReport);

    const handleUpdatePoliceReport:FormProps<PoliceReportForm>['onFinish'] = async(values:updateHostedPoliceReportDto) =>{
        try {
            await hostedRepository.updatePoliceReport(values, entity.id)
            .then(()=>{
                notifySuccess("Registro Atualizado");
                clearForm();
                updateData();
            });
            handleSwitchChange();
        } catch (error) {
            errorOnFinish(error)
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }
    
    const handleSwitchChange = () => {
        setEdit(!edit);
    };

    const clearForm =()=>{
        form.resetFields()
    }

    const updateData = useCallback(async()=>{
        const data = await hostedRepository.findById(entity.id)
        setReports(data.policeReport)
    },[entity.id])

    useEffect(()=>{
        const loadEntityData = async()=>{
            updateData()
        }
        loadEntityData();
    },[updateData])

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };



  return (
    <>
        <Divider>Novo Boletim de Ocorrência</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Switch checked={edit} onClick={handleSwitchChange} unCheckedChildren="Incluir" checkedChildren="Fechar" />

            <Form
                form={form}
                onFinish={handleUpdatePoliceReport}
                disabled={!edit}
                layout='vertical'
                clearOnDestroy={true}
            >
                <Form.Item name={['reportProtocol']} label='Nº do B.O' >
                    <Input />
                </Form.Item>

                <Form.Item name={['policeDepartment']} label='Delegacia' >
                    <Input />
                </Form.Item>

                <Form.Item name={['city']} label='Cidade' >
                    <Input />
                </Form.Item>

                <Form.Item name={['reportInfo']} label='Descrição' >
                    <TextArea size='large'/>
                </Form.Item>

                <Button htmlType='submit' type='primary'>Salvar</Button>

            </Form>
        </Space>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Divider>Histórico de B.O</Divider>
            {reports.map((report) =>{
                return (
                <>
                    <Collapse accordion 
                        style={{width:'30rem'}}
                        key={report.id}
                        items={[{key:report.id, 
                        label:`Boletim nº ${report.reportProtocol}`, 
                        children:(<>
                        <table >
                            <ul><strong>Nº do B.O: </strong>{report.reportProtocol}</ul>
                            <ul><strong>Delegacia: </strong>{report.policeDepartment}</ul>
                            <ul><strong>Cidade: </strong>{report.city}</ul>
                            <ul><strong>Descrição: </strong>{report.reportInfo}</ul>
                            <ul><strong>Inclusão no Sistema: </strong>{changeDateFormatVisualization(report.createdAt)}</ul>
                        </table>
                        </>)}]}/>
                </>
                )
            })}
        </Space>
    </>
  )
}
