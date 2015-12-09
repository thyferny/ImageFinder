
package org.trimps.images.analyzer.harris.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trimps.images.analyzer.ModifiableConst;
import org.trimps.images.analyzer.harissurf.SURFInterestPointN;
import org.trimps.images.analyzer.kdtree.KDTree;


public class SurfMatchPoints {

    private static class _mylist extends ArrayList<SurfMatch> {

        private static final long serialVersionUID = -1672787720681683109L;

        public void removeRange(int formIndex, int toIndex) {
            super.removeRange(formIndex, toIndex);
        }
    }

    public static List<SurfMatch> findMatchesBBF(List<SURFInterestPointN> keys1, List<SURFInterestPointN> keys2) {
        return findMatchesBBF(keys1, KDTree.createKDTree(keys2));
    }

    public static List<SurfMatch> findMatchesBBF(List<SURFInterestPointN> keys1, KDTree kd) {
        List<SurfMatch> matches = new _mylist();
        for (SURFInterestPointN sp : keys1) {
            ArrayList<KDTree.BestEntry> kpNNList = kd.nearestNeighbourListBBF(sp, 2, 40);
            if (kpNNList.size() < 2) throw (new IllegalArgumentException("BUG: less than two neighbours!"));
            KDTree.BestEntry be1 = (KDTree.BestEntry) kpNNList.get(0);
            KDTree.BestEntry be2 = (KDTree.BestEntry) kpNNList.get(1);
            if ((be1.getDist() / be2.getDist()) > ModifiableConst.getTowPntDistRatio()) continue;
            // XXX:最近邻点和次近邻点的距离比值
            SURFInterestPointN kpN = (SURFInterestPointN) be1.getNeighbour();
            if (Math.abs(kpN.getOrientation() - sp.getOrientation()) > ModifiableConst.getTowPntOrientationMinus())
            // continue;
            // if (Math.abs(kpN.getScale() - kp.getScale()) > ModifiableConst.getTowPntScaleMinus()) continue;
            matches.add(new SurfMatch(sp, kpN, be1.getDist(), be2.getDist()));
        }
        return (matches);
    }

    

    public static ArrayList<SurfMatch> filterJoins(List<SurfMatch> matches) {
        Map<SURFInterestPointN, Integer> map = new HashMap<SURFInterestPointN, Integer>();

        // Count the references to each keypoint
        for (SurfMatch m : matches) {
            Integer kp1V = map.get(m.getSp1());
            int lI = (kp1V == null) ? 0 : (int) kp1V;
            map.put(m.getSp1(), lI + 1);
            Integer kp2V = map.get(m.getSp2());
            int rI = (kp2V == null) ? 0 : (int) kp2V;
            map.put(m.getSp2(), rI + 1);
        }
        ArrayList<SurfMatch> survivors = new ArrayList<SurfMatch>();
        for (SurfMatch m : matches) {
            Integer kp1V = map.get(m.getSp1());
            Integer kp2V = map.get(m.getSp2());
            if (kp1V <= 1 && kp2V <= 1) survivors.add(m);
        }
        return (survivors);
    }

    public static void filterNBest(ArrayList<SurfMatch> matches, int bestQ) {
        Collections.sort(matches, new SurfMatch.MatchWeighter());
        if (matches.size() > bestQ) {
            ((_mylist) matches).removeRange(bestQ, matches.size() - bestQ);
        }
    }

    public static List<SurfMatch> filterFarMatchL(List<SurfMatch> matches, double minX, double minY) {
        int arcStep = ModifiableConst.getSolpeArcStep();
        if (matches.size() <= 1) return matches;

        int max_vote_count = 0;
        long max_vote = 0;

        int[] ms = new int[90 / arcStep + 1]; // 用数据的索引拂过每个度数的key，不使用map来保存，性能优化
        for (SurfMatch m : matches) {
            if (Math.abs(m.sp2.getOrientation() - m.sp1.getOrientation()) > 0.1) {
                continue;
            }
            double r = Math.atan((m.sp2.getY() + minY - m.sp1.getY()) / (m.sp2.getX() + minX - m.sp1.getX())) * 360
                       / (2 * Math.PI);
            if (r < 0) r += 90;
            int idx = (int) r / arcStep; // 取整

            ms[idx] = ms[idx] + 1;
            if (ms[idx] > max_vote_count) {
                max_vote_count = ms[idx];
                max_vote = idx;
            }
            m.slopeArc = idx;   
        }

        ArrayList<SurfMatch> survivors = new ArrayList<SurfMatch>();
        for (SurfMatch m : matches) {
            if (m.slopeArc == max_vote) survivors.add(m);
        }
        return survivors;
    }

    public static List<SurfMatch> filterFarMatchR(List<SurfMatch> matches, double minX, double minY) {
        int arcStep = ModifiableConst.getSolpeArcStep();
        if (matches.size() <= 1) return matches;
        int[] ms = new int[90 / arcStep]; // 用数据的索引拂过每个度数的key，不使用map来保存，性能优化
        for (SurfMatch m : matches) {
            double r = Math.atan((m.sp1.getY() - (m.sp2.getY() + minY)) / (m.sp1.getX() + minX - m.sp2.getX())) * 360
                       / (2 * Math.PI);
            m.slopeArc = ((int) r / arcStep * arcStep); // 第一次计算就把 match的斜率保存起来。
            if (m.slopeArc < 0) m.slopeArc += 90;
            ms[m.slopeArc / arcStep] = ms[m.slopeArc / arcStep] + 1;
        }
        int count = 0;
        int idx = 0;
        for (int i = 0; i < ms.length; i++) {// 找到斜率相同的最多的一个度数
            if (ms[i] > count) {
                count = ms[i];
                idx = i;
            }
        }
        idx = idx * arcStep;
        ArrayList<SurfMatch> survivors = new ArrayList<SurfMatch>();
        for (SurfMatch m : matches) {
            if (m.slopeArc == idx) survivors.add(m);
        }
        return survivors;

    }

}
