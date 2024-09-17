import React from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { Divider } from 'antd'

export const Documents:React.FC<Hosted> = (hosted:Hosted) => {

  return (
    <>
        <Divider>Cadastro Completo</Divider>
        <p>{hosted.id}</p>
        <p>{hosted.firstName}</p>
    </>
  )
}
