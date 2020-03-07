package com.atherys.battlegrounds.model.entity;

import com.atherys.core.db.Identifiable;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "player_ranking_sequence", initialValue = 1, allocationSize = 1)
public class PlayerRanking implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int rankPosition;

    public PlayerRanking() {
    }

    @Nonnull
    @Override
    public Integer getId() {
        return rankPosition;
    }

    public int getPosition() {
        return rankPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRanking that = (PlayerRanking) o;
        return rankPosition == that.rankPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rankPosition);
    }

    @Override
    public String toString() {
        return "PlayerRanking{" +
                "rankPosition=" + rankPosition +
                '}';
    }
}
