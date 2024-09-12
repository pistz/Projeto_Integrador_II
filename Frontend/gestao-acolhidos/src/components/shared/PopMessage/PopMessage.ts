import { message } from 'antd';


export const notifySuccess = (msg : string) => {
    message.success(msg, 2);
}

export const notifyError = (error : string) =>{
    message.error(error, 2)
}