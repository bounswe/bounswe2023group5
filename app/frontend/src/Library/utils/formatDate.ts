export function formatDate(inputDate?: Date | string) {
  const date = inputDate ? new Date(inputDate) : new Date(0);
  const day = date.getDate();
  const monthIndex = date.getMonth();
  const year = date.getFullYear();

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const monthName = monthNames[monthIndex];

  return `${day} ${monthName} ${year}`;
}
