import React, { useState } from "react";
import { Upload } from "antd";
import { useMutation } from "react-query";
import axios from "axios";
import styles from "./UploadArea.module.scss";
import { uploadImage } from "../../Services/image";

interface UploadAreaProps {
  style?: React.CSSProperties;
  onUpload?: (url: string) => void;
}

const UploadArea: React.FC<UploadAreaProps> = ({ style, onUpload }) => {
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const uploadMutation = useMutation(
    ({ image }: { image: File }) => uploadImage(image, "post-imgs"),
    {
      onSuccess: (data) => {
        setImageUrl(data); // Assuming 'data.url' is the URL of the uploaded image
        onUpload?.(data);
      },
    }
  );

  const handleUpload = async (file: any) => {
    uploadMutation.mutate({ image: file });
  };

  return (
    <div className={styles.uploadContainer} style={style}>
      {!imageUrl ? (
        <Upload.Dragger
          name="file"
          customRequest={({ file }) => handleUpload(file)}
          className={styles.dragger}
          showUploadList={false}
        >
          <p className="ant-upload-text">
            Click or drag file to this area to upload
          </p>
        </Upload.Dragger>
      ) : (
        <div className={styles.imagePreview}>
          <img
            src={imageUrl}
            alt="Uploaded"
            style={{ maxWidth: "100%", maxHeight: "100%" }}
          />
        </div>
      )}
    </div>
  );
};

export default UploadArea;
