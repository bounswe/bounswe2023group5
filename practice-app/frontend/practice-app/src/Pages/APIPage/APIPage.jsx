import React from 'react';
import { useLoaderData } from 'react-router-dom';
import FormBuilder from '../../Components/FormBuilder/FormBuilder';
import JsonViewer from '../../Components/JSONViewer/JSONViewer';
import { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import NavBar from '../../Components/NavBar/NavBar';


function APIPage({ }) {
    const apiData = useLoaderData();
    const onSubmit = async (data) => {
        if(apiData.postFunction) {
            await apiData.postFunction(data)
        } else {
            console.log(`You tried to post but dont have a post function set, heres what you posted \n ${JSON.stringify(data)}`)
        }
    }

    const [json, setJson] = useState(null);
    const [refreshData, setRefreshData] = useState(false);

    const getJSONData = async () => {
        const response = await apiData.getFunction()
        setJson(response)
    }

    useEffect(() => {
        getJSONData()
    }, [refreshData])


    return <div>
            <NavBar></NavBar>

        <FormBuilder inputs={apiData.form.inputs} buttonText={apiData.form.buttonText} onSubmit={onSubmit} />
        <div className='JsonViewer-button'>
            <Button
                variant="outlined" size="large" label="Submit" type="button"
                onClick={() => setRefreshData(!refreshData)}
            >
                Refresh
            </Button>
        </div>
        <JsonViewer json={json} />
    </div>;

}

export default APIPage;