import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, DatePicker, Divider, Form, FormProps, SelectProps} from 'antd';
import { Container } from './style';
import { Footer } from 'antd/es/layout/layout';
import { CheckOutlined, DownOutlined } from '@ant-design/icons';
import Select, { DefaultOptionType } from 'antd/es/select';
import dayjs from 'dayjs';
import locale from 'antd/es/date-picker/locale/pt_BR';
import { createReceptionDto } from '../../../../entity/dto/Reception/createReceptionDto';
import { ReceptionNewListForm } from './types';
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage';
import { ReceptionRepository } from '../../../../repository/Reception/ReceptionRepository';

const receptionRepository = new ReceptionRepository();
export const NewList:React.FC<{entity:Hosted[], capacity:number}> = ({entity, capacity}) => {

    const [hosteds, setHosteds] = useState<Hosted[]>(entity);
    const [bedCapacity, setBedCapacity] = useState<number>(capacity);

    const [selectdBeds, setSelectedBeds] = useState<number>(0)

    const [form] = Form.useForm();


    useEffect(()=>{
        if(entity){
            setHosteds(entity)
        }
        if(capacity){
            setBedCapacity(capacity)
        }
    },[entity,capacity])

    const options: SelectProps['options'] = [];

    hosteds.forEach((hosted)=>{
        const presentationName = `Nome: ${hosted.firstName} ${hosted.lastName}, CPF: ${hosted.socialSecurityNumber}`;
        options.push({
            label:presentationName,
            value:hosted.id
        })
    })

    const handleOnFinish:FormProps<ReceptionNewListForm>['onFinish'] = async (values:createReceptionDto) =>{
        values.date = handleDateChange(values.date)
        try {
            await receptionRepository.create(values)
            .then(()=>{
                notifySuccess("Registro Criado")
            });
        } catch (error) {
            errorOnFinish(error)   
        }
    }

    const errorOnFinish = (error:unknown) =>{
        notifyError("Erro ao tentar incluir registro");
        return error;
    }

    const inputFormat = 'DD/MM/YYYY'
    const dateFormat = 'YYYY-MM-DD';
    
    const handleDateChange = (date:string) => {
        const formattedDate = date ? dayjs(date).format(dateFormat) : '';
        return formattedDate
    };

    const handleChange = (values: DefaultOptionType[]) => {
        form.setFieldValue("hostedList", values)
        setSelectedBeds(values.length);
    };

    const capacityCounter = (
        <>
            <span>
                {selectdBeds} / {bedCapacity}
            </span>
            <DownOutlined />
        </>
    )

  return (
    <>
        <Container>
            <Form
                form={form}
                layout='vertical'
                onFinish={handleOnFinish}
                clearOnDestroy
            
            >
            <Divider>Nova Lista</Divider>
            <Form.Item name={['date']} label="Data do Acolhimento" 
                rules={[{ required: true, message:'Selecione uma data válida e não repetida'}]}>
                <DatePicker placeholder='Selecione a data' 
                    locale={locale} 
                    format={inputFormat}
                    onChange={handleDateChange}
                />
            </Form.Item>
            <Form.Item
                name={['hostedList']} label={""}
                >
            <div>
                <Select
                    mode='multiple'
                    style={{width:"100%"}}
                    options={options}
                    onChange={handleChange}
                    filterOption={(input, option) =>
                        (option?.label as string ?? '').toLowerCase().includes(input.toLowerCase())
                    }
                    placeholder={'Selecione pelo nome'}
                    maxCount={bedCapacity}
                    suffixIcon={capacityCounter}
                ></Select>
            </div>
            </Form.Item>
        
            <Footer style={{backgroundColor:"transparent", alignContent:"center", justifyContent:"center", display:'flex'}}>
                <Button icon={<CheckOutlined />} type='primary' htmlType='submit'>Salvar</Button>
            </Footer>
            </Form>
        </Container>
    </>
  )
}
