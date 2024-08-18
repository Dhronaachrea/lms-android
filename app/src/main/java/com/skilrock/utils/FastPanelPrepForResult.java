package com.skilrock.utils;

import com.skilrock.bean.TicketDataBean;

import java.util.List;

/**
 * Created by stpl on 7/18/2015.
 */
public class FastPanelPrepForResult {

    private List<TicketDataBean.PanelData> panelDatas;

    public FastPanelPrepForResult(List<TicketDataBean.PanelData> panelDatas) {
        this.panelDatas = panelDatas;
    }

    public String getFastLottoPanel() {
        String response = "";
        for (int k = 0; k < panelDatas.size(); k++){
            TicketDataBean.PanelData data = panelDatas.get(k);
            String betAmtMul = String.valueOf(data.getBetAmtMul());
            String numb = data.getPickedNumbers();
            String fastResNum = numb.substring(numb.indexOf("(") + 1, numb.indexOf(")"));;
            if (k == 0) {
                response = response.concat("" + (fastResNum.length() == 1 ? 0 + "" + fastResNum : fastResNum) + "#" + (betAmtMul.length() == 1 ? 0 + "" + betAmtMul : betAmtMul));
            } else {
                response=response.concat("," + (fastResNum.length() == 1 ? 0 + "" + fastResNum : fastResNum) + "#" + (betAmtMul.length() == 1 ? 0 + "" + betAmtMul : betAmtMul));
            }
        }

        return response;
    }


}
