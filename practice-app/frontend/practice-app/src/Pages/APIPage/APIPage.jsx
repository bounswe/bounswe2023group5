import React from 'react';
import { useLoaderData } from 'react-router-dom';
import FormBuilder from '../../Components/FormBuilder/FormBuilder';
import JsonViewer from '../../Components/JSONViewer/JSONViewer';
import { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import NavBar from '../../Components/NavBar/NavBar';
import "./APIPage.scss";
import { Divider } from '@mui/material';


function APIPage({ }) {
    const apiData = useLoaderData();

    const [json, setJson] = useState(null);
    const [refreshData, setRefreshData] = useState(false);

    const getJSONData = async () => {
        const response = await apiData.getFunction();
        setJson(response);
    };
    const onSubmit = async (data) => {
        if (apiData.postFunction) {
            await apiData.postFunction(data);
            await getJSONData();
        } else {
            console.log(`You tried to post but dont have a post function set, heres what you posted \n ${JSON.stringify(data)}`)
        }
    }


    useEffect(() => {
        getJSONData();
    }, [apiData, refreshData]);



    return <div className='apipage'>
        <div className='top-box'>
            <nav>
                <NavBar></NavBar>
            </nav>
            <h1 className='apipage-title'>
                {apiData.name}
            </h1>
            <Divider />
            <div className='form-box'>
                <FormBuilder inputs={apiData.form.inputs} buttonText={apiData.form.buttonText} onSubmit={onSubmit} />
            </div>
            {apiData.dataTitle ?
                <><h2 className='apipage-title'>
                    {apiData.dataTitle}
                </h2>
                    <Divider /> </> : null
            }
        </div>
        <div className='json-box'>
            <div className='refresh-btn'>
                <Button
                    variant="outlined" size="large" label="Submit" type="button"
                    onClick={() => setRefreshData(!refreshData)}
                >
                    Refresh
                </Button>
            </div>

            <JsonViewer json={json} />

        </div>
    </div>;

}

export default APIPage;