import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import "./globals.scss";
import AntdConfigProvider from "./Components/Providers/AntdConfigProvider";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


function App() {
  return (
    <AntdConfigProvider>
      <RouterProvider router={router} />
      <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          theme="light"
        />
    </AntdConfigProvider>
  );
}

export default App;
