import { NotificationUtil } from "./notification";

export function handleError(error: any) {
  {
    let text;
    if (typeof error?.response?.data === "string") {
      text = error?.response?.data;
    } else {
      text =
        (error as Error).message ?? `Unknown Error: ${JSON.stringify(error)}`;
    }
    NotificationUtil.error(text);
  }
}

export function handleAxiosError(error: any) {
  {
    const errorMessage = error?.response?.data;

    NotificationUtil.error(
      errorMessage && !isObject(errorMessage)
        ? errorMessage
        : `An error occurred`
    );
  }
}

function isObject(variable: any) {
  return typeof variable === "object" && variable !== null;
}
