import { EditFilled } from "@ant-design/icons";
import { Button, Form, Input, Modal, Radio } from "antd";
import { useEffect, useState } from "react";
import { editProfile } from "../../Services/profile";
import UploadArea from "../UploadArea/UploadArea";
import { useForm } from "antd/es/form/Form";

function EditProfile({ profile }: { profile: any }) {
  const profileId = profile.id;
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState<string | undefined>();
  const [form] = useForm();

  useEffect(() => {
    form.setFieldsValue(profile);
    form.setFieldValue("username", profile?.user?.username);
    setImageUrl(profile?.profilePhoto);
  }, [profile]);

  const showModal = () => {
    setOpen(true);
  };

  const handleConfirm = async (data: any) => {
    setConfirmLoading(true);
    const newdata = { ...data, ...{ profilePhoto: imageUrl } };
    console.log(newdata);
    try {
      await editProfile(newdata, profileId);
    } catch (error) {}
    setOpen(false);
    setConfirmLoading(false);
  };

  const handleCancel = () => {
    setOpen(false);
  };

  const onFinish = async (data: any) => {
    await handleConfirm(data);
  };

  return (
    <>
      <Button icon={<EditFilled />} type="primary" onClick={showModal}>
        Edit Profile
      </Button>
      <Modal
        title="Edit Profile"
        open={open}
        onCancel={handleCancel}
        style={{
          maxWidth: "70%",
          minWidth: "700px",
          display: "flex",
          flexDirection: "column",
        }}
        footer={[
          <Button
            key="submit"
            type="primary"
            loading={confirmLoading}
            onClick={handleConfirm}
            htmlType="submit"
            form="editProfileForm"
          >
            Confirm Changes
          </Button>,
        ]}
        forceRender
      >
        <Form
          onFinish={onFinish}
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 14 }}
          layout="horizontal"
          id="editProfileForm"
          form={form}
        >
          <Form.Item label="Profile Photo">
            <UploadArea
              style={{ height: "200px", width: "200px" }}
              onUpload={setImageUrl}
            />
          </Form.Item>
          <Form.Item label="Username" name="username">
            <Input />
          </Form.Item>
          <Form.Item label="Privacy" name="isPrivate">
            <Radio.Group>
              <Radio value={true}> private </Radio>
              <Radio value={false}> public </Radio>
            </Radio.Group>
          </Form.Item>
          <Form.Item label="Steam Profile" name="steamProfile">
            <Input />
          </Form.Item>
          <Form.Item label="Epic Games Profile" name="epicGamesProfile">
            <Input />
          </Form.Item>
          <Form.Item label="XBOX Profile" name="xboxProfile">
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

export default EditProfile;
