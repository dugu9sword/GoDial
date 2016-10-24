package godial.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouyi on 16-10-24.
 */
public class RegexUtil {

    static Log log = LogFactory.getLog(RegexUtil.class);

    /**
     * This method can be used to extract information from a string using regex-plus.
     * The string to be analyzed should be in the pattern like belows:
     * REGEX-EXPRESSION-WITH-SLOTS;{SLOT-1}=REGEX-1,{SLOT-2}=REGEX-2,...
     * Then the value of all slots will be extracted.
     * <p>
     * For example, suppose a string is:
     * I will fly to Beijing on the 6th of May, what about you?
     * And the regex-plus is like this:
     * (on the {day}th of {month})|(on {month} {day}th);{day}=\d+,{month}=(May|June|July)
     * Then the return value will be:
     * day = 6
     * month = May
     *
     * @param word
     * @param regexplus
     * @return
     */
    public static HashMap<String, String> extract(String word, String regexplus) {
        HashMap<String, String> map = new HashMap<>();
        String regexmain = StringUtils.split(regexplus, ";")[0];
        String[] regexsub = StringUtils.split(regexplus, ";")[1].split(",");
        String[][] regexslot = new String[regexsub.length][];
        for (int i = 0; i < regexsub.length; i++) {
            regexslot[i] = new String[2];
            regexslot[i][0] = StringUtils.split(regexsub[i], "=")[0];
            regexslot[i][1] = StringUtils.split(regexsub[i], "=")[1];
//            log.info(regexslot[i][0]+" "+regexslot[i][1]);
        }

        for (int i = 0; i < regexslot.length; i++)
            regexmain = StringUtils.replace(regexmain, regexslot[i][0], regexslot[i][1]);
//        log.info("regex is "+regexmain);
//        log.info("word is "+word);

        Matcher matcher = Pattern.compile(regexmain).matcher(word);
        if (matcher.find()) {
            String extracted = matcher.group();
            for (int i = 0; i < regexslot.length; i++) {
                Matcher slotmatcher = Pattern.compile(regexslot[i][1]).matcher(extracted);
//                log.info(regexslot[i][1]);
                slotmatcher.find();
                String value = slotmatcher.group();
                String slot = regexslot[i][0].substring(1, regexslot[i][0].length() - 1);
                map.put(slot, value);
            }
            return map;
        } else return null;
    }
}
