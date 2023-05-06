import React from 'react';
import { useLoaderData } from 'react-router-dom';
import FormBuilder from '../../Components/FormBuilder/FormBuilder';
import JsonViewer from '../../Components/JSONViewer/JSONViewer';
import { useState, useEffect } from 'react';

function APIPage({ }) {
    const apiData = useLoaderData();
    const onSubmit = (data) => console.log(data);

    const [json, setJson] = useState(null);
    const [loadingData, setLoadingData] = useState(true);
    const [refreshData, setRefreshData] = useState(false);

    const getJSONData = async () => {
        const response = await fetch(apiData.jsonUrl)
        setJson(await response.json())
        setLoadingData(false);
    }

    useEffect(() => {
        getJSONData()
    }, [refreshData])


    return <div>
        <FormBuilder inputs={apiData.form.inputs} buttonText={apiData.form.buttonText} onSubmit={onSubmit} />
        <button
            className="JsonViewer-button"
            type="button"
            onClick={() => setRefreshData(!refreshData)}
        >
            Refresh
        </button>
        <JsonViewer json={json} />
    </div>;
}

export default APIPage;