import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import "./globals.scss";
import { ConfigProvider } from "antd";
import { theme } from "./theme";

function App() {
  return (
    <>
      <ConfigProvider theme={theme}>
        <RouterProvider router={router} />
      </ConfigProvider>
    </>
  );
}

export default App;
