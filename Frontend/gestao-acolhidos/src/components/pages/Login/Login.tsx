import React from 'react';
import type { FormProps } from 'antd';
import { Button, Form, Input } from 'antd';
import {FieldType} from "./types.ts";
import {MainContainer} from "./styles.ts";
import logo from '../../../assets/logo1.jpeg'


const onFinish: FormProps<FieldType>['onFinish'] = (values) => {
    console.log('Success:', values);
};

const onFinishFailed: FormProps<FieldType>['onFinishFailed'] = (errorInfo) => {
    console.log('Failed:', errorInfo);
};

export const Login:React.FC = () => {
    return (
        <>
            <MainContainer>
                <img alt={"Logo"} src={logo} style={{width:'15rem', margin:'1rem 1rem'}}/>
                <Form
                    name="basic"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    style={{ maxWidth: 600 }}
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete="off"
                >
                    <Form.Item<FieldType>
                        label="E-mail"
                        name="email"
                        rules={[{ required: true, message: 'Coloque seu e-mail!' }]}
                    >
                        <Input
                            type="email"
                        />
                    </Form.Item>

                    <Form.Item<FieldType>
                        label="Senha"
                        name="password"
                        rules={[{ required: true, message: 'Coloque sua senha!' }]}
                    >
                        <Input.Password />
                    </Form.Item>

                    <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                        <Button type="primary" htmlType="submit">
                            Entrar
                        </Button>
                    </Form.Item>
                </Form>
            </MainContainer>
        </>
    );
};
