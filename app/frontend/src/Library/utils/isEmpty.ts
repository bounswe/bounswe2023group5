export function isEmpty(obj: any) {
    if (obj === undefined || obj === null) {
      return true;
    }
    if (Array.isArray(obj) && obj.length === 0) {
      return true;
    }
    return false;
  }