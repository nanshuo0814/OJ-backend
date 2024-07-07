package com.nanshuo.project.model.enums.sort;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nanshuo.project.model.domain.QuestionSubmit;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 题目提交排序字段枚举
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/07
 */
@Getter
public enum QuestionSubmitSortFieldEnums {

    ID(QuestionSubmit::getId),
    CREATE_TIME(QuestionSubmit::getCreateTime),
    UPDATE_TIME(QuestionSubmit::getUpdateTime),
    QUESTION_ID(QuestionSubmit::getQuestionId),
    USER_ID(QuestionSubmit::getUserId);

    private final SFunction<QuestionSubmit, ?> fieldGetter;

    QuestionSubmitSortFieldEnums(SFunction<QuestionSubmit, ?> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    private static final Map<String, QuestionSubmitSortFieldEnums> FIELD_MAPPING = Arrays.stream(values())
            .collect(Collectors.toMap(QuestionSubmitSortFieldEnums::name, field -> field));

    /**
     * 从字符串映射到枚举
     *
     * @param sortField 排序字段
     * @return {@code Optional<UserSortField>}
     */
    public static Optional<QuestionSubmitSortFieldEnums> fromString(String sortField) {
        // 转换驼峰式命名到下划线分隔，忽略大小写
        String formattedSortField = sortField.replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return Optional.ofNullable(FIELD_MAPPING.get(formattedSortField.toUpperCase()));
    }
}