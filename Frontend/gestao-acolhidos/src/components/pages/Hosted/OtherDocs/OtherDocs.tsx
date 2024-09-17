import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, Input, Space, Switch } from 'antd'
import { FormProps, useForm } from 'antd/es/form/Form'
import { updateDocsHostedDto } from '../../../../entity/dto/Hosted/updateDocsHostedDto'
import { DocsForm } from './types'

export const OtherDocs:React.FC<{entity:Hosted}> = ({entity}) => {
    const [form] = useForm();
    const [edit, setEdit] = useState<boolean>(false);

    const handleDocsUpdate:FormProps<DocsForm>['onFinish'] = async(values:updateDocsHostedDto)=>{
        console.log(values)
    }

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

    //TODO
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
            <Form.Item name={['generalRegisterRG']} label='RG' >
                <Input value={initialValues.generalRegisterRG} />
            </Form.Item>


            <Button htmlType='submit' type='primary'>Salvar</Button>
            
        </Form>

    </Space>
  )
}
