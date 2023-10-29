import { message } from "antd";

export function handleError(error: any) {
  {
    const text =
      (error as Error).message ?? `Unknown Error: ${JSON.stringify(error)}`;
    message.error(text);
    console.error(error as Error);
  }
}
