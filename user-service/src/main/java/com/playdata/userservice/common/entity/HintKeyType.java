package com.playdata.userservice.common.entity;

import lombok.Getter;

/**
 * QUESTION 1~5 힌트 질문/번호
 */
@Getter
public enum HintKeyType {

    QUESTION_1("졸업한 초등학교 이름은?", 1),
    QUESTION_2("어릴 적 별명은?", 2),
    QUESTION_3("가장 아끼는 물건은?", 3),
    QUESTION_4("기억에 남는 선생님 이름은?", 4),
    QUESTION_5("처음 키운 반려동물 이름은?", 5);

    private final String desc;
    private final int code;

    HintKeyType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    // 역변환 메서드 (숫자 → enum)
    public static HintKeyType fromCode(int code) {
        for (HintKeyType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}