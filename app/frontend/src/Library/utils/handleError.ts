import { message } from "antd";
import { NotificationUtil } from "./notification";

export function handleError(error: any) {
  {
    const text =
      (error as Error).message ?? `Unknown Error: ${JSON.stringify(error)}`;
    message.error(text);
    console.error(error as Error);
  }
}

export function handleAxiosError(error: any) {
  {
    const errorMessage = error.response.data || `An error occurred`;
    NotificationUtil.error(errorMessage);
  }
}
