package com.xjtu.visualization.utils;

import com.xjtu.dependency.domain.Dependency;

import java.util.List;
import java.util.Random;

/**
 * Created by shilei on 2017/3/27.
 */
public class VisualUtils {


    /**
     * 获得知识点的颜色  如果 term="中心"   是中心颜色。
     *
     * @param term
     * @return
     */
    public static String getcolor(String term) {
        String[] color = {"#0066ff"};
        Random random = new Random();

        if (term.equals("中心")) {
            return "#c00";
        } else {
            return color[random.nextInt(color.length)];
        }

    }

    public static int getSize(List<Dependency> dependencyList, Long TermID) {
        int result = 0;
        for (Dependency dependency : dependencyList) {
            if (dependency.getEndTermID() == TermID || dependency.getStartTermID() == TermID) result++;
        }
        return result;
    }


}
