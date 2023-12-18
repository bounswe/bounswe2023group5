import {
  Button,
  ConfigProvider,
  Descriptions,
  DescriptionsProps,
  Modal,
} from "antd";
import { useEffect, useState } from "react";
import Vibrant from "node-vibrant";
import styles from "./CharacterDetails.module.scss";
import Character from "./Character";

function CharacterDetails({ character }: { character: any }) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [palette, setPalette] = useState({} as any);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const imageUrl = `${import.meta.env.VITE_APP_IMG_URL}${character?.icon}`;

  useEffect(() => {
    const extractColors = async () => {
      Vibrant.from(imageUrl)
        .getPalette()
        .then((palette) => {
          // Access colors from the palette
          if (palette) {
            setPalette(palette);
          }
        })
        .catch((error) => {
          console.error("Error extracting color palette:", error);
        });
    };
    extractColors();
  }, [character?.icon]);

  const defaultItems: DescriptionsProps["items"] = [
    {
      key: "type",
      label: "Type",
      children: character?.type || "Unknown",
    },
    {
      key: "race",
      label: "Race",
      children: character?.race || "Unknown",
    },
    {
      key: "gender",
      label: "Gender",
      children: character?.gender || "Unknown",
    },
    {
      key: "height",
      label: "Height",
      children: character?.height || "Unknown",
    },
    {
      key: "age",
      label: "Age",
      children: character?.age || "Unknown",
    },
    {
      key: "status",
      label: "Status",
      children: character?.status || "Unknown",
    },
    {
      key: "occupation",
      label: "Occupation",
      children: character?.occupation || "Unknown",
    },
    {
      key: "voiceActor",
      label: "Voice Actor",
      children: character?.voiceActor || "Unknown",
    },
  ];

  let borderedItems: DescriptionsProps["items"];

  if (character?.customFields !== undefined) {
    borderedItems = [
      ...defaultItems,
      ...Object.keys(character?.customFields).map((key: any) => {
        return {
          key: key,
          label: key,
          children: character?.customFields[key],
        };
      }),
    ];
  } else {
    borderedItems = defaultItems;
  }

  return (
    <>
      <Character
        imgUrl={imageUrl}
        name={character?.name}
        onClick={showModal}
      ></Character>
      <ConfigProvider
        theme={{
          token: {
            colorText: palette?.LightVibrant?.hex,
          },
          components: {
            Modal: {
              contentBg: palette?.DarkMuted?.hex,
              headerBg: palette?.DarkMuted?.hex,
            },
            Descriptions: {
              labelBg: `rgba(${palette?.Vibrant?.rgb[0]}, ${palette?.Vibrant?.rgb[1]}, ${palette?.Vibrant?.rgb[2]}, 0.6)`,
            },
          },
        }}
      >
        <Modal
          title={character?.name}
          open={isModalOpen}
          onOk={handleOk}
          onCancel={handleCancel}
          footer={null}
        >
          <div className={styles.modalContainer}>
            <div className={styles.imageContainer}>
              <img src={imageUrl} alt="character" />
            </div>
            <div className={styles.description}>{character?.description}</div>
            <Descriptions
              bordered
              title="Character Attributes"
              size="middle"
              items={borderedItems}
              layout="vertical"
              style={{ overflow: "hidden" }}
            />
          </div>
        </Modal>
      </ConfigProvider>
    </>
  );
}

export default CharacterDetails;
