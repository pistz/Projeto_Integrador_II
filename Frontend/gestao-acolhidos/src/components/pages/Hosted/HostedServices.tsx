import React from 'react'
import { mainDivStyle, subMenuDivStyle, tableContainer } from './styles'
import { Link } from '../../shared/Link/Link'
import { hosted } from '../../../routes/HostedRoutes/HostedRoutes'
import { Button, Divider } from 'antd'
import { ListAll } from './ListAll/ListAll'
import { HostedRepository } from '../../../repository/Hosted/HostedRepository'

const hostedQueryKey = "hostedQueryKey";

export const HostedServices:React.FC = () => {

  const hosteds = new HostedRepository()


  return (
    <>
      <div style={mainDivStyle}>
        <Divider>Acolhidos</Divider>
        <div style={subMenuDivStyle}>
          {hosted.map((_,index) => <Button type='primary' shape='round' key={index+2}><Link to={hosted[index].fullpath+hosted[index].path} title={hosted[index].label} key={index}/> </Button>)}
        </div>
        <div style={tableContainer}>
          <ListAll listQueryKey={hostedQueryKey} getAllEntities={hosteds.findAll} deleteEntity={hosteds.deleteHosted}/>
        </div>
      </div>
    </>
  )
}
