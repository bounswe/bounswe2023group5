import React, { useState, useEffect } from 'react';
import './JSONViewer.css';

function JsonViewer() {
    const [json, setJson] = useState(null);
    const [loadingData, setLoadingData] = useState(true);
    const [refreshData, setRefreshData] = useState(false);

    const getData = async () => {
        const response = await fetch('https://jsonplaceholder.typicode.com/todos')
        setJson(await response.json())
        setLoadingData(false);
    }

    useEffect(() => {
        getData()
    }, [refreshData])

    function renderJson(json) {
        if (typeof json === 'string') {
            return <div>{json}</div>;
        }

        if (Array.isArray(json)) {
            return (
                <div>
                    {json.map((value, index) => (
                        <div className="JsonViewer-box" key={index}>
                            {renderJson(value)}
                        </div>
                    ))}
                </div>
            );
        }

        if (typeof json === 'object') {
            return (
                <div>
                    {Object.entries(json).map(([key, value], index) => (
                        <div>
                            <div key={index}>
                                <span className="JsonViewer-key">{key}:</span>
                            </div>
                            <div>
                                <span className="JsonViewer-value">{renderJson(value)}</span>
                            </div>
                        </div>
                    ))}
                </div>
            );
        }

        if (typeof json === 'number' || typeof json === 'boolean') {
            return <span>{String(json)}</span>;
        }




    }

    if (!json) {
        return <div>Loading...</div>;
    }

    return (
        <div className="JsonViewer">
            <button
                className="JsonViewer-button"
                type="button"
                onClick={() => setRefreshData(!refreshData)}
            >
                Refresh
            </button>
            {renderJson(json)}
        </div>
    );
}

export default JsonViewer;