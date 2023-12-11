import { Button } from "antd";
import styles from "./Main.module.scss";
import { useNavigate } from "react-router-dom";
import { Table } from "antd";

function Main() {
  const navigate = useNavigate();

  const DeleteButton = (text: string, link: string) => {
    return (
      <Button type="primary" danger onClick={() => navigate(link)}>
        {text}
      </Button>
    );
  };

  const EditButton = (text: string, link: string) => {
    return (
      <Button type="primary" onClick={() => navigate(link)}>
        {text}
      </Button>
    );
  };

  const AddButton = (text: string, link: string) => {
    return (
      <Button
        type="primary"
        style={{ backgroundColor: "green" }}
        onClick={() => navigate(link)}
      >
        {text}
      </Button>
    );
  };

  const columns = [
    {
      title: "Game",
      dataIndex: "game",
      key: "game",
    },
    {
      title: "Tag",
      dataIndex: "tag",
      key: "tag",
    },
    {
      title: "User",
      dataIndex: "user",
      key: "user",
    },
    {
      title: "Achievement",
      key: "achievement",
      dataIndex: "achievement",
    },
  ];

  const data = [
    {
      key: "1",
      game: AddButton("Create Game", "/create-game"),
      tag: AddButton("Create Tag", "/create-tag"),
      user: DeleteButton("Ban User", "/ban-user"),
      achievement: AddButton("Create Achievement", "/create-achievement"),
    },
    {
      key: "2",
      game: EditButton("Update Game", "/update-game"),
      tag: EditButton("Update Tag", "/update-tag"),
      user: EditButton("Give Admin Permission", "/admin-permission"),
      achievement: DeleteButton("Delete Achievement", "/delete-achievement"),
    },
    {
      key: "3",
      tag: DeleteButton("Delete Tag", "/delete-tag"),
    },
  ];

  return (
    <div className={styles.container}>
      <Table
        dataSource={data}
        columns={columns}
        pagination={false}
        bordered={true}
      />
    </div>
  );
}

export default Main;
