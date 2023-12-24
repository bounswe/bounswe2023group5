import React, { useRef } from "react";
import { Carousel } from "antd";
import { useElementSize } from "usehooks-ts";
import styles from "./RecommendationCarousel.module.scss";
import RecommendationItem from "./RecommendationItem/RecommendationItem";
import { PacmanLoader } from "react-spinners";
import { twj } from "tw-to-css";

function RecommendationCarousel({
  items,
  title,
  loading,
}: {
  items: {
    image: string;
    name: string;
    link: string;
    showName?: boolean;
  }[];
  title?: string;
  loading?: boolean;
}) {
  const [containerRef, { width }] = useElementSize();

  return (
    <div className={styles.recommendationCarouselContainer} ref={containerRef}>
      {title && <h2>{title}</h2>}
      {!loading ? (
        <Carousel
          autoplay
          slidesToShow={Math.floor(width / 150)}
          style={{ width: `${width - 20}px` }}
        >
          {items?.map((item, index) => (
            <RecommendationItem key={index} index={index + 1} {...item} />
          ))}
        </Carousel>
      ) : (
        <div style={twj("h-[200px] w-full flex justify-center items-center")}>
          <PacmanLoader color="#fbf8d8" size={30} />
        </div>
      )}
    </div>
  );
}

export default RecommendationCarousel;
