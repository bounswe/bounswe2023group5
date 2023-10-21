import { createBrowserRouter } from "react-router-dom";
import HelloWorld from "./Pages/HelloWorld/HelloWorld";
import MainLayout from "./Layout/MainLayout/MainLayout";
import ForgotPassword from "./Pages/ForgotPassword/ForgotPassword";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        path: "hello",
        element: <HelloWorld />,
      },
    ],
  },
  {
    path: "forgot",
    element: <ForgotPassword />,
  },
]);

export { router };
