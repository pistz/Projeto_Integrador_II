import React from 'react'
import { mainDivStyle, subMenuDivStyle } from './styles'
import { Link } from '../../shared/Link/Link'
import { hosted } from '../../../routes/HostedRoutes/HostedRoutes'
import { Divider } from 'antd'
import { ListAll } from './ListAll/ListAll'
import { TableContainer } from '../../shared/TableContainer/TableContainer'
import { HostedRepository } from '../../../repository/Hosted/HostedRepository'

const hostedQueryKey = "hostedQueryKey";

export const HostedServices:React.FC = () => {

  const hosteds = new HostedRepository()


  return (
    <>
      <div style={mainDivStyle}>
        <div style={subMenuDivStyle}>
          {hosted.map((_,index) => <Link to={hosted[index].fullpath+hosted[index].path} title={hosted[index].label} key={index}/>)}
        </div>
        <Divider style={{borderColor:"#000"}}/>
        <div>
          <TableContainer>
            <ListAll listQueryKey={hostedQueryKey} getAllEntities={hosteds.findAll}/>
          </TableContainer>
        </div>
      </div>
    </>
  )
}
