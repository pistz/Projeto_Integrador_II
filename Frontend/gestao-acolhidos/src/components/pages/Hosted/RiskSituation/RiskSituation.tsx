import React, { useEffect, useState } from 'react'
import { Hosted } from '../../../../entity/Hosted/Hosted'
import { useTableData } from '../../../../hooks/useTableData';
import { Divider, Form } from 'antd';

export const RiskSituation:React.FC<{entity:Hosted}> = ({entity}) => {

    const {hostedEntity, setHostedEntity} = useTableData();
    const [localHostedEntity, setLocalHostedEntity] = useState<Hosted>(entity);

    const [form] = Form.useForm();
    const [openEdit, setOpenEdit] = useState<boolean>(false);

    useEffect(() => {
        if (entity) {
            setLocalHostedEntity(entity);
        }
    }, [entity]);

    useEffect(() => {
        setHostedEntity(localHostedEntity);
    }, [localHostedEntity, setHostedEntity]);



  return (
    <>
        <Divider>Mapeamento de Situação de Risco</Divider>
    </>
  )
}
