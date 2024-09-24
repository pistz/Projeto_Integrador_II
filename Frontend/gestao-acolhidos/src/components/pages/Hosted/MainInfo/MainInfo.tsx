import { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Button, Divider, Drawer, Form, FormProps, Input, InputNumber, Select, Space, Switch } from 'antd'
import dayjs from 'dayjs'
import { CreateForm } from '../CreateHosted/types'
import { createHostedDto } from '../../../../entity/dto/Hosted/createHostedDto'
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository'
import { notifyError, notifySuccess } from '../../../shared/PopMessage/PopMessage'
import { useTableData } from '../../../../hooks/useTableData'
import { AlertOutlined, CheckOutlined, DollarOutlined, FlagOutlined, HomeOutlined, PlusOutlined, TeamOutlined } from '@ant-design/icons'
import { OtherDocs } from '../OtherDocs/OtherDocs'
import { brazilStates } from '../../../shared/StateList/StateList'
import { RefAddress } from '../ReferenceAddress/ReferenceAddress'
import { PoliceReportComponent } from '../PoliceReport/PoliceReport'
import { FamilyCompositionComponent } from '../FamilyComposition/FamilyCompositionComponent'
import { RiskSituation } from '../RiskSituation/RiskSituation'
import { SocialProgramComponent } from '../SocialProgram/SocialProgramComponent'

const hostedRepository = new HostedRepository();

export const MainInfo:React.FC<{entity:Hosted}> = ({entity}) => {

  //Contexto geral
  const {setHostedTableData} = useTableData();

  //Form options
  const [edit, setEdit] = useState<boolean>(false);
  const [form] = Form.useForm();

  //Controle de abertura do drawer de Outros Documentos
  const [openDocs, setOpenDocs] = useState<boolean>(false);

  const onOpenDocs = ()=>{
    updateData()
    setOpenDocs(true);
  }
  const onCloseDocs = () =>{
    updateData()
    setOpenDocs(false)
  }
  
    //Controle de abertura do drawer de Endereço de Referência
  const [openRefAddress, setOpenRefAddress] = useState<boolean>(false);

  const onOpenRefAddress = ()=>{
    updateData()
    setOpenRefAddress(true);
  }
  const onCloseRefAddress = () =>{
    updateData()
    setOpenRefAddress(false)
  }

    //Controle de abertura do drawer de Police Report
    const [openPoliceReport, setOpenPoliceReport] = useState<boolean>(false);

    const onOpenPoliceReport = ()=>{
      updateData()
      setOpenPoliceReport(true);
    }
    const onClosePoliceReport = () =>{
      updateData()
      setOpenPoliceReport(false)
    }

    //Controle de abertura do drawer de Family Composition
    const [openFamilyComposition, setOpenFamilyComposition] = useState<boolean>(false);

    const onOpenFamilyComposition = ()=>{
      updateData()
      setOpenFamilyComposition(true);
    }
    const onCloseFamilyComposition = () =>{
      updateData()
      setOpenFamilyComposition(false)
    }

    //Controle de abertura do drawer de Risk Situation
    const [openRiskSituation, setOpenRiskSituation] = useState<boolean>(false);

    const onOpenRiskSituation = ()=>{
      updateData()
      setOpenRiskSituation(true);
    }
    const onCloseRiskSituation = () =>{
      updateData()
      setOpenRiskSituation(false)
    }

    //Controle de abertura do drawer de Social Program
    const [openSocialProgram, setOpenSocialProgram] = useState<boolean>(false);

    const onOpenSocialProgram = ()=>{
      updateData()
      setOpenSocialProgram(true);
    }
    const onCloseSocialProgram = () =>{
      updateData()
      setOpenSocialProgram(false)
    }
    
  const handleDateChange = (date: string) => {
    const inputFormat = 'DD/MM/YYYY';
    const dateFormat = 'YYYY-MM-DD';
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
          <Divider>Documentação de Apoio | Família | Condição de Vulnerabilidade</Divider>

          <Space align='center' direction='vertical' style={{display:'flex', flexDirection:'row', marginBottom:'1.5rem'}}>

            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' icon={<PlusOutlined/>} onClick={onOpenDocs}>Mais Documentos</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onCloseDocs} open={openDocs} destroyOnClose>
                <OtherDocs entity={entity}></OtherDocs>
              </Drawer>
            </div>

            
            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' ghost icon={<HomeOutlined/>} onClick={onOpenRefAddress}>Endereço de Referência</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onCloseRefAddress} open={openRefAddress} destroyOnClose>
                <RefAddress entity={entity}></RefAddress>
              </Drawer>
            </div>

            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' ghost icon={<AlertOutlined/>} onClick={onOpenPoliceReport}>Boletim de Ocorrência</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onClosePoliceReport} open={openPoliceReport} destroyOnClose>
                <PoliceReportComponent entity={entity}></PoliceReportComponent>
              </Drawer>
            </div>

            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' ghost icon={<TeamOutlined/>} onClick={onOpenFamilyComposition}>Família</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onCloseFamilyComposition} open={openFamilyComposition} destroyOnClose>
                <FamilyCompositionComponent entity={entity}></FamilyCompositionComponent>
              </Drawer>
            </div>

            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' ghost icon={<FlagOutlined/>} onClick={onOpenRiskSituation}>Situação de Risco</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onCloseRiskSituation} open={openRiskSituation} destroyOnClose>
                <RiskSituation entity={entity}></RiskSituation>
              </Drawer>
            </div>


            <div className='Options-Buttons'>
              <Button style={{margin:"0 0 0 1rem"}} type='primary' ghost icon={<DollarOutlined/>} onClick={onOpenSocialProgram}>Programas Sociais</Button>
              <Drawer placement='right' width={800} closable={true} onClose={onCloseSocialProgram} open={openSocialProgram} destroyOnClose>
                <SocialProgramComponent entity={entity}></SocialProgramComponent>
              </Drawer>
            </div>
          </Space>

          <Divider>Dados Principais</Divider>

          <Space align='baseline' direction='vertical'>
            <Switch checked={edit} onClick={handleSwitchChange} unCheckedChildren="Editar" checkedChildren="Editar" />
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
              <InputNumber value={entity.paperTrail} disabled={!edit} />
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

            <Button htmlType='submit' type='primary' icon={<CheckOutlined />}>Salvar</Button>
          </Form>
        </Space>
    </>
  )
}
