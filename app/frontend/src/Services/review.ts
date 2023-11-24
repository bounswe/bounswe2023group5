import axios from "axios";

export async function getReview(id: string) {
  const reviews = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/review`,
    {
      params: {
        id,
      },
    }
  );

  return reviews;
}

export async function getAllReviews(
  gameId: string,
  sortBy: string,
  sortDirection: string,
  reviewedBy?: string
) {
  const reviews = await axios.get(
    `${import.meta.env.VITE_APP_API_URL}/review/get-all`,
    {
      params: {
        gameId,
        reviewedBy,
        sortBy,
        sortDirection,
      },
    }
  );

  return reviews;
}

export async function postReview(review: any) {
  await axios.post(`${import.meta.env.VITE_APP_API_URL}/review/create`, review);
}

export async function updateReview(id: string, review: any) {
  await axios.put(`${import.meta.env.VITE_APP_API_URL}/review/update`, review, {
    params: {
      id,
    },
  });
}

export async function deleteReview(id: string) {
  await axios.delete(`${import.meta.env.VITE_APP_API_URL}/review/delete`, {
    params: {
      id,
    },
  });
}
