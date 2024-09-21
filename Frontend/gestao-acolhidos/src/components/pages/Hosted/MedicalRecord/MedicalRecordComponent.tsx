import React from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'

export const MedicalRecordComponent:React.FC<{entity:Hosted}> = ({entity}) => {

  return (
    <p>{entity.id}</p>
  )
}
