import { Modal } from 'antd';
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export const Logout:React.FC = () => {
    const [logout, setLogout] = useState<boolean>(true);
    const navigate = useNavigate();

    
      const handleOk = () => {
        sessionStorage.clear();
        navigate('/');
        window.location.reload()
      };
    
      const handleCancel = () => {
        setLogout(false);
        navigate('/app/home');
      };

  return (
    <>
        <Modal title="Confirma a saída do sistema?" 
            open={logout} 
            onOk={handleOk} 
            onCancel={handleCancel} 
            cancelText={'Cancelar'} 
            okText={'Sair do Sistema'}
            />
    </>
  )
}
