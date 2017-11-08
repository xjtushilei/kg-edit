package com.xjtu.dependency.ranktext;

import com.xjtu.dependency.domain.Dependency;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RankText {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    public List<Dependency> rankText(List<Term> termList, String ClassID, int MAX) {
        List<Dependency> dependencies = new ArrayList<>();

        logger.info("Finish Hash...");
        logger.info("Start computing the hammingDistance...");
        HashMap<TwoTuple<Term, Term>, Double> disMap = new HashMap<>();
        for (int i = 0; i < termList.size(); i++) {
            for (int j = i + 1; j < termList.size(); j++) {
                Term term1 = termList.get(i);
                Term term2 = termList.get(j);
                double dis = CosineSimilarAlgorithm.getSimilarity(term1.getTermText(), term2.getTermText());
                //				logger.info(dis+"");
                TwoTuple<Term, Term> twoTuple = new TwoTuple<>(term1, term2);
                disMap.put(twoTuple, dis);
            }
        }
        logger.info("Finish computing the hammingDistance...");
        logger.info("Start ranking...");

        List<Map.Entry<TwoTuple<Term, Term>, Double>> infoIds = new ArrayList<Map.Entry<TwoTuple<Term, Term>, Double>>(disMap.entrySet());
        Collections.sort(infoIds, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        logger.info("Finish ranking!");
        logger.info("Start printing...");


        String classID = ClassID;
        int end = MAX;
        if (infoIds.size() < end) end = infoIds.size();
        logger.info("end:" + end);
        for (int k = 0; k < end; k++) {
            TwoTuple<Term, Term> twoTuple = infoIds.get(k).getKey();
            String term1_term2 = twoTuple.first.getTermName() + "_" + twoTuple.second.getTermName();
            float dis = Float.parseFloat(infoIds.get(k).getValue().toString());
            logger.info(term1_term2 + ": " + dis);

            Dependency dependency = new Dependency(classID, twoTuple.first.getTermName(), twoTuple.first.getTermID(), twoTuple.second.getTermName(), twoTuple.second.getTermID(), dis);
            //    		dependency.setClassID(classID);
            //    		dependency.setStartTermID(twoTuple.first.getTermID());
            //    		dependency.setStartTermName(twoTuple.first.getTermName());
            //    		dependency.setEndTermID(twoTuple.second.getTermID());
            //    		dependency.setEndTermName(twoTuple.second.getTermName());
            //    		dependency.setConfidence(dis);
            dependencies.add(dependency);
        }
        logger.info("Finish printing...");
        return dependencies;

    }
}
