/*
 * <summary></summary>
 * <author>thyferny</author>
 * <email>thyferny@163.com</email>
 * <create-date>2014/05/2014/5/26 13:52</create-date>
 *
 * <copyright file="UnknowWord.java" company="thyferny">
 * 
 * 
 * </copyright>
 */
package in.thyferny.nlp.recognition.nr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import in.thyferny.nlp.HanLP;
import in.thyferny.nlp.algoritm.Viterbi;
import in.thyferny.nlp.corpus.dictionary.item.EnumItem;
import in.thyferny.nlp.corpus.tag.NR;
import in.thyferny.nlp.dictionary.nr.PersonDictionary;
import in.thyferny.nlp.seg.common.Vertex;
import in.thyferny.nlp.seg.common.WordNet;

/**
 * 人名识别
 * @author thyferny
 */
public class PersonRecognition
{
    public static boolean Recognition(List<Vertex> pWordSegResult, WordNet wordNetOptimum, WordNet wordNetAll)
    {
        List<EnumItem<NR>> roleTagList = roleObserve(pWordSegResult);
        if (HanLP.Config.DEBUG)
        {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = pWordSegResult.iterator();
            for (EnumItem<NR> nrEnumItem : roleTagList)
            {
                sbLog.append('[');
                sbLog.append(iterator.next().realWord);
                sbLog.append(' ');
                sbLog.append(nrEnumItem);
                sbLog.append(']');
            }
            System.out.printf("人名角色观察：%s\n", sbLog.toString());
        }
        List<NR> nrList = viterbiComputeSimply(roleTagList);
        if (HanLP.Config.DEBUG)
        {
            StringBuilder sbLog = new StringBuilder();
            Iterator<Vertex> iterator = pWordSegResult.iterator();
            sbLog.append('[');
            for (NR nr : nrList)
            {
                sbLog.append(iterator.next().realWord);
                sbLog.append('/');
                sbLog.append(nr);
                sbLog.append(" ,");
            }
            if (sbLog.length() > 1) sbLog.delete(sbLog.length() - 2, sbLog.length());
            sbLog.append(']');
            System.out.printf("人名角色标注：%s\n", sbLog.toString());
        }

        PersonDictionary.parsePattern(nrList, pWordSegResult, wordNetOptimum, wordNetAll);
        return true;
    }

    /**
     * 角色观察(从模型中加载所有词语对应的所有角色,允许进行一些规则补充)
     * @param wordSegResult 粗分结果
     * @return
     */
    public static List<EnumItem<NR>> roleObserve(List<Vertex> wordSegResult)
    {
        List<EnumItem<NR>> tagList = new LinkedList<EnumItem<NR>>();
        for (Vertex vertex : wordSegResult)
        {
            EnumItem<NR> nrEnumItem = PersonDictionary.dictionary.get(vertex.realWord);
            if (nrEnumItem == null)
            {
                switch (vertex.guessNature())
                {
                    case nr:
                    {
                        // 有些双名实际上可以构成更长的三名
                        if (vertex.getAttribute().totalFrequency <= 1000 && vertex.realWord.length() == 2)
                        {
                            nrEnumItem = new EnumItem<NR>(NR.X, NR.G);
                        }
                        else nrEnumItem = new EnumItem<NR>(NR.A, PersonDictionary.transformMatrixDictionary.getTotalFrequency(NR.A));
                    }break;
                    case nnt:
                    {
                        // 姓+职位
                        nrEnumItem = new EnumItem<NR>(NR.G, NR.K);
                    }break;
                    default:
                    {
                        nrEnumItem = new EnumItem<NR>(NR.A, PersonDictionary.transformMatrixDictionary.getTotalFrequency(NR.A));
                    }break;
                }
            }
            tagList.add(nrEnumItem);
        }
        return tagList;
    }

    /**
     * 维特比算法求解最优标签
     * @param roleTagList
     * @return
     */
    public static List<NR> viterbiCompute(List<EnumItem<NR>> roleTagList)
    {
        return Viterbi.computeEnum(roleTagList, PersonDictionary.transformMatrixDictionary);
    }

    /**
     * 简化的"维特比算法"求解最优标签
     * @param roleTagList
     * @return
     */
    public static List<NR> viterbiComputeSimply(List<EnumItem<NR>> roleTagList)
    {
        return Viterbi.computeEnumSimply(roleTagList, PersonDictionary.transformMatrixDictionary);
    }
}
