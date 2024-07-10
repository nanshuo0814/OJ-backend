package com.nanshuo.project.judge.strategy;

import com.nanshuo.project.judge.model.JudgeInfo;
import com.nanshuo.project.model.domain.Question;
import com.nanshuo.project.model.domain.QuestionSubmit;
import com.nanshuo.project.model.dto.question.JudgeCase;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/10
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
