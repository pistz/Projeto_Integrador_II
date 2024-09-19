import React, { useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, FormProps, Radio, Space, Switch } from 'antd';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { FamilyCompositionForm } from './FamilyCompForm';
// import { FamilyTableForm } from './FamilyTable/FamilyTableForm';
import { updateFamilyCompositionDTO } from '../../../../entity/dto/Hosted/updateFamilyCompositionHostedDto';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
// import { updateFamilyTableDto } from '../../../../entity/dto/Hosted/updateFamilyTableHostDto';

const hostedRepository = new HostedRepository()

export const FamilyCompositionComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [form] = Form.useForm();
    const [editComposition, setEditComposition] = useState<boolean>(false);

    const handleFamilyComposition:FormProps<FamilyCompositionForm>['onFinish'] = async(values:updateFamilyCompositionDTO) =>{
        try {
            await hostedRepository.updateFamilyComposition(values, entity.id)
            .then(()=>{
                notifySuccess("Registro Atualizado");
            })
            handleSwitchChangeComposition();
        } catch (error) {
            errorOnFinish(error)
        }
    }

    // const handleFamilyTable:FormProps<FamilyTableForm>['onFinish'] = async(values:updateFamilyTableDto) =>{
    //     console.log(values);
    // }



    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

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
        <Switch checked={editComposition} onClick={handleSwitchChangeComposition} unCheckedChildren="Editar" checkedChildren="Cancelar" />
        <Form
            form={form}
            onFinish={handleFamilyComposition}
            disabled={!editComposition}
            initialValues={entity.familyComposition ? entity.familyComposition : initialComposition}
        >
            <Form.Item name={['hasFamily']} label={'Possui Família?'}>
                <Radio.Group name='hasFamily' defaultValue={entity.familyComposition ? entity.familyComposition.hasFamily:initialComposition.hasFamily}>
                    <Radio value={true}>sim</Radio>
                    <Radio value={false}>não</Radio>
                </Radio.Group>
            </Form.Item>

            <Form.Item name={['hasFamilyBond']} label={'Possui Vínculo Familiar?'}>
            <Radio.Group name='hasFamilyBond' defaultValue={entity.familyComposition ? entity.familyComposition.hasFamilyBond:initialComposition.hasFamilyBond}>
                    <Radio value={true}>sim</Radio>
                    <Radio value={false}>não</Radio>
                </Radio.Group>
            </Form.Item>

            <Button htmlType='submit' type='primary'>Salvar</Button>
        </Form>
    </Space>
    </>

  )
}
