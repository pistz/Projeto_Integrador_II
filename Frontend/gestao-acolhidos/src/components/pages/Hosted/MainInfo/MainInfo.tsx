import { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Drawer, Form, FormProps, Input, Select, Space, Switch } from 'antd'
import dayjs from 'dayjs'
import { CreateForm } from '../CreateHosted/types'
import { createHostedDto } from '../../../../entity/dto/Hosted/createHostedDto'
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository'
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage'
import { useTableData } from '../../../../hooks/useTableData'
import { PlusOutlined } from '@ant-design/icons'
import { OtherDocs } from '../OtherDocs/OtherDocs'

const hostedRepository = new HostedRepository();

const brazilStates = [
  'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 
  'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 
  'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
];
const inputFormat = 'DD/MM/YYYY';
const dateFormat = 'YYYY-MM-DD';

export const MainInfo:React.FC<{entity:Hosted}> = ({entity}) => {

  const {setHostedTableData} = useTableData();

  const [openDocs, setOpenDocs] = useState<boolean>(false);

  const [form] = Form.useForm();

  const [edit, setEdit] = useState<boolean>(false);

  const onOpenDocs = ()=>{
      setOpenDocs(true);
  }
  const onCloseDocs = () =>{
      updateData()
      setOpenDocs(false)
  }

  const handleDateChange = (date: string) => {
    if (!date) return '';
  
    // Regex para verificar o formato YYYY-MM-DD
    const regexYYYYMMDD = /^\d{4}-\d{2}-\d{2}$/;
    
    // Regex para verificar o formato DD/MM/YYYY
    const regexDDMMYYYY = /^\d{2}\/\d{2}\/\d{4}$/;
  
    if (regexYYYYMMDD.test(date)) {
      // Se a data já estiver no formato YYYY-MM-DD, retornamos sem mudanças
      return date;
    } else if (regexDDMMYYYY.test(date)) {
      // Se a data estiver no formato MM-DD-YYYY, convertemos para YYYY-MM-DD
      return dayjs(date, inputFormat).format(dateFormat);
    }
  };

  const capitalizeFirstLetter=(string:string)=> {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
  }

  const handleSwitchChange = () => {
    setEdit(!edit);
  };


  const handleUpdateMainInfo:FormProps<CreateForm>['onFinish'] = async (values:createHostedDto) =>{
      values.firstName = capitalizeFirstLetter(values.firstName);
      values.lastName = capitalizeFirstLetter(values.lastName);
      if(values.birthDay){
        values.birthDay = handleDateChange(values.birthDay)
      }
      try {
          await hostedRepository.edit(values,entity.id)
            .then(()=>{
              notifySuccess("Atualização realizada")
            })
            handleSwitchChange()
            updateData()
      } catch (error) {
        errorOnFinish(error)
      }
  }

  const updateData=async()=>{
    const update = await hostedRepository.findAll();
    setHostedTableData(update) 
  }

  const errorOnFinish = (error:unknown) =>{
    notifyError("Erro ao realizar cadastro");
    return error;
}

  useEffect(() => {
  form.setFieldsValue({ ...entity });
}, [entity, form]);



  return (
    <>
        <Space align='start' direction='vertical' style={{display:'flex', flexDirection:'column', margin:'0 3rem'}}>
          <Divider>Dados Principais</Divider>
          <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'row'}}>
            <Switch checked={edit} onClick={handleSwitchChange} unCheckedChildren="Editar" checkedChildren="Editar" />
            <Button style={{margin:"0 6rem"}} type='primary' icon={<PlusOutlined/>} onClick={onOpenDocs}>Mais Documentos</Button>
            <Drawer placement='right' width={800} closable={true} onClose={onCloseDocs} open={openDocs} destroyOnClose>
              <OtherDocs entity={entity}></OtherDocs>
            </Drawer>
          </Space>
          
          <Form
            form={form}
            disabled={!edit}
            initialValues={{ ...entity }}
            layout='vertical'
            onFinish={handleUpdateMainInfo}
          >
            <Form.Item name={['firstName']} label='Nome' >
              <Input value={entity.firstName} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['lastName']} label='Sobrenome' >
              <Input value={entity.lastName} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['socialSecurityNumber']} label='CPF' rules={[ {pattern:/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, message: 'Formato inválido. Use o formato 000.000.000-00' }]}>
              <Input value={entity.socialSecurityNumber} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['birthDay']} label='Data de nascimento'>
              {edit? <Input placeholder={entity.birthDay}/>
                :<Input />}
            </Form.Item>

            <Form.Item name={['paperTrail']} label="Prontuário nº">
              <Input value={entity.paperTrail} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['fathersName']} label="Nome do pai">
              <Input value={entity.fathersName} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['mothersName']} label="Nome da mãe">
              <Input value={entity.mothersName} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['occupation']} label="Profissão">
              <Input value={entity.occupation} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['cityOrigin']} label="Cidade de Nascimento">
              <Input value={entity.cityOrigin} disabled={!edit} />
            </Form.Item>

            <Form.Item name={['stateOrigin']} label="Estado">
              {edit?
              <Select>
                {brazilStates.map(state => (
                  <Select.Option key={state} value={state}>
                    {state}
                  </Select.Option>
                ))}
              </Select>
              :
              <Input value={entity.cityOrigin} disabled={!edit} />
            }
            </Form.Item>
            <Button htmlType='submit' type='primary'>Salvar Alterações</Button>
          </Form>
        </Space>
    </>
  )
}
