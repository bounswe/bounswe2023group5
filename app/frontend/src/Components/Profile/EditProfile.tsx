import { EditFilled } from "@ant-design/icons";
import { Button, Form, Input, Modal, Radio } from "antd";
import { useState } from "react";
import { editProfile } from "../../Services/profile";
import UploadArea from "../UploadArea/UploadArea";

function EditProfile({ editableFields }: { editableFields: any }) {
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState<string | undefined>();

  const showModal = () => {
    setOpen(true);
  };

  const handleConfirm = async (data: any) => {
    setConfirmLoading(true);
    const newdata = { ...data, ...{ profilePhoto: imageUrl } };
    console.log(newdata);
    await editProfile(newdata);
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
      >
        <Form
          onFinish={onFinish}
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 14 }}
          layout="horizontal"
          id="editProfileForm"
        >
          <Form.Item label="Profile Photo">
            <UploadArea
              style={{ height: "200px", width: "200px" }}
              onUpload={setImageUrl}
            />
          </Form.Item>
          <Form.Item label="Username" name="username">
            <Input defaultValue={editableFields?.username} />
          </Form.Item>
          <Form.Item label="Privacy" name="isPrivate">
            <Radio.Group value={editableFields?.isPrivate ? true : false}>
              <Radio value={true}> private </Radio>
              <Radio value={false}> public </Radio>
            </Radio.Group>
          </Form.Item>
          <Form.Item label="Steam Profile" name="steamProfile">
            <Input defaultValue={editableFields?.steamProfile} />
          </Form.Item>
          <Form.Item label="Epic Games Profile" name="epicGamesProfile">
            <Input defaultValue={editableFields?.epicGamesProfile} />
          </Form.Item>
          <Form.Item label="XBOX Profile" name="xboxProfile">
            <Input defaultValue={editableFields?.xboxProfile} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

export default EditProfile;
