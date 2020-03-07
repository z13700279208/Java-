package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式与文件大小的转化工作
 */
public class Util {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String[] SIZE_NAMES = {"B","KB","MB","GB"};

    private static  final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    public static String parseSize(Long size) {
        int n = 0;
        while(size > 1024){
            size = size / 1024;
            n++;
        }
        return size + SIZE_NAMES[n];
    }

    public static String parseDate(Long lastModified) {
        return DATE_FORMAT.format(new Date(lastModified));
    }

    public static void main(String[] args) {
        System.out.println(parseDate(35246546846L));
        System.out.println(parseSize(54652354L));
    }
}
