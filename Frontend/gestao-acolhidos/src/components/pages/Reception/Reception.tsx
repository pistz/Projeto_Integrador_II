import { Button, Divider, Modal, Space } from 'antd'
import React, { useState } from 'react'
import { mainDivStyle } from './styles'
import { FileOutlined, PlusCircleOutlined } from '@ant-design/icons'
import { NewList } from './NewList/NewList'
import { useTableData } from '../../../hooks/useTableData'
import { HostedRepository } from '../../../repository/Hosted/HostedRepository'
import { ConfigRepository } from '../../../repository/Config/ConfigRepository'
import { ReceptionTableList } from './ReceptionTableList/ReceptionTableList'
import { ReceptionRepository } from '../../../repository/Reception/ReceptionRepository'

const hostedRepository = new HostedRepository()
const capacity = new ConfigRepository();
const reception = new ReceptionRepository();

export const Reception:React.FC = () => {

  const [openNewList, setOpenNewList] = useState(false);
  const [bedCapacity, setBedCapacity] = useState(0);
  
  const {hostedTableData, setHostedTableData} = useTableData();

  const openNewListModal = async()=>{
    await capacity.getCapacity()
    .then((beds) => setBedCapacity(beds))
    await hostedRepository.findAll()
    .then(hostedList => {
      setHostedTableData(hostedList);
      setOpenNewList(true)
    });
  }


  return (
    <div style={mainDivStyle}>
      <Divider>Gestão de Pernoite</Divider>
      <Space align='center' direction='horizontal' style={{marginTop:'3rem'}}>
          <Button icon={<PlusCircleOutlined />} type='primary' onClick={openNewListModal}>Nova Lista</Button>
          <Modal
              open={openNewList} 
              width={1500}
              onCancel={()=> setOpenNewList(false)} 
              footer={false} 
              destroyOnClose
              >
            <NewList entity={hostedTableData} capacity={bedCapacity}/>
          </Modal>
          <Button icon={<FileOutlined />}>Relatórios</Button>
      </Space>
      <Space style={{margin:'3rem 0'}} align='center' direction='vertical'>
        <Divider>Listagem Completa de Acolhimentos</Divider>
        <ReceptionTableList
          getAllEntities={reception.findAll}
          deleteEntity={reception.delete}
          listQueryKey='receptionQueryKey'
        />
      </Space>
    </div>
  )
}
