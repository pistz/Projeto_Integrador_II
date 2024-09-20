import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { useTableData } from '../../../../hooks/useTableData';
import { Button, Divider, Form, FormProps, Select, Space, Switch } from 'antd';
import { updateSituationalRiskHostedDto } from '../../../../entity/dto/Hosted/updateSituationalRiskHostedDto';
import { Homeless, Lookup, Migrant, SituationalRiskForm } from './types';

export const RiskSituation:React.FC<{entity:Hosted}> = ({entity}) => {

    const {hostedEntity, setHostedEntity} = useTableData();
    const [localHostedEntity, setLocalHostedEntity] = useState<Hosted>(entity);

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
            setLocalHostedEntity(entity);
        }
    }, [entity]);

    useEffect(() => {
        setHostedEntity(localHostedEntity);
    }, [localHostedEntity, setHostedEntity]);

    const handleSwitch = () => {
        setOpenEdit(!openEdit);
    };

    const handleSituationalRisk:FormProps<SituationalRiskForm>['onFinish'] = async(values:updateSituationalRiskHostedDto) =>{
        console.log(values)
        handleSwitch()
    }



  return (
    <>
        <Divider>Mapeamento de Situação de Risco</Divider>
        <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
            <Switch checked={openEdit} onClick={handleSwitch} unCheckedChildren="Editar" checkedChildren="Cancelar" />
            <Form
                form={form}
                initialValues={{...hostedEntity.situationalRisk}}
                onFinish={handleSituationalRisk}
                disabled={!openEdit}
                layout='vertical'
            >
                <Form.Item name={['lookUp']} label={'Tipo de procura'}>
                    <Select placeholder="Selecione o tipo de procura">
                        {lookUpOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name={['migrant']} label={'É migrante?'}>
                    <Select placeholder="Motivo de estar no município">
                        {migranteOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name={['homeless']} label={'É população de rua?'}>
                    <Select placeholder="Motivo pelo qual procurou o albergue">
                        {homelessOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                    </Select>
                </Form.Item>

                <Button htmlType='submit'>Salvar</Button>
            </Form>
        </Space>
    </>
  )
}
