import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../../entity/Hosted/Hosted'
import { Collapse } from 'antd'
import dayjs from 'dayjs'

export const MedicalRecordTable:React.FC<{entity:Hosted}> = ({entity}) => {

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
        {hostedEntity.medicalRecord? hostedEntity.medicalRecord.map((member) =>{
            return (
            <>
                <Collapse accordion 
                    style={{width:'30rem'}}
                    key={'mainId'+member.id}
                    items={[{key:'itemId'+member.id, 
                    label:`Queixa registrada em ${changeDateFormatVisualization(member.createdAt)}`, 
                    children:(<>
                    <table key={'tableId'+member.id}>
                        <ul><strong>Nome: </strong>{member.complaints}</ul>
                    </table>
                    </>)}]}/>
            </>)}):(<></>)}
        </>
        )
}
