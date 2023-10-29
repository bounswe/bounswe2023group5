import React from "react";
import ReactDOM from "react-dom/client";
import { QueryCache, QueryClient, QueryClientProvider } from "react-query";

import App from "./App.tsx";
import { AuthProvider } from "./Components/Hooks/useAuth.tsx";
import { message } from "antd";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError(error) {
      const text = (error as Error).message;
      message.error(text);
      console.error(error as Error);
    },
  }),
});

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
