import { toast, TypeOptions } from "react-toastify";

export class NotificationUtil {
  static error(message: string): void {
    NotificationUtil.notify(message, "error");
  }

  static success(message: string): void {
    NotificationUtil.notify(message, "success");
  }

  static warn(message: string): void {
    NotificationUtil.notify(message, "warning");
  }

  private static notify(message: string, type: TypeOptions) {
    toast[type](message);
  }
}
