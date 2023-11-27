import React, { useState } from 'react';
import styles from './SquareAchievement.module.scss';
import { Tooltip } from 'antd';


function SquareAchievement ({ props }:{ props: any }) {


  return (
    <div>
        <div className= {styles.achievement}>
        
        
            <div className={styles.achievement_content}>
                <Tooltip title={props.title+ ": "+ props.description} className={styles.tootip}>
                    <img
                        src={`${import.meta.env.VITE_APP_IMG_URL}${props?.icon}`}
                        alt="achievement icon"
                        className={styles.icon}
                    ></img>
                </Tooltip>
            </div>
            
        </div>

    </div>
  );
};


export default SquareAchievement;