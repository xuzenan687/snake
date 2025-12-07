package com.xuziran.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    Integer id=0;
    String nickname="";
    Integer score=0;
}
