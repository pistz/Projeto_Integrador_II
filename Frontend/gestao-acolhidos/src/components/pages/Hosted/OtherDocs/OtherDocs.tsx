import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, Input, Space, Switch } from 'antd'
import { FormProps, useForm } from 'antd/es/form/Form'
import { updateDocsHostedDto } from '../../../../entity/dto/Hosted/updateDocsHostedDto'
import { DocsForm } from './types'
import dayjs from 'dayjs'
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository'
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage'


const hostedRepository = new HostedRepository()

export const OtherDocs:React.FC<{entity:Hosted}> = ({entity}) => {
    const [form] = useForm();
    const [edit, setEdit] = useState<boolean>(false);

    const handleDocsUpdate:FormProps<DocsForm>['onFinish'] = async(values:updateDocsHostedDto)=>{
        values.dateOfIssueRG = handleDateChange(values.dateOfIssueRG)
        try {
            await hostedRepository.updateDocs(values,entity.id)
                .then(()=>{
                    notifySuccess("Cadastro atualizado")
                });
                handleSwitchChange()
        } catch (error) {
            errorOnFinish(error)
        }

    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }
    

    const handleDateChange = (date: string):string => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        if (!date) return '01/01/1900';
        const regexYYYYMMDD = /^\d{4}-\d{2}-\d{2}$/;
        const regexDDMMYYYY = /^\d{2}\/\d{2}\/\d{4}$/;
        
        if (regexYYYYMMDD.test(date)) {
            return date;
        } else if (regexDDMMYYYY.test(date)) {
            return dayjs(date, inputFormat).format(dateFormat);
        }
        return '01/01/1900';
    };
    

    const handleSwitchChange = () => {
        setEdit(!edit);
    };

 
    const initialValues:updateDocsHostedDto={
        generalRegisterRG:'',
        dateOfIssueRG:'',
        driversLicenseNumber:'',
        birthCertificate:{
            book:0,
            certificateNumber:0,
            sheets:''
        }
    }

  return (

    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem'}}>
        <Divider>Documentos Adicionais</Divider>
        <Space direction='horizontal'>
            <p>Nome:</p>
            <Input value={`${entity.firstName} ${entity.lastName}`} disabled/>
        </Space>
        <Switch checked={edit} onClick={handleSwitchChange} unCheckedChildren="Editar" checkedChildren="Editar" />

        <Form
            form={form}
            initialValues={entity.otherDocuments? entity.otherDocuments : initialValues }
            layout='vertical'
            onFinish={handleDocsUpdate}
            disabled={!edit}
        >
            <Form.Item name={['generalRegisterRG']} label='RG' rules={[{pattern:/^\d{2}\.\d{3}\.\d{3}(-\d{1})?$/, message:'RG inválido'}]}>
                <Input value={entity.otherDocuments? entity.otherDocuments.generalRegisterRG:initialValues.generalRegisterRG} placeholder='00.000.000-0'/>
            </Form.Item>

            <Form.Item name={['dateOfIssueRG']} label='Data de emissão do RG'>
                {edit? <Input placeholder={initialValues.dateOfIssueRG || '00/00/0000'}/>
                :<Input />}
            </Form.Item>

            <Form.Item name={['driversLicenseNumber']} label='CNH' >
                <Input value={entity.otherDocuments? entity.otherDocuments.driversLicenseNumber:initialValues.driversLicenseNumber} />
            </Form.Item>

            <Divider>Certidão de Nascimento</Divider>

            <Form.Item name={['birthCertificate','certificateNumber']} label='Número da Certidão de Nascimento' >
                <Input value={entity.otherDocuments? entity.otherDocuments.birthCertificate.certificateNumber:initialValues.birthCertificate.certificateNumber} />
            </Form.Item>

            <Form.Item name={['birthCertificate','sheets']} label='Folhas nº' >
                <Input value={entity.otherDocuments? entity.otherDocuments.birthCertificate.sheets:initialValues.birthCertificate.sheets} />
            </Form.Item>

            <Form.Item name={['birthCertificate','book']} label='Livro nº' >
                <Input value={entity.otherDocuments? entity.otherDocuments.birthCertificate.book:initialValues.birthCertificate.book} />
            </Form.Item>

            <Button htmlType='submit' type='primary'>Salvar</Button>
            
        </Form>

    </Space>
  )
}
