import { Button, Col, Divider, Form, InputNumber, Statistic, Switch } from 'antd'
import React, { useEffect, useState } from 'react'
import { ConfigRepository } from '../../../repository/Config/ConfigRepository'
import { notifyError, notifySuccess } from '../../shared/PopMessage/PopMessage'
import { ManageUsers } from './SystemUsers/ManageUsers'
import { CheckOutlined } from '@ant-design/icons'

const config = new ConfigRepository()
const MAX_CAPACITY = 100;
export const Config:React.FC = () => {
  const [form] = Form.useForm();

  const [beds, setBeds] = useState<number>(0);

  const [edit, setEdit] = useState<boolean>(false);

  const handleEdit = () =>{
    setEdit(!edit)
  }

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
    <div style={{display:'flex', flexDirection:"column", alignItems:"center", justifyContent:'flex-start', margin:"1rem auto"}}>
      <div>
        <Divider>Configurações do Sistema</Divider>
        <Col span={24} style={{display:"flex", flexDirection:'column', alignItems:"center", justifyContent:'center'}}>
          <Statistic title="Capacidade de acolhimento" value={`${beds} leitos`}/>
          <Form
            form={form}
            onFinish={updateBedValue}
            clearOnDestroy={true}
          >
            <Form.Item name={['update']}>
              <InputNumber style={{width:'10rem'}} max={MAX_CAPACITY}/>
            </Form.Item>
              <Button type='primary' htmlType='submit' icon={<CheckOutlined/>}>Atualizar capacidade</Button>
          </Form>
        </Col>

        <div style={{display:'flex', flexDirection:"column", alignItems:'center', justifyContent:"center", margin:'3rem'}}>
          <Divider>Gerir Usuários</Divider>
          <Switch checked={edit} onClick={handleEdit} unCheckedChildren="Usuários" checkedChildren="Fechar" style={{margin:'2rem 0', width:'7rem'}} />
          {edit? <><ManageUsers /></> : <></>}
        </div>
      </div>
    </div>
  )
}
