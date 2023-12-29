package com.app.gamereview.dto.request.game;

import com.app.gamereview.model.Game;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RecommendGameDto implements Comparable<RecommendGameDto>{
    private Game game;
    private int score = Integer.MAX_VALUE;

    @Override
    public int compareTo(RecommendGameDto o) {
        if(game.getGameName().equals(o.getGame().getGameName())) return 0;
        if(score == o.score) return 1;
        return Integer.compare(this.score, o.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendGameDto given = (RecommendGameDto) o;
        return this.game.getGameName().equals(given.getGame().getGameName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(game.getGameName());
    }
}
