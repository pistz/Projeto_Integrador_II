import React from 'react'
import { UsersTable } from './UsersTable'

import { Button, Divider, Form, FormProps, Input, Select } from 'antd'
import { mainDivStyle } from './styles'
import { UserRepository } from '../../../../repository/User/UserRepository'
import {  Role } from '../../../../entity/User/IUser'
import { RegisterUserDTO } from '../../../../entity/dto/User/RegisterUserDTO'
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage'
import { tableContainer } from '../styles'
import { CheckOutlined } from '@ant-design/icons'


const userQueryKey = 'userQueryKey'
const userRepository = new UserRepository()

export const ManageUsers:React.FC = () => {
    const [form] = Form.useForm()
    

    const roleOptions: { label: string; value: Role }[] = [
        { label: 'ADMINISTRADOR', value:'ADMIN' },
        { label: 'SECRETARIA', value:'SECRETARY' },
        { label: 'DIRETORIA', value:'BOARD'},
    ];

    const onFinish:FormProps<RegisterUserDTO>['onFinish'] = async (values:RegisterUserDTO) =>{
        try {
            await userRepository.register(values)
            .then(() => notifySuccess("Registro efetivado"));
            form.resetFields();
        } catch (error) {
            errorOnFinish(error)
            form.resetFields();
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

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
                    <Input.Password />
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
