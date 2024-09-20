import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Form, FormProps, Input, InputNumber, Radio, Space, Switch } from 'antd'
import { useTableData } from '../../../../hooks/useTableData';
import { SocialProgramsForm } from './types';
import { updateSocialProgramHostedDto } from '../../../../entity/dto/Hosted/updateSocialProgramHostedDto';

export const SocialProgramComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const {hostedEntity, setHostedEntity} = useTableData();

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    useEffect(() => {
        if (entity) {
            setHostedEntity(entity);
        }
    }, [entity, setHostedEntity]);

    const handleSocialPrograms:FormProps<SocialProgramsForm>['onFinish']=async(values:updateSocialProgramHostedDto)=>{
        console.log(values)
        handleSwitch();
    }

    const handleSwitch = () => {
        setOpenEdit(!openEdit);
    };


  return (
    <>
        <Divider>Programas Sociais</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Switch checked={openEdit} onClick={handleSwitch} unCheckedChildren="Editar" checkedChildren="Cancelar" />
            
            <Form
                form={form}
                initialValues={hostedEntity.socialPrograms? {...hostedEntity.socialPrograms} : {...{} as SocialProgramsForm}}
                onFinish={handleSocialPrograms}
                disabled={!openEdit}
                layout='vertical'
                clearOnDestroy
            >
                <Divider>Municipal</Divider>
                <Space direction='horizontal'>
                    <Form.Item name={['hasPasseDeficiente']} label={'Passe Deficiente'}>
                        <Radio value={true}></Radio>
                    </Form.Item>

                    <Form.Item name={['hasPasseIdoso']} label={'Passe Idoso'}>
                        <Radio value={true}></Radio>
                    </Form.Item>
                </Space>

                <Divider>Estadual</Divider>
                <Space direction='horizontal'>

                    <Form.Item name={['hasRendaCidada']} label={'Renda Cidadã'}>
                        <Radio value={true}></Radio>
                    </Form.Item>

                    <Form.Item name={['hasAcaoJovem']} label={'Ação Jovem'}>
                        <Radio value={true}></Radio>
                    </Form.Item>

                    <Form.Item name={['hasVivaLeite']} label={'Viva Leite'}>
                        <Radio value={true}></Radio>
                    </Form.Item>
                </Space>

                <Divider>Federal</Divider>
                <Space direction='horizontal'>

                    <Form.Item name={['hasBPC_LOAS']} label={'BPC LOAS'}>
                        <Radio value={true}></Radio>
                    </Form.Item>

                    <Form.Item name={['hasBolsaFamilia']} label={'Bolsa Família'}>
                        <Radio value={true}></Radio>
                    </Form.Item>

                    <Form.Item name={['hasPETI']} label={'PETI'}>
                        <Radio value={true}></Radio>
                    </Form.Item>                  
                </Space>
                <Divider>Possui outro benefício?</Divider>
                    <Form.Item name={['others']} label={'Descreva o benefício'}>
                        <Input type='text'/>
                    </Form.Item>

                    <Form.Item name={['howLong']} label={'Há quanto tempo recebe benefício (em meses)?'}>
                        <InputNumber />
                    </Form.Item>

                    <Form.Item name={['wage']} label={'Valor recebido em R$'}>
                        <InputNumber />
                    </Form.Item>


                <Divider variant='dotted'></Divider>
                <Button htmlType='submit' type='primary'>Salvar</Button>
            </Form>
        </Space>
    </>
  )
}
