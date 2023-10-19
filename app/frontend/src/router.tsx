import { createBrowserRouter } from "react-router-dom";
import HelloWorld from "./Pages/HelloWorld/HelloWorld";

const router = createBrowserRouter([
  {
    path: "/",
    element: <HelloWorld />,
  },
]);

export { router };
