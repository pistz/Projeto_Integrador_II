import { Button, Col, Divider, Form, Input, Statistic } from 'antd'
import React, { useEffect, useState } from 'react'
import { ConfigRepository } from '../../../repository/Config/ConfigRepository'
import { notifyError, notifySuccess } from '../../shared/PopMessage/PopMessage'
import { ManageUsers } from './SystemUsers/ManageUsers'

const config = new ConfigRepository()

export const Config:React.FC = () => {
  const [form] = Form.useForm();

  const [beds, setBeds] = useState<number>();

  const updateBedValue = async (values: { update: string }) => {
    const value = parseInt(values.update, 10); // Converte para número
    if (isNaN(value)) {
      notifyError('O valor inserido não é válido');
      return;
    }

    try {
      await config.updateCapacity(value);
      setBeds(value);
      notifySuccess('Valor atualizado');
      form.resetFields();
    } catch (error) {
      errorOnFinish(error);
    }
  };


  const errorOnFinish = (error:unknown) =>{
    notifyError("Erro ao realizar cadastro");
    return error;
}

  useEffect(()=>{
    const getCapacity = async()=>{
      const value = await config.getCapacity();
      if(value > 0){
        setBeds(value);
      }
    }
    getCapacity();
  },[]);


  return (
    <div style={{display:'flex', flexDirection:"column", alignItems:"center", justifyContent:'start', margin:"0 auto"}}>
      <div>
      <Divider>Configurações do Sistema</Divider>
      <Divider></Divider>
      <Col span={24}>
        <Statistic title="Capacidade de acolhimento" value={`${beds} leitos`} />
        <Form
          form={form}
          onFinish={updateBedValue}
          clearOnDestroy={true}
        >
          <Form.Item name={['update']}>
            <Input style={{width:'10rem'}} type='number'/>
          </Form.Item>
            <Button type='primary' htmlType='submit'>Atualizar capacidade</Button>
        </Form>
      </Col>
      <div>
        <ManageUsers />
      </div>
      </div>
    </div>
  )
}
