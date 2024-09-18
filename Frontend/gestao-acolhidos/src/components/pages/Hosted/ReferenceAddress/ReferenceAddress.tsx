import React from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { ReferenceAddressDto } from './types'

export const RefAddress:React.FC<{entity:Hosted}> = ({entity}) => {

    const dto:ReferenceAddressDto={
        cep:'',
        city:'',
        neighborhood:'',
        number:0,
        phoneNumber:0,
        street:''
    }
    //TODO - finalizar componente
  return (
    <p>{entity.age}</p>
  )
}
