package com.nanshuo.project.model.enums.sort;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nanshuo.project.model.domain.Question;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 题目排序字段枚举
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/07
 */
@Getter
public enum QuestionSortFieldEnums {

    ID(Question::getId),
    CREATE_TIME(Question::getCreateTime),
    UPDATE_TIME(Question::getUpdateTime),
    FAVOUR_NUM(Question::getFavourNum),
    THUMB_NUM(Question::getThumbNum),
    SUBMIT_NUM(Question::getSubmitNum),
    ACCEPTED_NUM(Question::getAcceptedNum),
    USER_ID(Question::getUserId);

    private final SFunction<Question, ?> fieldGetter;

    QuestionSortFieldEnums(SFunction<Question, ?> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    private static final Map<String, QuestionSortFieldEnums> FIELD_MAPPING = Arrays.stream(values())
            .collect(Collectors.toMap(QuestionSortFieldEnums::name, field -> field));

    /**
     * 从字符串映射到枚举
     *
     * @param sortField 排序字段
     * @return {@code Optional<UserSortField>}
     */
    public static Optional<QuestionSortFieldEnums> fromString(String sortField) {
        // 转换驼峰式命名到下划线分隔，忽略大小写
        String formattedSortField = sortField.replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return Optional.ofNullable(FIELD_MAPPING.get(formattedSortField.toUpperCase()));
    }
}