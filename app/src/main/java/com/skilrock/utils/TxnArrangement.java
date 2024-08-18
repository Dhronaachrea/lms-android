package com.skilrock.utils;

import com.skilrock.bean.TxnBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TxnArrangement {
    private ArrayList<TxnBean.TransactionData> datas;

    public TxnArrangement(ArrayList<TxnBean.TransactionData> datas) {
        this.datas = datas;
    }

    public LinkedHashMap<String, ArrayList<TxnBean.TransactionData>> getArrangdTxnData() {
        LinkedHashMap<String, ArrayList<TxnBean.TransactionData>> map = new LinkedHashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            TxnBean.TransactionData games = datas.get(i);
//            datas.remove(i);
            if (map.containsKey(games.getTransCategory())) {
                ArrayList<TxnBean.TransactionData> localGame = map.get(games.getTransCategory());
                localGame.add(games);
                map.put(games.getTransCategory(), localGame);
            } else {
                ArrayList<TxnBean.TransactionData> localGame = new ArrayList<>();
                localGame.add(games);
                map.put(games.getTransCategory(), localGame);
            }
        }
        return map;
    }
}