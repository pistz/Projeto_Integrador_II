import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, FormProps, Space, Switch } from 'antd';
import { TreatmentsForm } from './types';
import { updateCustomTreatmentsDto } from '../../../../entity/dto/Hosted/updateCustomTreatmentsHostedDto';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import TextArea from 'antd/es/input/TextArea';
import { CheckOutlined } from '@ant-design/icons';
import { TreatmentsTable } from './TreatmentsTable/TreatmentsTable';

const hostedRepository = new HostedRepository();

export const TreatmentsComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [hostedEntity, setHostedEntity] = useState<Hosted>(entity);

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    const handleOnFinish:FormProps<TreatmentsForm>['onFinish'] = async(values:updateCustomTreatmentsDto) =>{
        try {
            await hostedRepository.updateCustomTreatments(values, entity.id)
            .then(() => {
                notifySuccess("Registro Atualizado");
            });
            const newData = await hostedRepository.findById(hostedEntity.id)
            setHostedEntity(newData)
            handleSwitch();
        } catch (error) {
            errorOnFinish(error)
        }
    }
    
    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

    const handleSwitch = () => {
        setOpenEdit(!openEdit);
    };


    useEffect(() => {
        const update = async ()=>{
            if (entity.id) {
                const data = await hostedRepository.findById(entity.id)
                setHostedEntity(data)
            }
        }
        update()
    }, [entity, setHostedEntity, entity.medicalRecord]);

  return (
    <>
    <Divider> Plano Personalizado de Atendimento</Divider>
    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
        <Switch checked={openEdit} onClick={handleSwitch} unCheckedChildren="Incluir" checkedChildren="Cancelar" />
        {openEdit? 
        <>
            <Form
                form={form}
                onFinish={handleOnFinish}
                layout='vertical'
                clearOnDestroy
            
            >
                <br></br>
                <Form.Item name={['procedure']} label={'Procedimentos Adotados'}>
                    <TextArea size='large' style={{width:'30rem'}}/>
                </Form.Item>

                <Button htmlType='submit' type='primary' icon={<CheckOutlined />}>Salvar</Button>

            </Form>            
        </> 
        : 
        <></>
        }

        <Divider>Histórico do Usuário</Divider>
        {hostedEntity.customTreatments? <TreatmentsTable entity={hostedEntity}/>:<></>}
    </Space>

</>
  )
}
