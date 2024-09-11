import React from 'react';
import type { FormProps } from 'antd';
import { Button, Form, Input } from 'antd';
import {FieldType} from "./types.ts";
import {
    buttonsFormStyles,
    formStyles,
    LoginButtonStyle,
    LoginFormStyle, loginFormStyles,
    LoginH2Style,
    LoginSpanStyle,
    MainContainer
} from "./styles.ts";
import logo from '../../../assets/logo1.jpeg'
import {NavigateFunction, useNavigate} from "react-router-dom";



export const Login:React.FC = () => {
    const navigate:NavigateFunction = useNavigate()


    const onFinish: FormProps<FieldType>['onFinish'] = (values) => {
        console.log('Success:', values);
        navigate('/app/home')

    };

    const onFinishFailed: FormProps<FieldType>['onFinishFailed'] = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <>
            <MainContainer>
                <h2 style={LoginH2Style}>GESTÃO DE ACOLHIDOS</h2>
                <img alt={"Logo"} src={logo} style={{width: '15rem', margin: '1rem 1rem', borderRadius:"100% 100%"}}/>
                <div style={loginFormStyles}>
                <Form
                    name="basic"
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
                            style={formStyles}
                        />
                    </Form.Item>

                    <Form.Item<FieldType>
                        label="Senha"
                        name="password"
                        rules={[{required: true, message: 'Coloque sua senha!'}]}
                    >
                        <Input.Password
                            style={formStyles}
                        />
                    </Form.Item>

                    <Form.Item style={buttonsFormStyles}>
                        <Button type="primary" htmlType="submit" style={LoginButtonStyle}>
                            Entrar
                        </Button>
                    </Form.Item>
                </Form>
                </div>
            <span style={LoginSpanStyle}>Versão 1.0 - {new Date().getFullYear()}</span>
            </MainContainer>
        </>
    );
};
