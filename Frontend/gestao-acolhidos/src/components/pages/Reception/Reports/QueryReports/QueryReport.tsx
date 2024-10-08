import React, { useEffect, useState } from 'react'
import { Reception } from '../../../../../entity/Reception/Reception'
import { thStyle } from './style';
import { Divider } from 'antd';
import dayjs from 'dayjs';
import { queryReceptionDto } from '../../../../../entity/dto/Reception/queryReceptionDto';

export const QueryReport:React.FC<{entity:Reception[], referenceDate:queryReceptionDto}> = ({entity, referenceDate}) => {

    const [report, setReport] = useState<Reception[]>(entity);
    const [reportDate, setReportDate] = useState<queryReceptionDto>(referenceDate)

    const getTotalDaysWithReception = () => {
        return report.length;
    };

    const changeDateFormatVisualization = (date: string) => {
        const inputFormat = 'DD/MM/YYYY';
        const dateFormat = 'YYYY-MM-DD';
        return dayjs(date, dateFormat).format(inputFormat);
    };

    const getTotalHostedsPerReport = () => {
        let result = 0;
        report.forEach((e) => {
            result += e.hostedList.length;
        });
        return result;
    };

    useEffect(() =>{
        if(entity){
            setReport(entity)
            setReportDate(referenceDate)
        }
    }, [entity,setReport, setReportDate,referenceDate])

  return (
    <>
        <div style={{display:'flex', alignItems:'center', justifyContent:'flex-start', margin:'3rem auto', flexDirection:'column'}}>
            <Divider style={{fontSize:'2rem'}}>Relatório de Acolhimentos</Divider>
            {reportDate.month? <>
            <h3>Mensal: {`${reportDate.month}-${reportDate.year}`}</h3>
            </> : <>
            <h3>Anual: {reportDate.year}</h3>
            </>}
            <h3>Total de dias com acolhimento: {getTotalDaysWithReception()}</h3>
            <h3>Total de acolhidos: {getTotalHostedsPerReport()}</h3>
            
            {report.sort((a, b)=> a.date.localeCompare(b.date)).map((reception) => (
                <div key={reception.receptionId} style={{ marginBottom: '2rem', borderBottom: '1px solid #ccc', paddingBottom: '1rem' }}>
                    <h4>Data: {changeDateFormatVisualization(reception.date)}</h4>
                    <p>Total de acolhidos: {reception.hostedList.length}</p>
                    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                        <thead>
                            <tr>
                                <th style={thStyle}>Nome Completo</th>
                                <th style={thStyle}>CPF</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reception.hostedList.map((hosted) => (
                                <tr key={hosted.hostedId}>
                                    <td style={thStyle}>{`${hosted.firstName} ${hosted.lastName}`}</td>
                                    <td style={thStyle}>{hosted.socialSecurityCPF}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ))}
        </div>
    </>
  )
}
