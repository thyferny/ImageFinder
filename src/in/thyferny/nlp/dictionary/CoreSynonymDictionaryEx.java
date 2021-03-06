/*
 * <summary></summary>
 * <author>thyferny</author>
 * <email>thyferny@163.com</email>
 * <create-date>2014/9/13 13:12</create-date>
 *
 * <copyright file="CoreSynonymDictionary.java" company="thyferny">
 * 
 * 
 * </copyright>
 */
package in.thyferny.nlp.dictionary;

import static in.thyferny.nlp.utility.Predefine.logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import in.thyferny.nlp.HanLP;
import in.thyferny.nlp.algoritm.EditDistance;
import in.thyferny.nlp.dictionary.common.CommonSynonymDictionary;
import in.thyferny.nlp.dictionary.common.CommonSynonymDictionaryEx;
import in.thyferny.nlp.dictionary.stopword.CoreStopWordDictionary;
import in.thyferny.nlp.seg.common.Term;
import in.thyferny.utils.TextUtility;
/**
 * 核心同义词词典
 *
 * @author thyferny
 */
public class CoreSynonymDictionaryEx
{
    static CommonSynonymDictionaryEx dictionary;

    static
    {
        try
        {
            dictionary = CommonSynonymDictionaryEx.create(new FileInputStream(HanLP.Config.CoreSynonymDictionaryDictionaryPath));
        }
        catch (Exception e)
        {
            System.err.println("载入核心同义词词典失败" + e);
            System.exit(-1);
        }
    }

    public static Long[] get(String key)
    {
        return dictionary.get(key);
    }

    /**
     * 语义距离
     * @param itemA
     * @param itemB
     * @return
     */
    public static long distance(CommonSynonymDictionary.SynonymItem itemA, CommonSynonymDictionary.SynonymItem itemB)
    {
        return itemA.distance(itemB);
    }

    /**
     * 将分词结果转换为同义词列表
     * @param sentence 句子
     * @param withUndefinedItem 是否保留词典中没有的词语
     * @return
     */
    public static List<Long[]> convert(List<Term> sentence, boolean withUndefinedItem)
    {
        List<Long[]> synonymItemList = new ArrayList<Long[]>(sentence.size());
        for (Term term : sentence)
        {
            // 除掉停用词
            if (term.nature == null) continue;
            String nature = term.nature.toString();
            char firstChar = nature.charAt(0);
            switch (firstChar)
            {
                case 'm':
                {
                    if (!TextUtility.isAllChinese(term.word)) continue;
                }break;
                case 'w':
                {
                    continue;
                }
            }
            // 停用词
            if (CoreStopWordDictionary.contains(term.word)) continue;
            Long[] item = get(term.word);
//            logger.trace("{} {}", wordResult.word, Arrays.toString(item));
            if (item == null)
            {
                if (withUndefinedItem)
                {
                    item = new Long[]{Long.MAX_VALUE / 3};
                    synonymItemList.add(item);
                }

            }
            else
            {
                synonymItemList.add(item);
            }
        }

        return synonymItemList;
    }

    /**
     * 获取语义标签
     * @return
     */
    public static long[] getLexemeArray(List<CommonSynonymDictionary.SynonymItem> synonymItemList)
    {
        long[] array = new long[synonymItemList.size()];
        int i = 0;
        for (CommonSynonymDictionary.SynonymItem item : synonymItemList)
        {
            array[i++] = item.entry.id;
        }
        return array;
    }


    public long distance(List<CommonSynonymDictionary.SynonymItem> synonymItemListA, List<CommonSynonymDictionary.SynonymItem> synonymItemListB)
    {
        return EditDistance.compute(synonymItemListA, synonymItemListB);
    }

    public long distance(long[] arrayA, long[] arrayB)
    {
        return EditDistance.compute(arrayA, arrayB);
    }
}
