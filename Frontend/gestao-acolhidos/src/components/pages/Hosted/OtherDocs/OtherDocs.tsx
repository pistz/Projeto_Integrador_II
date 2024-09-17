import React from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Divider, Form, Input, Space } from 'antd'
import { FormProps, useForm } from 'antd/es/form/Form'
import { updateDocsHostedDto } from '../../../../entity/dto/Hosted/updateDocsHostedDto'
import { DocsForm } from './types'

export const OtherDocs:React.FC<{entity:Hosted}> = ({entity}) => {
    const [form] = useForm();

    const handleDocsUpdate:FormProps<DocsForm>['onFinish'] = async(values:updateDocsHostedDto)=>{
        console.log(values)
    }
    
  return (

    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem'}}>
        <Divider>Documentos Adicionais</Divider>
        <Space direction='horizontal'>
            <p>Nome:</p>
            <Input value={`${entity.firstName} ${entity.lastName}`} disabled/>
        </Space>
        <Form
            form={form}
            initialValues={{ ...entity }}
            layout='vertical'
            onFinish={handleDocsUpdate}
        >
            
        </Form>

    </Space>
  )
}
