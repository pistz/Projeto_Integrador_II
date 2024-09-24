import React, { useEffect, useState } from 'react'
import { UsersTable } from './UsersTable'

import { Button, Divider, Form, Input, Select } from 'antd'
import { mainDivStyle } from './styles'
import { UserRepository } from '../../../../repository/User/UserRepository'
import { Role } from '../../../../entity/User/IUser'
import { RegisterUserDTO } from '../../../../entity/dto/User/RegisterUserDTO'
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage'
import { tableContainer } from '../styles'
import { CheckOutlined } from '@ant-design/icons'


const userQueryKey = 'userQueryKey'

export const ManageUsers:React.FC = () => {
    const [form] = Form.useForm()
    const [updated, setUpdated] = useState<boolean>(false);
    const userRepository = new UserRepository()

    const roleOptions: { label: string; value: Role }[] = [
        { label: 'ADMINISTRADOR', value: 'ADMIN' },
        { label: 'SECRETARIA', value: 'SECRETARY' },
        { label: 'DIRETORIA', value: 'BOARD' },
    ];

    const onFinish = async (values:RegisterUserDTO) =>{
        const body:RegisterUserDTO = {
            email:values.email,
            password:values.password,
            role:values.role
        }
        try {
            await userRepository.register(body);
            notifySuccess("Registro efetivado")
            setUpdated(true);
            form.resetFields();
        } catch (error) {
            errorOnFinish(error)
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

    useEffect(()=>{
        const reload = ()=>{
            if(updated){
                setUpdated(false)
            }
        }
        reload();
    },[setUpdated, updated])

  return (
    <>
        <div style={mainDivStyle}>
            <div style={tableContainer}>
            <UsersTable 
                deleteEntity={userRepository.delete}
                listQueryKey={userQueryKey}
                getAllEntities={userRepository.findAllUsers}
            />
            </div>
            <Divider>Criar novo usuário</Divider>
            <Form
                form={form}
                clearOnDestroy={true}
                layout='vertical'
                onFinish={onFinish}
            >
                <Form.Item name={['email']} label={'E-mail'} rules={[{required:true, message:'e-mail é obrigatório'}, {pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message:"Ainda é um email inválido"}]}>
                    <Input type='text' placeholder='email@email.com'/>
                </Form.Item>
                <Form.Item name={['password']} label={'Senha'} rules={[{required:true, message:'senha é obrigatório'}]}>
                    <Input type='password'/>
                </Form.Item>
                <Form.Item name={['role']} label={'Nivel de Acesso'} rules={[{required:true, message:'Nível de acesso é obrigatório'}]}>
                <Select placeholder="Selecione o cargo">
                    {roleOptions.map((role) => (
                    <Select.Option key={role.value} value={role.value}>
                        {role.label}
                    </Select.Option>
                    ))}
                </Select>
                </Form.Item>

                <Button type='primary' htmlType='submit' icon={<CheckOutlined />}>Salvar</Button>
            </Form>
        </div>
    </>
  )
}
