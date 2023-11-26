import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import "./globals.scss";
import AntdConfigProvider from "./Components/Providers/AntdConfigProvider";

function App() {
  return (
    <AntdConfigProvider>
      <RouterProvider router={router} />
    </AntdConfigProvider>
  );
}

export default App;
