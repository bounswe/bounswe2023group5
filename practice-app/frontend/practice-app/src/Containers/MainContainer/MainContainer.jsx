import React from "react";
import { Outlet } from "react-router-dom";
import "./MainContainer.scss";

function MainContainer() {
    return <div className="main-container">
        <main className="center-box">
            <Outlet />
        </main>
    </div>
}

export default MainContainer;