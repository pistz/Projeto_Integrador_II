import React, { useEffect, useState } from 'react'
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';
import { Divider, Spin } from 'antd';
import dayjs from 'dayjs';

const hostedRepository = new HostedRepository();

export const IndividualReceptions:React.FC<{entity:string}> = ({entity}) => {

    const [hostedId, setHostedId] = useState<string>(entity)

    const [receptions, setReceptions] = useState<string[]>([])

    const [loading, setLoading] = useState<boolean>(true);

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };


    useEffect(()=>{
        if(entity){
            setHostedId(entity)
        }
        const loadList = async(id:string) =>{
            try {
                await hostedRepository.getReceptionList(id)
                .then((e) => setReceptions(e))
                setLoading(false)
            } catch (error) {
                console.error(error)
            }
        }
        loadList(hostedId)
    },[entity, setHostedId, hostedId])

  return (
    <>  <Spin spinning={loading} size='large'>
        <h3>{`Quantidade de acolhimentos: ${receptions.length}`}</h3>
        <Divider>Datas</Divider>
            {receptions.sort().map((e) => {
                return (<>
                    <p>{changeDateFormatVisualization(e)}</p>
                </>)
            })}
        </Spin>
    </>
  )
  
}
