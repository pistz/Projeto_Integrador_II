import React from 'react'
import { hostedInputForm, mainDivStyle } from '../styles'
import { Button, DatePicker, Form, FormProps, Input, Select, Space } from 'antd'
import { CreateForm } from './types'
import locale from 'antd/es/date-picker/locale/pt_BR';
import dayjs from 'dayjs';
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { createHostedDto } from '../../../../entity/dto/Hosted/createHostedDto';


const hostedRepository = new HostedRepository();

export const CreateHosted:React.FC = () => {

  const [form] = Form.useForm();

  const brazilStates = [
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 
    'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 
    'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
  ];

  const inputFormat = 'DD/MM/YYYY'
  const dateFormat = 'YYYY-MM-DD';

  function capitalizeFirstLetter(string:string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
  }

  const clearForm =()=>{
    form.resetFields()
  }

  
  const onFinish:FormProps<CreateForm>['onFinish'] = async (values:createHostedDto) =>{
    values.firstName = capitalizeFirstLetter(values.firstName);
    values.lastName = capitalizeFirstLetter(values.lastName);
    if(values.birthDay){
      values.birthDay = handleDateChange(values.birthDay)
    }
    try {
      await hostedRepository.create(values)
      .then(()=>{
        notifySuccess("Cadastro realizado")
      });
      clearForm();
    } catch (error) {
      errorOnFinish(error)
    }
  }

  const errorOnFinish = (error:unknown) =>{
    notifyError("Erro ao realizar cadastro");
    return error;
}

  const handleDateChange = (date:string) => {
    const formattedDate = date ? dayjs(date).format(dateFormat) : '';
    return formattedDate
  };

  return (
    <>
    <div style={mainDivStyle}>
     <Space></Space>
      <Form
        form={form}
        onFinish={onFinish}
        style={{marginTop:'2rem'}}
        layout='vertical'
        clearOnDestroy={true}
      >
        <h2 style={{padding:'1rem'}}>Cadastro de Novo Acolhido</h2>
        <Form.Item name={['firstName']} label="Nome" 
            rules={[{ required: true, message:"Nome é um campo obrigatório" }]}>
          <Input style={hostedInputForm}/>
        </Form.Item>

        <Form.Item name={['lastName']} label="Sobrenome" 
            rules={[{ required: true, message:"Sobrenome é um campo obrigatório" }]}>
          <Input style={hostedInputForm}/>
        </Form.Item>

        <Form.Item name={['socialSecurityNumber']} label="CPF" 
            rules={[{ required: true , message:"CPF é um campo obrigatório"}, 
                {pattern:/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, message: 'Formato inválido. Use o formato 000.000.000-00' }]}>
          <Input style={hostedInputForm}  placeholder="000.000.000-00"/>
        </Form.Item>

        <Form.Item name={['birthDay']} label="Data de Nascimento" 
            rules={[{ required: false}]}>
            <DatePicker placeholder='Selecione a data' 
            locale={locale} 
            format={inputFormat}
            onChange={handleDateChange}
            />
        </Form.Item>

        <Form.Item name={['paperTrail']} label="Prontuário nº" 
            rules={[{ required: false}]}>
          <Input style={hostedInputForm} type='number'/>
        </Form.Item>

        <Form.Item name={['fathersName']} label="Nome completo do pai" 
            rules={[{ required: false}]}>
          <Input style={hostedInputForm} type='text'/>
        </Form.Item>

        <Form.Item name={['mothersName']} label="Nome completo da mãe" 
            rules={[{ required: false}]}>
          <Input style={hostedInputForm} type='text'/>
        </Form.Item>

        <Form.Item name={['occupation']} label="Profissão" 
            rules={[{ required: false}]}>
          <Input style={hostedInputForm} type='text'/>
        </Form.Item>

        <Form.Item name={['cityOrigin']} label="Cidade de Nascimento" 
            rules={[{ required: false}]}>
          <Input style={hostedInputForm} type='text'/>
        </Form.Item>

        <Form.Item name={['stateOrigin']} label="Estado" 
            rules={[{ required: false}]}>
          <Select>
            {brazilStates.map(state => (
              <Select.Option key={state} value={state}>
                {state}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <div style={{display:'flex', flexDirection:'row', alignItems:'stretch', justifyContent:'space-around', marginBottom:'3rem'}}>
        <Button type='primary' htmlType='submit'>Salvar</Button>
        <Button htmlType='reset'> Cancelar</Button>
        </div>
      </Form>
      <Space></Space>
    </div>
    </>
  )
}
