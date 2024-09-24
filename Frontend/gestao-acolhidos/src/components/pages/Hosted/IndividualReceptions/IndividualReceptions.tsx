import React, { useEffect, useState } from 'react'
import { HostedRepository } from '../../../../repository/Hosted/HostedRepository';

const hostedRepository = new HostedRepository();

export const IndividualReceptions:React.FC<{entity:string}> = ({entity}) => {

    const [hostedId, setHostedId] = useState<string>(entity)

    const [receptions, setReceptions] = useState<string[]>([])

    useEffect(()=>{
        if(entity){
            setHostedId(entity)
        }
        const loadList = async(id:string) =>{
            try {
                await hostedRepository.getReceptionList(id)
                .then((e) => setReceptions(e))
            } catch (error) {
                console.error(error)
            }
        }
        loadList(hostedId)
    },[entity, setHostedId, hostedId])

  return (
    <>
    <div>IndividualReceptions</div>
        {receptions.map((e) => {
            return (<>
                <p>{e}</p>
            </>)
        })}
    </>
  )
  
}
