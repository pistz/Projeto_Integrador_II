import React, { useEffect, useState } from 'react';
import type { FormProps } from 'antd';
import { Button, Form, Input } from 'antd';
import {FieldType} from "./types.ts";
import {
    formStyles,
    LoginButtonStyle,
    LoginFormStyle, loginFormStyles,
    LoginSpanStyle,
    MainContainer
} from "./styles.ts";
import logo from '../../../assets/login-img.png'
import {NavigateFunction, useNavigate} from "react-router-dom";
import { useAuth } from '../../../hooks/useAuth.ts';
import { AuthRepository } from '../../../repository/Auth/AuthRepository.ts';
import { UserLoginDTO } from '../../../entity/dto/User/UserLoginDTO.ts';
import { notifyError } from '../../shared/PopMessage/PopMessage.ts';
import { getTokenFromSessionStorage, getTokenId } from '../../../services/Token.ts';


const authenticate = new AuthRepository();
const tokenId:string = getTokenId();

export const Login:React.FC = () => {

    const [sessionToken, setSessionToken] = useState<string>();
    const [loading, setLoading] = useState<boolean>(false)

    const [form] = Form.useForm();

    const navigate:NavigateFunction = useNavigate()

    const {setSigned, setUserRole} = useAuth();


    const onFinish: FormProps<FieldType>['onFinish'] = async (values:UserLoginDTO) => {
        setLoading(true)
        if(!values){
            notifyError("Usuário ou senha inválidos");
            setLoading(false)
        }
        try {
            await authenticate.authenticateUser(values)
                .then((e)=>{
                    sessionStorage.setItem(tokenId,JSON.stringify(e));
                    setSessionToken(e.toString())
                });
        } catch (error) {
            errorOnFinish(error)
        }
    };

    const errorOnFinish = (error:unknown) =>{
        setLoading(false)
        notifyError("Usuário ou senha inválidos");
        return error;
    }


    useEffect(()=>{        
        const authenticateLogin = async () =>{
            const token = getTokenFromSessionStorage();
            if(!token){
                navigate('/login')
                setSigned(false)
                setLoading(false)
                sessionStorage.clear();
                return;
            }
            try {
                const sessionToken:string = String(token)
                await authenticate.getRoleFromToken(sessionToken)
                .then((res)=>{
                    const role = String(res);
                    setUserRole(role)
                    setSigned(true)
                    navigate('/app/home');
                });
            } catch (error) {
                console.error(error)
                setSigned(false)
                sessionStorage.clear()
                errorOnFinish(error)
                navigate('/login')
            }
        }
        authenticateLogin()
    },[sessionToken])

    return (
        <>
            <MainContainer>
                <img alt={"Logo"} src={logo} style={{width: '20rem', margin: '1rem 1rem', borderRadius:"3rem 1rem"}}/>
                <div style={loginFormStyles}>
                <Form
                    name="login"
                    style={LoginFormStyle}
                    initialValues={{remember: false}}
                    onFinish={onFinish}
                    autoComplete="off"
                    layout='vertical'
                    form={form}
                >
                    <Form.Item
                        label="E-mail"
                        name={['email']}
                        rules={[{required: true, message: 'Coloque seu e-mail!'}]}
                    >
                        <Input
                            type="email"
                            style={formStyles}
                        />
                    </Form.Item>

                    <Form.Item
                        label="Senha"
                        name={['password']}
                        rules={[{required: true, message: 'Coloque sua senha!'}]}
                    >
                        <Input.Password
                            style={formStyles}
                        />
                    </Form.Item>

                    <Button type="primary" htmlType="submit" style={LoginButtonStyle} loading={loading}>
                        Entrar
                    </Button>
                </Form>
                </div>
            <span style={LoginSpanStyle}>Versão 1.0 - {new Date().getFullYear()}</span>
            </MainContainer>
        </>
    );
};
