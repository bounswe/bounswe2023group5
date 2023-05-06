import React, { useState, useEffect } from 'react';
import './JSONViewer.css';

function JsonViewer() {
    const [json, setJson] = useState(null);

    useEffect(() => {
        // Fetch an example JSON object for testing purposes
        fetch('https://jsonplaceholder.typicode.com/todos')
            .then(response => response.json())
            .then(json => setJson(json));
    }, []);

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
            {renderJson(json)}
        </div>
    );
}

export default JsonViewer;

