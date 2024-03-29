import {
  useLocation,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";
import styles from "./ForumPost.module.scss";
import { useMutation, useQuery } from "react-query";
import { getPost } from "../../Services/forum";
import { getCommentList } from "../../Services/comment";
import CommentForm from "../../Components/Comment/CommentForm/CommentForm.tsx";
import Comment from "../../Components/Comment/Comment/Comment.tsx";
import { useVote } from "../../Components/Hooks/useVote.tsx";
import { useAuth } from "../../Components/Hooks/useAuth.tsx";
import {
  DownOutlined,
  EditOutlined,
  UpOutlined,
  CommentOutlined,
  WarningOutlined,
  ArrowLeftOutlined,
  CheckOutlined,
  InfoCircleOutlined,
} from "@ant-design/icons";
import clsx from "clsx";
import { Button, Tooltip } from "antd";
import { useEffect, useState } from "react";
import TagRenderer from "../../Components/TagRenderer/TagRenderer.tsx";
import { twj } from "tw-to-css";
import Achievement from "../../Components/Achievement/Achievement/Achievement.tsx";
import { grantAchievement } from "../../Services/achievement.ts";
import { formatDate } from "../../Library/utils/formatDate.ts";
import {
  handleAxiosError,
  handleError,
} from "../../Library/utils/handleError.ts";
import { Recogito } from "@recogito/recogito-js";
import "@recogito/recogito-js/dist/recogito.min.css";
import {
  createAnnotation,
  deleteAnnotation,
  updateAnnotation,
} from "../../Services/annotation.ts";
import { NotificationUtil } from "../../Library/utils/notification.ts";
import { Annotorious } from "@recogito/annotorious";
import "@recogito/annotorious/dist/annotorious.min.css";
import CharacterDetails from "../../Components/Character/CharacterDetails.tsx";

function ForumPost() {
  const { isLoggedIn, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();
  const [isAnnotationsApplied, setIsAnnotationsApplied] = useState(false);
  const [isImageAnnotationsApplied, setIsImageAnnotationsApplied] =
    useState(false);
  const { postId, forumId } = useParams();
  const { data: post, isLoading } = useQuery(["post", postId], () =>
    getPost(postId!)
  );
  const pageUrl = window.location.href.replace("?back=/home", "");

  const isAdmin = user?.role === "ADMIN";
  const { upvote, downvote } = useVote({
    voteType: "POST",
    typeId: postId!,
    invalidateKeys: [
      ["forum", forumId],
      ["post", postId],
    ],
  });
  const { data: comments, isLoading: isLoadingComments } = useQuery(
    ["comments", postId],
    () => getCommentList({ postId: postId! })
  );

  const hideTagField = () => {
    const tagField = document.querySelectorAll(".r6o-widget.r6o-tag");

    if (tagField && tagField.length > 0) {
      for (const elem of tagField) {
        elem.style.display = "none";
      }
    }
  };

  useEffect(() => {
    const textElement = document.querySelector("#textElement");

    if (textElement) {
      textElement.addEventListener("click", hideTagField);
    }
  }, [isLoading]);

  useEffect(() => {
    const imageElement = document.querySelector("#imageElement");

    if (imageElement) {
      imageElement.addEventListener("click", hideTagField);
    }
  }, [isLoading]);

  const [isCommenting, setCommenting] = useState(false);

  const { mutate: grant } = useMutation(
    () => grantAchievement(post.poster.id, post.achievement.id),
    {
      onSuccess() {
        NotificationUtil.success(`Achievement Granted`);
      },

      onError(err: any) {
        handleError(err);
      },
    }
  );

  const toggleCommenting = () => {
    setCommenting(!isCommenting);
  };

  const linkAnnotation = (elem: any) => {
    if (elem && isAnnotationsApplied === false) {
      const r = new Recogito({
        content: elem,
        readOnly: !(user?.id === post.poster.id || isAdmin),
      });
      setIsAnnotationsApplied(true);

      r.loadAnnotations(
        `${
          import.meta.env.VITE_APP_ANNOTATION_API_URL
        }/annotation/get-source-annotations?source=${pageUrl}`
      )
        .then(function (annotations) {})
        .catch((error) => {
          if (error instanceof SyntaxError) {
            return;
          }
          NotificationUtil.error("Error occurred while retrieving annotations");
        });

      r.on("createAnnotation", async (annotation: any, overrideId) => {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          const newId = pageUrl + "/" + annotation.id.replace("#", "");
          annotation.id = newId;
          overrideId(newId);
          await createAnnotation(annotation);
          NotificationUtil.success("You successfully create the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      r.on("deleteAnnotation", async function (annotation: any) {
        try {
          const id = annotation.id;
          await deleteAnnotation(id);
          NotificationUtil.success("You successfully delete the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      r.on("selectAnnotation", async function (annotation: any) {
        hideTagField();
      });

      r.on("updateAnnotation", async function (annotation, _previous) {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          await updateAnnotation(annotation);
          NotificationUtil.success("You successfully update the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });
    }
  };

  const linkImageAnnotation = (elem: any) => {
    if (elem && isImageAnnotationsApplied === false) {
      const config = {
        image: elem,
        readOnly: !(user?.id === post.poster.id || isAdmin),
      };

      const anno = new Annotorious(config);

      setIsImageAnnotationsApplied(true);

      anno
        .loadAnnotations(
          `${
            import.meta.env.VITE_APP_ANNOTATION_API_URL
          }/annotation/get-image-annotations?source=${pageUrl}`
        )
        .then(function (annotations) {})
        .catch((error) => {
          if (error instanceof SyntaxError) {
            return;
          }
          NotificationUtil.error("Error occurred while retrieving annotations");
        });

      anno.on("createAnnotation", async (annotation: any, overrideId) => {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          const newId = pageUrl + "/" + annotation.id.replace("#", "");
          annotation.id = newId;
          await createAnnotation(annotation);
          NotificationUtil.success("You successfully create the annotation");
        } catch (error) {
          console.log(error);
          handleAxiosError(error);
        }
      });

      anno.on("deleteAnnotation", async function (annotation: any) {
        try {
          const id = annotation.id;
          await deleteAnnotation(id);
          NotificationUtil.success("You successfully delete the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });

      anno.on("selectAnnotation", async function (annotation: any) {
        hideTagField();
      });

      anno.on("updateAnnotation", async function (annotation, _previous) {
        try {
          annotation.target = { ...annotation.target, source: pageUrl };
          await updateAnnotation(annotation);
          NotificationUtil.success("You successfully update the annotation");
        } catch (error) {
          handleAxiosError(error);
        }
      });
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.topContainer}>
        <div
          className={styles.backButton}
          onClick={() => navigate(searchParams.get("back") ?? "/")}
        >
          <ArrowLeftOutlined />
        </div>
        <Tooltip title="This page is annotable. If you are an admin or the owner of the post, you can add, edit, and delete annotations to image or content of the post.">
          <InfoCircleOutlined style={{ fontSize: "20px" }} />
        </Tooltip>
      </div>
      {!isLoading && (
        <div className={styles.postContainer}>
          {user?.id === post.poster.id && (
            <div className={styles.edit}>
              <Button
                onClick={() =>
                  navigate(
                    `/forum/form?forumId=${forumId}&&redirect=${location.pathname}&&editId=${postId}`
                  )
                }
              >
                <EditOutlined />
              </Button>
            </div>
          )}
          <div className={styles.title}>
            <div className={styles.vote}>
              <Button
                type="primary"
                shape="circle"
                icon={<UpOutlined />}
                size="small"
                onClick={upvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "UPVOTE" && styles.active)}
              />
              <div>{post.overallVote}</div>

              <Button
                type="primary"
                shape="circle"
                size="small"
                icon={<DownOutlined />}
                onClick={downvote}
                disabled={!isLoggedIn}
                className={clsx(post?.userVote === "DOWNVOTE" && styles.active)}
              />
            </div>

            <h2>{post.title}</h2>

            <TagRenderer tags={post.tags} />
          </div>{" "}
          {post.postImage && (
            <div className={styles.image} id="imageElement">
              <img
                ref={(elem) => linkImageAnnotation(elem)}
                height="30px"
                src={`${import.meta.env.VITE_APP_IMG_URL}${post.postImage}`}
              />
            </div>
          )}
          {post.achievement && (
            <div>
              <div className={styles.achievement}>
                <span className={styles.mySpan}>Achievement:</span>
                <Achievement props={post.achievement} />
                {user?.role === "ADMIN" && (
                  <Tooltip
                    title="Grant Achievement"
                    className={styles.grantButton}
                  >
                    <CheckOutlined onClick={() => grant()} />
                  </Tooltip>
                )}
              </div>
            </div>
          )}
          {post.character && (
            <div className={styles.character}>
              <span>Character:</span>

              <CharacterDetails character={post?.character} />
            </div>
          )}
          <span className={styles.body}>
            <span id="textElement" ref={(elem) => linkAnnotation(elem)}>
              {post.postContent}
            </span>
            <span className={styles.postDetails}>
              <span>Poster: {post.poster?.username}</span>
              <span>Last edit: {formatDate(post.lastEditedAt)}</span>
            </span>
          </span>
          <div className={styles.comment}>
            <div style={twj("flex gap-2 pt-2")}>
              <Button
                size="small"
                icon={<CommentOutlined style={{ color: "#555064" }} />}
                onClick={() => {
                  toggleCommenting();
                }}
              >
                Comment{" "}
              </Button>
            </div>
            {isCommenting && <CommentForm />}
          </div>
        </div>
      )}

      <div className={styles.commentTitle}>Comments</div>
      {!isLoadingComments &&
        comments?.map(
          (comment: any) =>
            !comment.isDeleted && (
              <Comment comment={comment} postId={postId!} key={comment.id} />
            )
        )}
    </div>
  );
}

export default ForumPost;
