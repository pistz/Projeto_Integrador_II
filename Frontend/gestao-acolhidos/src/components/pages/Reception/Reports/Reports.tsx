import { CheckOutlined } from '@ant-design/icons'
import { Button, Divider, InputNumber, Space, Form, FormProps} from 'antd'
import React from 'react'
import { queryReceptionDto } from '../../../../entity/dto/Reception/queryReceptionDto';
import { ReceptionRepository } from '../../../../repository/Reception/ReceptionRepository';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';

const CURRENT_YEAR = new Date().getFullYear();
const MINIMUM_YEAR = 1970; // Ano de fundação da instituição

const receptionRepository = new ReceptionRepository();

export const Reports:React.FC = () => {
    const [formMonthAndYear] = Form.useForm();
    const [formYear] = Form.useForm();


    const handleQuery:FormProps<queryReceptionDto>['onFinish'] = async (values:queryReceptionDto) =>{
        try {
            if(values.month){
                await receptionRepository.findByMonthAndYear(values)
                .then((e)=> console.log(e));
                notifySuccess("Relatório Mensal Emitido")
            }else{
                await receptionRepository.findByYear(values)
                .then((e)=> console.log(e));
                notifySuccess("Relatório Anual Emitido")
            }

        } catch (error) {
            errorOnFinish(error)
        }
    }



    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao realizar busca");
        return error;
    }

  return (
    <>
        <Divider>Relatórios de Acolhimento</Divider>
        
        <Space align='center' style={{display:'flex', flexDirection:'column'}}>
            <Divider>Mensal</Divider>
            <Form
                form={formMonthAndYear}
                layout='vertical'
                onFinish={handleQuery}
                clearOnDestroy
            >
                <Form.Item name={['year']} label={'Ano'} rules={[{required:true, message:"Obrigatório"}]}>
                    <InputNumber min={MINIMUM_YEAR} max={CURRENT_YEAR} />
                </Form.Item>

                <Form.Item name={['month']} label={'Mês'} rules={[{required:true, message:"Obrigatório"}]}>
                    <InputNumber min={1} max={12} />
                </Form.Item>

                <Button type='primary' icon={<CheckOutlined />} htmlType='submit'>Gerar</Button>
            </Form>
        </Space>

        <Space align='center' style={{display:'flex', flexDirection:'column'}}>
            <Divider>Anual</Divider>
            <Form
                form={formYear}
                layout='vertical'
                onFinish={handleQuery}
                clearOnDestroy
            >
                <Form.Item name={['year']} label={'Ano'} rules={[{required:true, message:"Obrigatório"}]}>
                    <InputNumber min={MINIMUM_YEAR} max={CURRENT_YEAR} />
                </Form.Item>

                <Button type='primary' icon={<CheckOutlined />} htmlType='submit'>Gerar</Button>
            </Form>
        </Space>
    </>
  )
}
