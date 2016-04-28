/*
 * <summary></summary>
 * <author>thyferny</author>
 * <email>thyferny@163.com</email>
 * <create-date>2014/11/25 20:53</create-date>
 *
 * <copyright file="MaxEntDependencyModelMaker.java" company="thyferny">
 * 
 * 
 * </copyright>
 */
package in.thyferny.nlp.corpus.dependency.model;

import java.io.*;
import java.util.*;

import in.thyferny.nlp.corpus.dependency.CoNll.CoNLLLoader;
import in.thyferny.nlp.corpus.dependency.CoNll.CoNLLSentence;
import in.thyferny.nlp.corpus.dependency.CoNll.CoNLLWord;
import in.thyferny.nlp.corpus.dictionary.DictionaryMaker;
import in.thyferny.nlp.corpus.io.IOUtil;

/**
 * 最大熵模型构建工具，训练暂时不使用自己的代码，借用opennlp训练。本maker只生成训练文件
 *
 * @author thyferny
 */
public class MaxEntDependencyModelMaker
{
    public static boolean makeModel(String corpusLoadPath, String modelSavePath) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelSavePath)));
        LinkedList<CoNLLSentence> sentenceList = CoNLLLoader.loadSentenceList(corpusLoadPath);
        int id = 1;
        for (CoNLLSentence sentence : sentenceList)
        {
            System.out.printf("%d / %d...", id++, sentenceList.size());
            String[][] edgeArray = sentence.getEdgeArray();
            CoNLLWord[] word = sentence.getWordArrayWithRoot();
            for (int i = 0; i < word.length; ++i)
            {
                for (int j = 0; j < word.length; ++j)
                {
                    if (i == j) continue;
                    // 这就是一个边的实例，从i出发，到j，当然它可能存在也可能不存在，不存在取null照样是一个实例
                    List<String> contextList = new LinkedList<String>();
                    // 先生成i和j的原子特征
                    contextList.addAll(generateSingleWordContext(word, i, "i"));
                    contextList.addAll(generateSingleWordContext(word, j, "j"));
                    // 然后生成二元组的特征
                    contextList.addAll(generateUniContext(word, i, j));
                    // 将特征字符串化
                    for (String f : contextList)
                    {
                        bw.write(f);
                        bw.write(' ');
                    }
                    // 事件名称为依存关系
                    bw.write("" + edgeArray[i][j]);
                    bw.newLine();
                }
            }
            System.out.println("done.");
        }
        bw.close();
        return true;
    }

    public static Collection<String> generateSingleWordContext(CoNLLWord[] word, int index, String mark)
    {
        Collection<String> context = new LinkedList<String>();
        for (int i = index - 2; i < index + 2 + 1; ++i)
        {
            CoNLLWord w = i >= 0 && i < word.length ? word[i] : CoNLLWord.NULL;
            context.add(w.NAME + mark + (i - index));      // 在尾巴上做个标记，不然特征冲突了
            context.add(w.POSTAG + mark + (i - index));
        }

        return context;
    }

    public static Collection<String> generateUniContext(CoNLLWord[] word, int i, int j)
    {
        Collection<String> context = new LinkedList<String>();
        context.add(word[i].NAME + '→' + word[j].NAME);
        context.add(word[i].POSTAG + '→' + word[j].POSTAG);
        context.add(word[i].NAME + '→' + word[j].NAME + (i - j));
        context.add(word[i].POSTAG + '→' + word[j].POSTAG + (i - j));
        CoNLLWord wordBeforeI = i - 1 >= 0 ? word[i - 1] : CoNLLWord.NULL;
        CoNLLWord wordBeforeJ = j - 1 >= 0 ? word[j - 1] : CoNLLWord.NULL;
        context.add(wordBeforeI.NAME + '@' + word[i].NAME + '→' + word[j].NAME);
        context.add(word[i].NAME + '→' + wordBeforeJ.NAME + '@' + word[j].NAME);
        context.add(wordBeforeI.POSTAG + '@' + word[i].POSTAG + '→' + word[j].POSTAG);
        context.add(word[i].POSTAG + '→' + wordBeforeJ.POSTAG + '@' + word[j].POSTAG);
        return context;
    }

    public static void main(String[] args) throws IOException
    {
        makeModel("D:\\Doc\\语料库\\依存分析训练数据\\THU\\train.conll.fixed.txt", "data/model/dependency/MaxEntTrain.txt");
//        makeModel("D:\\Doc\\语料库\\依存分析训练数据\\THU\\out.txt", "data/model/dependency/MaxEntTrain.test.txt");
    }
}
