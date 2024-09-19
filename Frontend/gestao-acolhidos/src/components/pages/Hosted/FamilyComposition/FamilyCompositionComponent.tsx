import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Divider, Form, FormProps, Space } from 'antd';
import { notifyError } from '../../../shared/PopMessage/PopMessage';
import { FamilyCompositionForm } from './FamilyCompForm';
import { FamilyTableForm } from './FamilyTable/FamilyTableForm';
import { updateFamilyCompositionDTO } from '../../../../entity/dto/Hosted/updateFamilyCompositionHostedDto';
import { updateFamilyTableDto } from '../../../../entity/dto/Hosted/updateFamilyTableHostDto';

export const FamilyCompositionComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [form] = Form.useForm();
    const [edit, setEdit] = useState<boolean>(false);

    const handleFamilyComposition:FormProps<FamilyCompositionForm>['onFinish'] = async(values:updateFamilyCompositionDTO) =>{
        console.log(values);
    }

    const handleFamilyTable:FormProps<FamilyTableForm>['onFinish'] = async(values:updateFamilyTableDto) =>{
        console.log(values);
    }



    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

    const handleSwitchChange = () => {
        setEdit(!edit);
    };

    const clearForm =()=>{
        form.resetFields()
    }

  return (
    <>
    <Divider>Composição Familiar</Divider>
    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
        
    </Space>
    </>

  )
}
