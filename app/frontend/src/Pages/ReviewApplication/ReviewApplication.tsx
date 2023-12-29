import { Button, Select } from "antd";
import PrivateGroup from "../../Components/Groups/PrivateGroup";
import PublicGroup from "../../Components/Groups/PublicGroup";
import styles from "./ReviewApplication.module.scss";
import { useQuery } from "react-query";
import { getGames } from "../../Services/games";
import Search from "antd/es/input/Search";
import { useState } from "react";
import { getGroups } from "../../Services/groups";
import {
  PlusOutlined,
  SortAscendingOutlined,
  SortDescendingOutlined,
} from "@ant-design/icons";
import { getTags } from "../../Services/tags";
import { useNavigate, useSearchParams } from "react-router-dom";
import Application from "../../Components/Application/Application";
import { getApplications } from "../../Services/applications";
import { Group } from "antd/es/avatar";

function ReviewApplication() {
  const navigate = useNavigate();

  const membershipOptions = [
    { value: "PRIVATE", label: "Private" },
    { value: "PUBLIC", label: "Public" },
    { value: null, label: "All" },
  ];

  const groupId = window.location.pathname.split("/")[3];

  const [title, setTitle] = useState("");
  const [tags, setTags] = useState([]);

  const { data: applications, isLoading: isLoadingComments } = useQuery(
    ["applications", groupId],
    () => getApplications({ groupId: groupId! })
  );

  return (
    <div className={styles.groupsPage}>
      <div className={styles.groupsContainer}>
        <Search
          placeholder="Search applications by applicant"
          enterButton
          className={styles.search}
          onSearch={setTitle}
          onChange={(e) => {
            e.target.value === "" ? setTitle("") : "";
          }}
          style={{ width: "50%" }}
        />

        {applications &&
          applications.map((group: any) => (
            <Application key={group.id} application={group}></Application>
          ))}
      </div>
    </div>
  );
}

export default ReviewApplication;
