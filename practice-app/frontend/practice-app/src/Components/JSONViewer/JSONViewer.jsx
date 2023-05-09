import { Paper } from '@mui/material';
import React, { useState, useEffect } from 'react';
import './JSONViewer.scss';

function JsonViewer({ json }) {

    function renderJson(json) {
        if (!json) {
            return "";
        }
        else if (typeof json === 'string') {
            return json;
        }
        else if (Array.isArray(json)) {
            return (
                <div className="jsonviewer-array-box" key="array-box">
                    {json.map((value, index) =>
                        <div key={value._id ? value._id : index}>
                            {renderJson(value)}
                        </div>
                    )}
                </div>
            );
        }
        else if (typeof json === 'object') {
            return (
                <Paper elevation={4} key="paper">
                    <div className="jsonviewer-object-box">
                        {Object.entries(json).map(([key, value], index) => (
                            <div className="jsonviewer-value-box" key={index}>
                                <div className="jsonviewer-key">{key}:</div>
                                <div className="jsonviewer-value">{renderJson(value)}</div>
                            </div>
                        ))}
                    </div>
                </Paper>
            );
        }
        else if (typeof json === 'number' || typeof json === 'boolean') {
            return String(json);
        }
        else {
            return "Data type not supported";
        }
    }

    if (!json) {
        return <div className='jsonviewer-loading'>Loading...</div>;
    }

    return (
        <div className="jsonviewer" key="jsonviewer">
            {renderJson(json)}
        </div>
    );
}

export default JsonViewer;
