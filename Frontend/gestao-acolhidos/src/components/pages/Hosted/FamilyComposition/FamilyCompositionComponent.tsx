import React, { useCallback, useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Collapse, Divider, Form, FormProps, Input, InputNumber, Radio, Select, Space, Switch } from 'antd';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { FamilyCompositionForm } from './FamilyCompForm';
import { updateFamilyCompositionDTO } from '../../../../entity/dto/Hosted/updateFamilyCompositionHostedDto';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { FamilyTable } from '../../../../entity/Hosted/FamilyTable/FamilyTable';
import dayjs from 'dayjs';
import { Education, FamilyTableForm, Gender, MaritalStatus } from './FamilyTable/FamilyTableForm';
import { updateFamilyTableDto } from '../../../../entity/dto/Hosted/updateFamilyTableHostDto';

const hostedRepository = new HostedRepository()

export const FamilyCompositionComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const genderOptions: { label: string; value: Gender }[] = [
        { label: 'Homem', value: 'MALE' },
        { label: 'Mulher', value: 'FEMALE' },
        { label: 'Outro', value: 'OTHER' },
    ];

    const educationOptions: { label: string; value: Education }[] = [
        { label: 'Analfabeto', value: 'ANALFABETO' },
        { label: 'Ensino Básico', value: 'ENSINO_BASICO' },
        { label: 'Ensino Médio', value: 'ENSINO_MEDIO' },
        { label: 'Ensino Superior', value: 'ENSINO_SUPERIOR' },
        { label: 'Pós Graduação', value: 'POS_GRADUACAO' },
    ];

    const maritalStatusOptions: { label: string; value: MaritalStatus }[] = [
        { label: 'Casado(a)', value: 'CASADO' },
        { label: 'Solteiro(a)', value: 'SOLTEIRO' },
        { label: 'Viúvo(a)', value: 'VIUVO' },
        { label: 'Divorciado(a)', value: 'DIVORCIADO' },
        { label: 'Separado(a)', value: 'SEPARADO' },
        { label: 'União Estável', value: 'UNIAO_ESTAVEL' },
    ];

    const [compositionForm] = Form.useForm();
    const [editComposition, setEditComposition] = useState<boolean>(false);

    const [memberForm] = Form.useForm();

    const [editMember, setEditMember] = useState<boolean>(false);


    const [familyMembers, setFamilyMembers] = useState<FamilyTable[]>(entity.familyTable);

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

    const handleFamilyTable:FormProps<FamilyTableForm>['onFinish'] = async(values:updateFamilyTableDto) =>{
        try {
            await hostedRepository.updateFamilyTable(values,entity.id)
                .then(()=>{
                    notifySuccess("Registro Incluído");
                });
                handleSwitchChangeFamilyMember();
        } catch (error) {
            errorOnFinish(error)
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar cadastro");
        return error;
    }

    const handleSwitchChangeComposition = () => {
        setEditComposition(!editComposition);
    };

    const handleSwitchChangeFamilyMember = () => {
        setEditMember(!editMember);
        clearForm()
    };

    const clearForm =()=>{
        memberForm.resetFields()
    }

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };

    const updateData = useCallback(async()=>{
        const data = await hostedRepository.findById(entity.id)
        setFamilyMembers(data.familyTable)
    },[entity.id])

    useEffect(()=>{
        const loadEntityData = async()=>{
            updateData()
        }
        loadEntityData();
    },[updateData])

    const initialComposition:updateFamilyCompositionDTO = {
        hasFamily:false,
        hasFamilyBond:false
    }

    const initialMember:updateFamilyTableDto = {
        name:'',
        age:0,
        gender:'OTHER',
        education:'ANALFABETO',
        maritalStatus:'SOLTEIRO',
        occupation:''
    }
//TODO - verificar erro ao cadastrar familiar
  return (
    <>
    <Divider>Composição Familiar</Divider>
    <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem', alignItems:'center', justifyContent:'center'}}>
        <Switch checked={editComposition} onClick={handleSwitchChangeComposition} unCheckedChildren="Editar" checkedChildren="Cancelar" />
        <Form
            form={compositionForm}
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

        <Divider>Incluir membro da Família</Divider>

        {entity.familyComposition === null ? <></> :
            entity.familyComposition.hasFamily || entity.familyComposition.hasFamilyBond ? 
            <>
                <Switch checked={editMember} onClick={handleSwitchChangeFamilyMember} unCheckedChildren="Incluir" checkedChildren="Cancelar" />
                {editMember? 
                <> 
                <Form
                    form={memberForm}
                    onFinish={handleFamilyTable}
                    disabled={!editMember}
                    clearOnDestroy={true}
                    initialValues={initialMember}
                    layout='vertical'
                >
                    <Form.Item name={['name']} label={'Nome'} rules={[{required:true, message:'Deve possuir nome'}]}>
                        <Input value={initialMember.name}/>
                    </Form.Item>

                    <Form.Item name={['age']} label={'Idade'} >
                        <InputNumber />
                    </Form.Item>

                    <Form.Item name={['gender']} label={'Gênero'} >
                        <Select placeholder="Selecione o gênero">
                        {genderOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                        </Select>
                    </Form.Item>

                    <Form.Item name={['maritalStatus']} label={'Estado Civil'} >
                        <Select placeholder="Selecione o estado civil">
                        {maritalStatusOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                        </Select>
                    </Form.Item>

                    <Form.Item name={['education']} label={'Escolaridade'} >
                        <Select placeholder="Selecione a escolaridade">
                        {educationOptions.map((option) => (
                            <Select.Option key={option.value} value={option.value}>
                                {option.label}
                            </Select.Option>
                        ))}
                        </Select>
                    </Form.Item>

                    <Form.Item name={['occupation']} label={'Profissão'} >
                        <Input value={initialMember.occupation}/>
                    </Form.Item>

                    <Button htmlType='submit' type='primary'>Salvar</Button>
                </Form>
                </> 
                : 
                <></>}
            </>
            :
            <></>
        }


        {familyMembers.map((member) =>{
                return (
                <>
                    <Collapse accordion 
                        style={{width:'30rem'}}
                        key={member.id}
                        items={[{key:member.id, 
                        label:member.name, 
                        children:(<>
                        <table >
                            <ul><strong>Nome: </strong>{member.name}</ul>
                            <ul><strong>Idade: </strong>{member.age}</ul>
                            <ul><strong>Sexo: </strong>{member.gender}</ul>
                            <ul><strong>Estado Civil: </strong>{member.maritalStatus}</ul>
                            <ul><strong>Escolaridade: </strong>{member.education}</ul>
                            <ul><strong>Profissão: </strong>{member.occupation}</ul>
                            <ul><strong>Registrado em: </strong>{changeDateFormatVisualization(member.updatedAt)}</ul>
                        </table>
                        </>)}]}/>
                </>
                )
            })}

    </Space>
    </>

  )
}
