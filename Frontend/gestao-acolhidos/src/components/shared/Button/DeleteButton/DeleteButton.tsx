import React from 'react';
import { DeleteOutlined, QuestionCircleOutlined } from '@ant-design/icons';
import { Button, Popconfirm } from 'antd';
import { IRemoveButtonProps } from './types';


export const DeleteButton:React.FC<IRemoveButtonProps> = ({removeMethod}:IRemoveButtonProps) => {
    
    return (
        <Popconfirm
            title="Remover Registro"
            description="Deseja realmente remover este registro?"
            onConfirm={removeMethod}
            icon={<QuestionCircleOutlined />}
        >
            <Button danger ghost  icon={<DeleteOutlined/> }>Remover</Button>
        </Popconfirm>)
}