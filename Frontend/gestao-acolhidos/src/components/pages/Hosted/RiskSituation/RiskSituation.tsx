import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { useTableData } from '../../../../hooks/useTableData';
import { Button, Divider, Form, FormProps, Select, Space, Switch } from 'antd';
import { updateSituationalRiskHostedDto } from '../../../../entity/dto/Hosted/updateSituationalRiskHostedDto';
import { Homeless, Lookup, Migrant, SituationalRiskForm } from './types';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';

const hostedRepository = new HostedRepository()

export const RiskSituation:React.FC<{entity:Hosted}> = ({entity}) => {

    const {hostedEntity, setHostedEntity} = useTableData();

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    const lookUpOptions: { label: string; value: Lookup }[] = [
        { label: 'Formal (Com encaminhamento)', value: 'FORMAL' },
        { label: 'Espontânea', value: 'ESPONTANEA' },
        { label: 'Abordagem de rua (ronda)', value: 'ABORDAGEM_DE_RUA' },
    ];

    const migranteOptions: { label: string; value: Migrant }[] = [
        { label: 'Fixar Residência', value: 'FIXAR_RESIDENCIA' },
        { label: 'Passagem', value: 'PASSAGEM' },
        { label: 'Outro motivo', value: 'OUTRO' },
    ];

    const homelessOptions: { label: string; value: Homeless }[] = [
        { label: 'Pernoite', value: 'PERNOITE' },
        { label: 'Refeição', value: 'REFEICAO' },
        { label: 'Outro motivo', value: 'OUTRO' },
    ];

    useEffect(() => {
        if (entity) {
            setHostedEntity(entity);
        }
    }, [entity, setHostedEntity]);


    const handleSwitch = () => {
        setOpenEdit(!openEdit);
    };

    const handleSituationalRisk:FormProps<SituationalRiskForm>['onFinish'] = async(values:updateSituationalRiskHostedDto) =>{
        try {
            await hostedRepository.updateSituationalRisk(values, entity.id)
            .then(()=>{
                notifySuccess("Cadastro atualizado")
            });
            await hostedRepository.findById(entity.id)
            .then((e)=>{
                setHostedEntity(e);
            })
            handleSwitch()
        } catch (error) {
            errorOnFinish(error)
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

    const initialValues:SituationalRiskForm= {
        lookUp:" ",
        migrant:" ",
        homeless: " "
    }



  return (
    <>
        <Divider>Mapeamento de Situação de Risco</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Switch checked={openEdit} onClick={handleSwitch} unCheckedChildren="Editar" checkedChildren="Cancelar" />
            <Form
                form={form}
                onFinish={handleSituationalRisk}
                disabled={!openEdit}
                layout='vertical'
                clearOnDestroy
                initialValues={hostedEntity.situationalRisk ?{...hostedEntity.situationalRisk} :{initialValues}}
                >
                <Form.Item name={['lookUp']} label={'Tipo de procura'}>
                    {openEdit? (<>                    
                    <Select placeholder="Selecione o tipo de procura">
                        {lookUpOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                    </>):(
                        <>
                    <p style={{fontWeight:'bold'}}>{lookUpOptions.map((e)=> {
                        if(e.value === hostedEntity.situationalRisk?.lookUp){
                            return e.label
                        }
                    }) || initialValues.lookUp} </p>
                    </>
                )}
                </Form.Item>

                <Form.Item name={['migrant']} label={'É migrante?'} >
                    {openEdit? 
                    (<>
                    <Select placeholder="Motivo de estar no município">
                        {migranteOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                    </>):
                    (
                    <>
                    <p style={{fontWeight:'bold'}}>{migranteOptions.map((e)=> {
                        if(e.value === hostedEntity.situationalRisk?.migrant){
                            return e.label
                        }
                    }) || initialValues.migrant} </p>
                    </>
                    )    
                }
                </Form.Item>

                <Form.Item name={['homeless']} label={'É população de rua?'}>
                    {openEdit? (<>
                    <Select placeholder="Motivo pelo qual procurou o albergue">
                        {homelessOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                    </>):(<>
                    <p style={{fontWeight:'bold'}}>{homelessOptions.map((e)=> {
                        if(e.value === hostedEntity.situationalRisk?.homeless){
                            return e.label
                        }
                    }) || initialValues.homeless} </p>
                    </>)}

                </Form.Item>

                <Button htmlType='submit' type='primary'>Salvar</Button>
            </Form>
        </Space>
    </>
  )
}
