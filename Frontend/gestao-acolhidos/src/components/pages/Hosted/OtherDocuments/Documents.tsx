import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Divider, Space } from 'antd'

export const Documents:React.FC<{entity:Hosted}> = ({entity}) => {

  return (
    <>
        <Space align='center' direction='vertical' style={{justifyContent:'center'}}>
          <Divider>Cadastro Completo</Divider>
          <p>{entity.id}</p>
          <p>{entity.firstName}</p>
        </Space>
    </>
  )
}
