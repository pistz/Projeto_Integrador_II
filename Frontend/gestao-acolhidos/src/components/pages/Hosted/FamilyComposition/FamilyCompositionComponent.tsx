import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Divider, Form, FormProps, Radio, Space, Switch } from 'antd';
// import { notifyError } from '../../../shared/PopMessage/PopMessage';
import { FamilyCompositionForm } from './FamilyCompForm';
// import { FamilyTableForm } from './FamilyTable/FamilyTableForm';
import { updateFamilyCompositionDTO } from '../../../../entity/dto/Hosted/updateFamilyCompositionHostedDto';
import { FamilyComposition } from '../../../../entity/Hosted/FamilyComposition/FamilyComposition';
// import { updateFamilyTableDto } from '../../../../entity/dto/Hosted/updateFamilyTableHostDto';

export const FamilyCompositionComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [form] = Form.useForm();
    const [editComposition, setEditComposition] = useState<boolean>(false);

    const handleFamilyComposition:FormProps<FamilyCompositionForm>['onFinish'] = async(values:updateFamilyCompositionDTO) =>{
        console.log(values);

    }

    // const handleFamilyTable:FormProps<FamilyTableForm>['onFinish'] = async(values:updateFamilyTableDto) =>{
    //     console.log(values);
    // }



    // const errorOnFinish = (error:unknown) =>{
    //     notifyError("Erro ao realizar cadastro");
    //     return error;
    // }

    const handleSwitchChangeComposition = () => {
        setEditComposition(!editComposition);
    };

    // const clearForm =()=>{
    //     form.resetFields()
    // }

    const initialComposition:updateFamilyCompositionDTO = {
        hasFamily:false,
        hasFamilyBond:false
    }

  return (
    <>
    <Divider>Composição Familiar</Divider>
    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
        <Switch checked={editComposition} onClick={handleSwitchChangeComposition} unCheckedChildren="Incluir" checkedChildren="Fechar" />
        <Form
            form={form}
            onFinish={handleFamilyComposition}
            disabled={!editComposition}
            initialValues={entity.familyComposition ? entity.familyComposition : initialComposition}
        >
            <Form.Item name={['hasFamily']} label={'Possui Família?'}>
                <Radio value={true}>sim</Radio>
                <Radio value={false}>não</Radio>
            </Form.Item>
        </Form>
    </Space>
    </>

  )
}
