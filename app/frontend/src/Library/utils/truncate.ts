export function truncateWithEllipsis(text: string, maxLength: number) {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + "...";
  }

  return text;
}
