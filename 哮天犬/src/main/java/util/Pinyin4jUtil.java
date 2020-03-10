package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Pinyin4jUtil {

    //中文字符格式
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";
    //拼音输出格式
    private static final  HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();

    static {
          FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);//设置拼音小写
          FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置不带音调
          FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);//设置带v字符
    }


    /**
     * 简单实现
     * 1.取汉字拼音的第一个，然后拼接在一起返回
     * 2.取每个汉字拼音的第一个字符串的首字母，拼接返回
     * @param hanyu
     * @return
     */
    public static String[] get(String hanyu) {
        String[] array = new String[2];
        StringBuilder pinyin = new StringBuilder();
        StringBuilder pinyin_first = new StringBuilder();
        for (int i = 0; i < hanyu.length(); i++) {
            try {
                //API有提供单个字符的拼音转换：
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                //中文字符返回的字符串数组，可能为null或0
                if (pinyins == null || pinyins.length == 0) {
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                } else {
                    //可以转换为拼音，只取第一个字符串作为拼音
                    pinyin.append(pinyins[0]);
                    pinyin_first.append(pinyins[0].charAt(0));
                }
            } catch (Exception e) {
                //出现异常，返回原始字符
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0] = pinyin.toString();
        array[1] = pinyin_first.toString();
        return array;
    }

    //判断是否含中文
    public static boolean containsChinese(String str){
        return str.matches(".*" + CHINESE_PATTERN + ".*");
    }

    /**
     * 返回所有拼音排列组合
     * @param hanyu  字符串
     * @param fullSpell 是否为全拼
     * @retrun
     */
    public static String[][] get(String hanyu,boolean fullSpell){
        String[][] array = new String[hanyu.length()][];
        for (int i = 0; i < hanyu.length(); i++) {
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                if (pinyins == null || pinyins.length == 0) {//a -> ["a"]
                    array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                } else {
                    array[i] = unique(pinyins,fullSpell);
                }
            } catch (Exception e) {
                //出现异常，返回原始字符
                array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return array;
    }

    /**
     * @param pinyins
     * @param fullSpell  返回全拼   否则返回首字母
     * @return
     */
    private static String[] unique(String[] pinyins,boolean fullSpell) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < pinyins.length; i++) {
            if(fullSpell){
                set.add(pinyins[i]);
            }else{
                set.add(String.valueOf(pinyins[i].charAt(0)));
            }
        }
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        System.out.println(containsChinese("ss啊"));//true

        String[] s = get("中华人民共和国");
        System.out.println(Arrays.toString(s));

        for (String[] array : get("中华人民共和国",true)) {
            System.out.println(Arrays.toString(array));
        }
    }
}
