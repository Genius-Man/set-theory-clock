package com.paic.arch.interviews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author HuiYu.Xiao
 * @date 2018/2/28
 */
public class TimeConverterImpl implements TimeConverter{

    private static final Logger LOG = LoggerFactory.getLogger(TimeConverterImpl.class);

    /**
     *
     * @param aTime
     *            HH:mm:ss格式
     * @return
     */
    @Override
    public String convertTime(String aTime) {

        String result = "";
        try {
            // 验证时间格式是否合法
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.parse(aTime);
        } catch (ParseException e) {
            LOG.error("aTime[{}] parameter is illegal ", aTime , e.getCause());
            throw new RuntimeException("parameter is illegal" + aTime);
        }

        String[] timeAry = aTime.split(":");// 时分秒
        String[] dateAry = new String[5];//保存五行灯显示

        // 1.处理第一排秒
        dateAry[0] = Integer.parseInt(timeAry[2]) % 2 == 0 ? TimeConstants.LampColor.Y.name()
                : TimeConstants.LampColor.O.name();

        // 2.处理第二排小时 (每个灯代表5个小时)
        int hours = Integer.parseInt(timeAry[0]);
        int totalRedNum = hours / 5;
        dateAry[1] = getParseResult(TimeConstants.BASE_SHOW_FOUR, 0, totalRedNum, TimeConstants.LampColor.R.name());

        // 3.处理第三排小时 (每个灯代表一个小时)
        totalRedNum = hours % 5;
        dateAry[2] = getParseResult(TimeConstants.BASE_SHOW_FOUR, 0, totalRedNum, TimeConstants.LampColor.R.name());

        int minute = Integer.parseInt(timeAry[1]);
        // 4.处理第四排(每个灯代表五分钟)
        totalRedNum = minute / 5;
        dateAry[3] = getParseResult(TimeConstants.BASE_SHOW_TWELVE, totalRedNum, TimeConstants.BASE_SHOW_TWELVE.length(),
                TimeConstants.LampColor.O.name());

        // 5.处理第五排(每个灯代表1分钟)
        totalRedNum = minute % 5;
        dateAry[4] = getParseResult(TimeConstants.BASE_SHOW_FOUR, 0, totalRedNum, TimeConstants.LampColor.Y.name());

        return String.join(System.getProperty("line.separator"),dateAry);
    }

    /**
     * 替换字符串
     * @param source 原字符串
     * @param start 起始位置
     * @param end 截止位置
     * @param replaceChar 替换字符
     * @return
     */
    public String getParseResult(String source, int start, int end, String replaceChar) {
        StringBuilder replaceStr = new StringBuilder("");
        for (int i = 0; i < end - start; i++) {
            replaceStr .append(replaceChar) ;
        }
        return new StringBuilder(source).replace(start, end, replaceStr.toString()).toString();
    }

}
