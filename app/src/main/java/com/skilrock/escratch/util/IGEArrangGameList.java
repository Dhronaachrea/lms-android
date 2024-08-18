package com.skilrock.escratch.util;

import com.skilrock.escratch.bean.GameListDataBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;



/**
 * Created by abhishekd on 5/23/2015.
 */
public class IGEArrangGameList {
    private ArrayList<GameListDataBean.Games> gameses;

    public IGEArrangGameList(ArrayList<GameListDataBean.Games> gameses) {
        this.gameses = gameses;
    }

    public LinkedHashMap<String, ArrayList<GameListDataBean.Games>> getArrangGameList() {
        LinkedHashMap<String, ArrayList<GameListDataBean.Games>> map = new LinkedHashMap<>();
        for (int i = 0; i < gameses.size(); i++) {
            GameListDataBean.Games games = gameses.get(i);
            if (map.containsKey(games.getGameCategory())) {
                ArrayList<GameListDataBean.Games> localGame = map.get(games.getGameCategory());
                localGame.add(games);
                //   map.put(games.getGameCategory(), localGame);
            } else {
                ArrayList<GameListDataBean.Games> localGame = new ArrayList<>();
                localGame.add(games);
                map.put(games.getGameCategory(), localGame);
            }
        }
        return map;
    }
}