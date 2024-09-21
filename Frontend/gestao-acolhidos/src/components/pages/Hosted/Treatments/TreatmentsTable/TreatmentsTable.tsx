import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../../entity/Hosted/Hosted'
import dayjs from 'dayjs'
import { Collapse } from 'antd'

export const TreatmentsTable:React.FC<{entity:Hosted}> = ({entity}) => {

    const [hostedEntity, setHostedEntity] = useState<Hosted>(entity)

    useEffect(()=>{
      if(entity) setHostedEntity(entity)
    },[entity])
  
    const changeDateFormatVisualization = (date: string) => {
      const inputFormat = 'DD/MM/YYYY';
      const dateFormat = 'YYYY-MM-DD';
      return dayjs(date, dateFormat).format(inputFormat);
  };
  
  return (
    <>
        {hostedEntity.customTreatments? hostedEntity.customTreatments.map((member) =>{
            return (
            <>
                <Collapse accordion 
                    style={{width:'30rem'}}
                    key={'mainId'+member.id}
                    items={[{key:'itemId'+member.id, 
                    label:`Procedimento registrado em:  ${changeDateFormatVisualization(member.createdAt)}`, 
                    children:(<>
                    <table key={'tableId'+member.id}>
                        <ul>{member.procedure}</ul>
                    </table>
                    </>)}]}/>
            </>)}):(<></>)}
        </>
        )
}
