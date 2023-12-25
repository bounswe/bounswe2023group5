import { useNavigate, useSearchParams } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "react-query";
import {  useState } from "react";
import styles from "./ApplyGroup.module.scss";
import { getTags } from "../../Services/tags";
import { Button, Form, Input,  } from "antd";
import { useForm } from "antd/es/form/Form";
import { twj } from "tw-to-css";

import { applyGroup } from "../../Services/group";
import { NotificationUtil } from "../../Library/utils/notification";


function CreateGroup() {
  const [form] = useForm();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const groupId = window.location.pathname.split("/")[3];
  const queryClient = useQueryClient();


  const { mutate: submit, isLoading } = useMutation(
    ({
        message,
    }: {
        message: string;
    }) => {
      return applyGroup({
            groupId: groupId,
            message,
        }
      );
    },
    {
      onSuccess() {
        queryClient.invalidateQueries(["groups"]);
        navigate(`/groups`);
        NotificationUtil.success("You successfully applied to the group");
      },
      onError(error: any) {
        console.log(groupId)
        if(error.response.data.startsWith("You have")){
            navigate(`/groups`);
        }
        NotificationUtil.error(error.response.data);
        
        console.log(error);
      },
    }
  );

  return (
    <div className={styles.container}>
      <Form onFinish={submit} layout="vertical" form={form}>
        <Form.Item
          name="message"
          rules={[{ required: true, message: "Please enter a message" }]}
          label="Message"
        >
          <Input.TextArea
            rows={5}
            placeholder="Message to group moderator. Maybe you are a good fit for the group?"
          />
        </Form.Item>

        <Form.Item noStyle>
          <Button
            type="primary"
            htmlType="submit"
            disabled={isLoading}
            style={twj("ml-[85%]")}
          >
            Apply
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default CreateGroup;
