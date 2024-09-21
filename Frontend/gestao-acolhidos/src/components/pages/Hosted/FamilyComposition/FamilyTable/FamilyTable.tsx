import React, { useEffect } from 'react'
import { Hosted } from '../../../../../entity/Hosted/Hosted'
import { useTableData } from '../../../../../hooks/useTableData'
import { Collapse } from 'antd'
import dayjs from 'dayjs'

export const FamilyTableComponent:React.FC<{entity:Hosted}> = ({entity}) => {

    const {hostedEntity, setHostedEntity} = useTableData();
    
    useEffect(()=>{
        if(entity.familyComposition){
            setHostedEntity(entity)
        }
    },[entity, setHostedEntity]);

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };


return (
<>
    {hostedEntity.familyTable? hostedEntity.familyTable.map((member) =>{
        return (
        <>
            <Collapse accordion 
                style={{width:'30rem'}}
                key={'mainId'+member.id}
                items={[{key:'itemId'+member.id, 
                label:member.name, 
                children:(<>
                <ul key={'tableId'+member.id}>
                    <li><strong>Nome: </strong>{member.name}</li>
                    <li><strong>Idade: </strong>{member.age}</li>
                    <li><strong>Sexo: </strong>{member.gender}</li>
                    <li><strong>Estado Civil: </strong>{member.maritalStatus}</li>
                    <li><strong>Escolaridade: </strong>{member.education}</li>
                    <li><strong>Profissão: </strong>{member.occupation}</li>
                    <li><strong>Registrado em: </strong>{changeDateFormatVisualization(member.updatedAt)}</li>
                </ul>
                </>)}]}/>
        </>)}):(<></>)}
    </>
    )
}
