import React from 'react';
import type { FormProps } from 'antd';
import { Button, Form, Input } from 'antd';
import {FieldType} from "./types.ts";
import {
    LoginButtonStyle,
    LoginFormStyle,
    LoginH2Style,
    LoginInputStyle,
    LoginSpanStyle,
    MainContainer
} from "./styles.ts";
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
                <h2 style={LoginH2Style}>SISTEMA DE GESTÃO DE ACOLHIDOS</h2>
                <img alt={"Logo"} src={logo} style={{width: '15rem', margin: '1rem 1rem'}}/>
                <Form
                    name="basic"
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={LoginFormStyle}
                    initialValues={{remember: true}}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete="off"
                >
                    <Form.Item<FieldType>
                        label="E-mail"
                        name="email"
                        rules={[{required: true, message: 'Coloque seu e-mail!'}]}
                    >
                        <Input
                            type="email"
                            style={LoginInputStyle}
                        />
                    </Form.Item>

                    <Form.Item<FieldType>
                        label="Senha"
                        name="password"
                        rules={[{required: true, message: 'Coloque sua senha!'}]}
                    >
                        <Input.Password
                            style={LoginInputStyle}
                        />
                    </Form.Item>

                    <Form.Item wrapperCol={{offset: 8, span: 16}}>
                        <Button type="primary" htmlType="submit" style={LoginButtonStyle}>
                            Entrar
                        </Button>
                    </Form.Item>
                </Form>
            <span style={LoginSpanStyle}>Versão 1.0 - {new Date().getFullYear()}</span>
            </MainContainer>
        </>
    );
};
