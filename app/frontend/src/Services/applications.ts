import axios from "axios";


export const getApplications = async ({ groupId }: { groupId: string }) => {
    const response = await axios.get(
      import.meta.env.VITE_APP_API_URL + "/group/list-applications?groupId=" + groupId,
    );
    console.log(response.data);
    return response.data;
  };

  export async function reviewApplication(applicationId: string, message: string) {
    const response = await axios.post(
      `${import.meta.env.VITE_APP_API_URL}/group/review-application?applicationId=${applicationId}`,
      {
        result: message,
      }
    );
    return response.data;
  }