import { EditFilled, PlusOutlined } from "@ant-design/icons";
import { Button, Form, Input, Modal, Upload } from "antd";
import { useState } from "react";

function EditProfile() {
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);

  const showModal = () => {
    setOpen(true);
  };

  const handleOk = () => {
    setConfirmLoading(true);
    setTimeout(() => {
      setOpen(false);
      setConfirmLoading(false);
    }, 2000);
  };

  const handleCancel = () => {
    console.log("Clicked cancel button");
    setOpen(false);
  };

  const normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };

  return (
    <>
      <Button icon={<EditFilled />} type="primary" onClick={showModal}>
        Edit Profile
      </Button>
      <Modal
        title="Edit Profile"
        open={open}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <Form
          labelCol={{ span: 4 }}
          wrapperCol={{ span: 14 }}
          layout="horizontal"
          style={{ maxWidth: 600 }}
        >
          <Form.Item
            label="Profile Photo"
            valuePropName="fileList"
            getValueFromEvent={normFile}
          >
            <Upload action="/upload.do" listType="picture-card">
              <div>
                <PlusOutlined />
                <div style={{ marginTop: 8 }}>Upload</div>
              </div>
            </Upload>
          </Form.Item>
          <Form.Item label="Username">
            <Input defaultValue="Old Name" />
          </Form.Item>
          <Form.Item label="Name">
            <Input defaultValue="Old Name" />
          </Form.Item>
          <Form.Item label="Steam link">
            <Input defaultValue="Old Link" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

export default EditProfile;
