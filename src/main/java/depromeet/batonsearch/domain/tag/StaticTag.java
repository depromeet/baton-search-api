package depromeet.batonsearch.domain.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticTag {
    static {
        Map<String, Integer> tempTagToKey = new HashMap<>();
        Map<Integer, String> tempKeyToTag = new HashMap<>();

        String[] tags = {
                "친절한 선생님",
                "체계적인 수업",
                "맞춤케어",
                "넓은 시설",
                "다양한 기구",
                "최신 기구",
                "사람이 많은",
                "사람이 적은",
                "쾌적한 환경",
                "조용한 분위기",
                "역세권"
        };

        for (int i = 0; i < tags.length; i++) {
            tempTagToKey.put(tags[i], i + 1);
            tempKeyToTag.put(i + 1, tags[i]);
        }

        tagToKey = Collections.unmodifiableMap(tempTagToKey);
        keyToTag = Collections.unmodifiableMap(tempKeyToTag);
    }
    public static final Map<String, Integer> tagToKey;
    public static final Map<Integer, String> keyToTag;
}
