import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, FormProps, Space, Switch } from 'antd';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { MedicalRecordForm } from './types';
import { createMedicalRecordDto } from '../../../../entity/dto/Hosted/createMedicalRecordHostedDto';
import { MedicalRecordTable } from './MedicalRecordsTable/MedicalRecordTable';
import TextArea from 'antd/es/input/TextArea';
import { CheckOutlined } from '@ant-design/icons';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';

const hostedRepository = new HostedRepository();

export const MedicalRecordComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [hostedEntity, setHostedEntity] = useState<Hosted>(entity);

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    const handleOnFinish:FormProps<MedicalRecordForm>['onFinish'] = async(values:createMedicalRecordDto) =>{
        try {
            await hostedRepository.updateMedicalRecord(values, entity.id)
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
        <Divider>Saúde do Acolhido</Divider>
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
                    <Form.Item name={['complaints']} label={'Nova entrada'}>
                        <TextArea size='large' style={{width:'30rem'}}/>
                    </Form.Item>

                    <Button htmlType='submit' type='primary' icon={<CheckOutlined />}>Salvar</Button>

                </Form>            
            </> 
            : 
            <></>
            }

            <Divider>Histórico</Divider>
            {hostedEntity.medicalRecord? <MedicalRecordTable entity={hostedEntity}/>:<></>}
        </Space>

    </>
  )
}
