import { message } from "antd";

export function handleError(error: any) {
  {
    let text;
    if (typeof error?.response?.data === "string") {
      text = error?.response?.data;
    } else {
      text =
        (error as Error).message ?? `Unknown Error: ${JSON.stringify(error)}`;
    }
    message.error(text);
    console.error(error as Error);
  }
}
