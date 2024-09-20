import React from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'

export const RiskSituation:React.FC<{entity:Hosted}> = ({entity}) => {

  return (
    <div>
        <p>{entity.firstName}</p>
    </div>
  )
}
