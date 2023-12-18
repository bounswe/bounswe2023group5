package com.app.gamereview.dto.request.group;

import com.app.gamereview.model.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RecommendGroupDto implements Comparable<RecommendGroupDto>{
    private Group group;
    private int score = Integer.MAX_VALUE;

    @Override
    public int compareTo(RecommendGroupDto o) {
        if(group.getId().equals(o.getGroup().getId())) return 0;
        if(score == o.score) return 1;
        return Integer.compare(this.score, o.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendGroupDto given = (RecommendGroupDto) o;
        return this.group.getId().equals(given.getGroup().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(group.getId());
    }
}
