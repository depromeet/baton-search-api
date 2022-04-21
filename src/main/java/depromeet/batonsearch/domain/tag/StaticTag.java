package depromeet.batonsearch.domain.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticTag {
    static {
        Map<String, Integer> tempTagToKey = new HashMap<>();
        Map<Integer, String> tempKeyToTag = new HashMap<>();

        String[] tags = {"태그1", "태그2", "태그3"};

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
