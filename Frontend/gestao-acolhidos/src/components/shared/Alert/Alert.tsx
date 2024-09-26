import { notification } from 'antd';
import React, { useEffect } from 'react'

export const Alert:React.FC<{expired:boolean}> = ({expired}) => {

const [api, contextHolder] = notification.useNotification();

  const openNotification = () => {
    api.info({
      message: `Sessão Expirada`,
      description:
        'Realize o login novamente',
      placement:'top',
      onClick() {
            sessionStorage.clear()
            window.location.reload()
      },
      onClose() {
            sessionStorage.clear()
            window.location.reload()
      },
    });
  };

  useEffect(()=>{
    if(expired)openNotification()
  },[expired])

  return (
        <>{contextHolder}</>
  )
}
