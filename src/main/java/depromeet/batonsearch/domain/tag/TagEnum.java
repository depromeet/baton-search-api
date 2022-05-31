package depromeet.batonsearch.domain.tag;

public enum TagEnum {
    KIND_TEACHER("친절한 선생님", 1),
    SYSTEMATIC_CLASS("체계적인 수업", 2),
    CUSTOMIZED_CARE("맞춤케어", 3),
    SPACIOUS_FACILITIES("넓은 시설", 4),
    VARIOUS_EQUIPMENT("다양한 기구", 5),
    NEW_EQUIPMENT("최신 기구", 6),
    MANY_PEOPLE("사람이 많은", 7),
    LESS_PEOPLE("사람이 적은", 8),
    AGREEMENT("쾌적한 환경", 9),
    QUIET_AMBIENCE("조용한 분위기", 10),
    STATION_AREA("역세권", 11);

    private String content;
    private Integer hash;

    TagEnum(String content, Integer hash) {
        this.content = content;
        this.hash = hash;
    }

    public Integer getHash() {
        return hash;
    }

    public String getContent() {
        return content;
    }
}
