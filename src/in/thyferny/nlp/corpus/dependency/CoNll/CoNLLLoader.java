/*
 * <summary></summary>
 * <author>thyferny</author>
 * <email>thyferny@163.com</email>
 * <create-date>2014/11/19 18:53</create-date>
 *
 * <copyright file="CoNLLLoader.java" company="thyferny">
 * 
 * 
 * </copyright>
 */
package in.thyferny.nlp.corpus.dependency.CoNll;

import java.util.LinkedList;

import in.thyferny.nlp.corpus.io.IOUtil;

/**
 * CoNLL格式依存语料加载
 * @author thyferny
 */
public class CoNLLLoader
{
    public static LinkedList<CoNLLSentence> loadSentenceList(String path)
    {
        LinkedList<CoNLLSentence> result = new LinkedList<CoNLLSentence>();
        LinkedList<CoNllLine> lineList = new LinkedList<CoNllLine>();
        for (String line : IOUtil.readLineListWithLessMemory(path))
        {
            if (line.trim().length() == 0)
            {
                result.add(new CoNLLSentence(lineList));
                lineList = new LinkedList<CoNllLine>();
                continue;
            }
            lineList.add(new CoNllLine(line.split("\t")));
        }

        return result;
    }
    public static void main(String[] args)
    {
        LinkedList<CoNLLSentence> coNLLSentences = CoNLLLoader.loadSentenceList("D:\\Doc\\语料库\\依存分析训练数据\\THU\\dev.conll.fixed.txt");
        System.out.println(coNLLSentences.getFirst());
    }
}
