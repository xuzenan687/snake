package com.xuziran.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    Integer id;
    String nickname;
    Integer score=0;
    public void setCurrentUser(Player player) {
        this.id = player.getId();
        this.nickname = player.getNickname();
        this.score = (player.getScore() == null ? 0 : player.getScore());
    }

}
