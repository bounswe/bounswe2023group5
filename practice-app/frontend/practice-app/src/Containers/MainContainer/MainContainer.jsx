import React from "react";
import { Outlet } from "react-router-dom";
import "./MainContainer.scss";
import JSONViewer from "../../Components/JSONViewer/JSONViewer.js"

function MainContainer() {
    return <div className="main-container">
        <main className="center-box">
            <Outlet />
            <JSONViewer json={JSON.stringify({ key: 'value' })} />
        </main>
    </div>
}

export default MainContainer;