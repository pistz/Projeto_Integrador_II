import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, FormProps, Input, InputNumber, Radio, Space, Switch } from 'antd'
import { SocialProgramsForm } from './types';
import { updateSocialProgramHostedDto } from '../../../../entity/dto/Hosted/updateSocialProgramHostedDto';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';

const hostedRepository = new HostedRepository()

export const SocialProgramComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const [hostedEntity, setHostedEntity] = useState<Hosted>(entity)

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    useEffect(() => {
        const update = async (id:string)=>{
            await hostedRepository.findById(id)
            .then(e => setHostedEntity(e))
        }
        if (entity) {
            update(entity.id)
        }
    }, [entity, setHostedEntity]);

    const handleSocialPrograms:FormProps<SocialProgramsForm>['onFinish']=async(values:updateSocialProgramHostedDto)=>{
        const newValue = values.wage.toString().replace(',', '.')
        values.wage = Number(newValue)
        try {
            await hostedRepository.updateSocialPrograms(values, entity.id)
            .then(()=>{
                notifySuccess("Registro Atualizado")
            });
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

    const initialValues:SocialProgramsForm = {
        hasAcaoJovem:false,
        hasBolsaFamilia:false,
        hasBPC_LOAS:false,
        hasPasseDeficiente:false,
        hasPasseIdoso:false,
        hasPETI:false,
        hasRendaCidada:false,
        hasVivaLeite:false,
        howLong:0,
        others:"",
        wage:0,
    }

  return (
    <>
        <Divider>Programas Sociais</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Switch checked={openEdit} onClick={handleSwitch} unCheckedChildren="Editar" checkedChildren="Cancelar" />
            
            <Form
                form={form}
                initialValues={hostedEntity?.socialPrograms !== null ? hostedEntity?.socialPrograms : initialValues}
                onFinish={handleSocialPrograms}
                disabled={!openEdit}
                layout='vertical'
            >
                <Divider>Municipal</Divider>
                <Space direction='horizontal'>
                    <Form.Item name={['hasPasseDeficiente']} label={'Passe Deficiente'} style={{margin:'0 6rem'}}>
                        <Radio.Group name='hasPasseDeficiente'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item name={['hasPasseIdoso']} label={'Passe Idoso'}  style={{margin:'0 6rem'}}>
                        <Radio.Group name='hasPasseIdoso'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>
                </Space>

                <Divider>Estadual</Divider>
                <Space direction='horizontal'>

                    <Form.Item name={['hasRendaCidada']} label={'Renda Cidadã'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasRendaCidada'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item name={['hasAcaoJovem']} label={'Ação Jovem'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasAcaoJovem'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item name={['hasVivaLeite']} label={'Viva Leite'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasVivaLeite'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>
                </Space>

                <Divider>Federal</Divider>
                <Space direction='horizontal'>

                    <Form.Item name={['hasBPC_LOAS']} label={'BPC LOAS'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasBPC_LOAS'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item name={['hasBolsaFamilia']} label={'Bolsa Família'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasBolsaFamilia'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item name={['hasPETI']} label={'PETI'} style={{margin:'0 3rem'}}>
                        <Radio.Group name='hasPETI'>
                            <Radio value={true}>sim</Radio>
                            <Radio value={false}>não</Radio>
                        </Radio.Group>
                    </Form.Item>                  
                </Space>
                <Divider>Possui outro benefício?</Divider>
                    <Form.Item name={['others']} label={'Outro: descreva o benefício'}>
                        <Input type='text'/>
                    </Form.Item>

                    <Form.Item name={['howLong']} label={'Há quanto tempo recebe benefício (em meses)?'}>
                        <InputNumber />
                    </Form.Item>

                    <Form.Item name={['wage']} label={'Valor do benefício'} rules={[{pattern: /^\d+(,\d{1,2})?$/, message:'Formato decimal aceito: 0000,00'}]}>
                        <Input style={{width:'13rem'}} prefix="R$"/>
                    </Form.Item>


                <Divider variant='dotted'></Divider>
                <Button htmlType='submit' type='primary'>Salvar</Button>
            </Form>
        </Space>
    </>
  )
}
