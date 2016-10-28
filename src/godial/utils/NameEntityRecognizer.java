package godial.utils;

import godial.dialstructure.DialEleType;

/**
 * Created by Yi Zhou on 2016/10/25.
 */
public class NameEntityRecognizer {
    public static DialEleType convert(String utterance) {
        if (utterance.matches("(Beijing|Shanghai|Guangzhou|Shenzhen)"))
            return DialEleType.LOCATION;
        if (utterance.matches("(((the \\d+th of (May|June|July))|(May|June|July) \\d+th))"))
            return DialEleType.DATE;
        if (utterance.matches("(\\d+|one|two|three|four)"))
            return DialEleType.NUMBER;
        if (utterance.matches("(yes|sure|ok)"))
            return DialEleType.CONFIRMATION;
        if (utterance.matches("(no|not)"))
            return DialEleType.CANCELLATION;
        return DialEleType.OTHER;
    }
}
