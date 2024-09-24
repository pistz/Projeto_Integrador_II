import React from 'react'
import { Reception } from '../../../../../entity/Reception/Reception'

export const QueryReport:React.FC<{entity:Reception[]}> = ({entity}) => {

  return (
    <>
        <div>:React.FC</div>
        {entity.map((e) =>{
            return (
                <>
                    <p>{e.date}</p>
                </>
            )
        })}
    </>
  )
}
