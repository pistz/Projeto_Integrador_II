import React, { useEffect, useState } from 'react'
import { Reception } from '../../../../../entity/Reception/Reception'
import { Divider } from 'antd'
import dayjs from 'dayjs';

export const ReceptionList:React.FC<{entity:Reception}> = ({entity}) => {

    const [list, setList] = useState<Reception>(entity);

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };

    useEffect(()=>{
        if(entity){
            setList(entity)
        }
    },[entity, setList])
    
  return (
    <div>
        <Divider>Lista de {changeDateFormatVisualization(entity.date)}</Divider>
        {list.hostedList.map((hosted)=>{
            return (
                <>
                    <dl>
                        <li>{hosted.hostedId}</li>
                        <li>{hosted.socialSecurityCPF}</li>
                    </dl>
                    
                </>
            )
        })}
    </div>
  )
}
