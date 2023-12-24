import React, { useRef } from "react";
import { Carousel } from "antd";
import { useElementSize } from "usehooks-ts";
import styles from "./RecommendationCarousel.module.scss";
import RecommendationItem from "./RecommendationItem/RecommendationItem";

function RecommendationCarousel({
  items,
  title,
}: {
  items: {
    image: string;
    name: string;
    link: string;
    showName?: boolean;
  }[];
  title?: string;
}) {
  const [containerRef, { width }] = useElementSize();

  return (
    <div className={styles.recommendationCarouselContainer} ref={containerRef}>
      {title && <h2>{title}</h2>}
      <Carousel
        autoplay
        slidesToShow={Math.floor(width / 150)}
        style={{ width: `${width - 20}px` }}
      >
        {items?.map((item, index) => (
          <RecommendationItem key={index} index={index + 1} {...item} />
        ))}
      </Carousel>
    </div>
  );
}

export default RecommendationCarousel;
