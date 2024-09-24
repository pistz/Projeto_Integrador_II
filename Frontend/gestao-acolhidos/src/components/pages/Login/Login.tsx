import React, { useEffect, useState } from 'react';
import type { FormProps } from 'antd';
import { Button, Form, Input } from 'antd';
import {FieldType} from "./types.ts";
import {
    buttonsFormStyles,
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
import { getTokenFromLocalStorage, getTokenId } from '../../../services/Token.ts';


const authenticate = new AuthRepository();
const tokenId:string = getTokenId();

export const Login:React.FC = () => {

    const [sessionToken, setSessionToken] = useState<string>();

    const navigate:NavigateFunction = useNavigate()

    const {setSigned, setUserRole} = useAuth();


    const onFinish: FormProps<FieldType>['onFinish'] = async (values) => {
        const body:UserLoginDTO = {
            email:"",
            password:""
        }
        if(values){
            body.email=values.email;
            body.password=values.password
        }
        try {
            await authenticate.authenticateUser(body)
                .then((e)=>{
                    sessionStorage.setItem(tokenId,JSON.stringify(e));
                    setSessionToken(e.toString())
                });
        } catch (error) {
            errorOnFinish(error)
        }
    };

    const errorOnFinish = (error:unknown) =>{
        notifyError("Usuário ou senha inválidos");
        return error;
    }


    useEffect(()=>{        
        const authenticateLogin = async () =>{
            const token = getTokenFromLocalStorage();
            if(!token){
                navigate('/login')
                setSigned(false)
                sessionStorage.clear();
                return;
            }
            try {
                await authenticate.getRoleFromToken(token)
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
