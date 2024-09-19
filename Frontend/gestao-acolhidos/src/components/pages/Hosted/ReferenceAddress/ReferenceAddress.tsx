import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { ReferenceAddress } from './types'
import { FormProps, useForm } from 'antd/es/form/Form';
import { Button, Divider, Form, Input, Space, Switch } from 'antd';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { updateHostedRefAddressDto } from '../../../../entity/dto/Hosted/updateRefAddressDto';

const hostedRepository = new HostedRepository()

export const RefAddress:React.FC<{entity:Hosted}> = ({entity}) => {
    const [form] = useForm();
    const [edit, setEdit] = useState<boolean>(false);

    const handleUpdateRefAddress:FormProps<ReferenceAddress>['onFinish'] = async(values:updateHostedRefAddressDto) =>{
        try {
            await hostedRepository.updateRefAddress(values, entity.id)
            .then(()=>{
                notifySuccess("Registro Atualizado");
            });
            handleSwitchChange();
        } catch (error) {
            errorOnFinish(error)
        }
    }

    const handleSwitchChange = () => {
        setEdit(!edit);
    };

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }


    const initialValues:updateHostedRefAddressDto={
        cep:'',
        city:'',
        neighborhood:'',
        number:0,
        phoneNumber:0,
        street:''
    }

    return (
    <>
        <Divider>Referências</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem'}}>
            <Switch checked={edit} onClick={handleSwitchChange} unCheckedChildren="Editar" checkedChildren="Editar" />
            <Form
                form={form}
                initialValues={entity.referenceAddress ? entity.referenceAddress : initialValues}
                onFinish={handleUpdateRefAddress}
                disabled={!edit}
                layout='vertical'
            >
                <Form.Item name={['street']} label='Rua' >
                    <Input value={entity.referenceAddress? entity.referenceAddress.street:initialValues.street} style={{width:'15rem'}}/>
                </Form.Item>

                <Form.Item name={['number']} label='Número' >
                    <Input type='number' value={entity.referenceAddress? entity.referenceAddress.number:initialValues.number} style={{width:'15rem'}}/>
                </Form.Item>

                <Form.Item name={['neighborhood']} label='Complemento e Bairro' >
                    <Input value={entity.referenceAddress? entity.referenceAddress.neighborhood:initialValues.neighborhood} style={{width:'15rem'}}/>
                </Form.Item>

                <Form.Item name={['city']} label='Cidade' >
                    <Input value={entity.referenceAddress? entity.referenceAddress.city:initialValues.city} style={{width:'15rem'}}/>
                </Form.Item>

                <Form.Item name={['cep']} label='CEP' rules={[{pattern:/^\d{2}\.\d{3}-\d{3}$/, message:'Coloque o CEP no formato XX.XXX-XXX'}]}>
                    <Input value={entity.referenceAddress? entity.referenceAddress.cep:initialValues.cep} style={{width:'15rem'}} placeholder='00.000-000'/>
                </Form.Item>
                <Divider>Telefone de Referência</Divider>
                <Form.Item name={['phoneNumber']} label='Telefone' rules={[{pattern:/^(\d{11}|\d{12})$/, message:'Apenas números com DDD com o zero, sem espaços nem traços'}]}>
                    <Input type='number' value={entity.referenceAddress? entity.referenceAddress.phoneNumber:initialValues.phoneNumber} style={{width:'15rem'}}/>
                </Form.Item>

                <Button htmlType='submit' type='primary'>Salvar</Button>
            </Form>
        </Space>
    </>

    )
}
