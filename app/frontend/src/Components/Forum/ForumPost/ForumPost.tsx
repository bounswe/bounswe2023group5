import { Button } from "antd";
import { formatDate } from "../../../Library/utils/formatDate";
import styles from "./ForumPost.module.scss";
import {
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  UpOutlined,
} from "@ant-design/icons";
import { useMutation } from "react-query";
import { deletePost } from "../../../Services/forum";
import { useAuth } from "../../Hooks/useAuth";
import { useVote } from "../../Hooks/useVote";
import clsx from "clsx";
import { truncateWithEllipsis } from "../../../Library/utils/truncate";
import { useNavigate } from "react-router-dom";
import TagRenderer from "../../TagRenderer/TagRenderer";
import { twj } from "tw-to-css";
import { NotificationUtil } from "../../../Library/utils/notification";
import { handleAxiosError } from "../../../Library/utils/handleError";
import SquareAchievement from "../../Achievement/SquareAchievement/SquareAchievement";
import Character from "../../Character/Character";
import CharacterDetails from "../../Character/CharacterDetails";

function ForumPost({
  post,
  forumId,
  redirect = "/",
  gameId,
  type = "STANDARD",
  typeName,
  typeId,
}: {
  post: any;
  forumId: string;
  redirect?: string;
  gameId?: string;
  type?: "STANDARD" | "GROUP" | "GAME";
  typeName?: string;
  typeId?: string;
}) {
  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const isAdmin = user?.role === "ADMIN";
  const deletePostMutation = useMutation(deletePost, {
    onSuccess: async () => {
      NotificationUtil.success("You successfully delete the post.");
    },
    onError: (error) => {
      handleAxiosError(error);
    },
  });

  const handleDelete = () => {
    deletePostMutation.mutate(post.id);
  };

  const { upvote, downvote } = useVote({
    voteType: "POST",
    typeId: post.id,
    invalidateKeys: [
      ["forum", forumId],
      ["post", post.id],
    ],
  });
  const typeStyle =
    type === "GAME" ? styles.game : type === "GROUP" ? styles.group : undefined;

  return (
    <div className={clsx(styles.container, typeStyle)}>
      <div className={styles.vote}>
        <Button
          type="primary"
          shape="circle"
          icon={<UpOutlined />}
          onClick={upvote}
          disabled={!isLoggedIn}
          className={clsx(post?.userVote === "UPVOTE" && styles.active)}
        />
        <div>{post.overallVote}</div>

        <Button
          type="primary"
          shape="circle"
          icon={<DownOutlined />}
          onClick={downvote}
          disabled={!isLoggedIn}
          className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
        />
      </div>

      {(post.postImage || post.achievement || post.character) && (
        <div className={styles.imgConatiner}>
          {post.postImage && (
            <img
              height="30px"
              src={`${import.meta.env.VITE_APP_IMG_URL}${post.postImage}`}
            />
          )}
          {post.achievement && <SquareAchievement props={post.achievement} />}

          {post.character && (
            <CharacterDetails
              character={post.character}
              className={styles.character}
            />
          )}
        </div>
      )}

      <div className={styles.titleContainer}>
        <div className={styles.title}>{post.title}</div>
        {isAdmin && (
          <DeleteOutlined style={{ color: "red" }} onClick={handleDelete} />
        )}
        <span style={twj("text-xs")}>
          <TagRenderer tags={post.tags} />
        </span>
      </div>

      <div className={styles.content}>
        {truncateWithEllipsis(post.postContent, 300)}
      </div>

      <div className={styles.meta}>
        <span>{post.poster?.username}</span>
        <span>{post.createdAt && formatDate(post.createdAt)}</span>
      </div>
      <div className={styles.readMore}>
        {type !== "STANDARD" && (
          <Button
            onClick={() =>
              type === "GAME"
                ? navigate(`/game/detail/${typeId}?subPage=forum`)
                : navigate(`/group/detail/${typeId}?subPage=forum`)
            }
            style={twj(
              "bg-transparent border-2 border-white text-sm font-bold"
            )}
            size="small"
          >
            {typeName}
          </Button>
        )}
        <Button
          onClick={() =>
            navigate(`/forum/detail/${forumId}/${post.id}?back=${redirect}`)
          }
        >
          Read More
        </Button>
      </div>
      {user?.id === post.poster?.id && (
        <div className={styles.edit}>
          <Button
            onClick={() =>
              navigate(
                `/forum/form?forumId=${forumId}&&redirect=${redirect}&&editId=${
                  post.id
                }${gameId ? `&&gameId=${gameId}` : ``}`
              )
            }
          >
            <EditOutlined />
          </Button>
        </div>
      )}
    </div>
  );
}

export default ForumPost;
