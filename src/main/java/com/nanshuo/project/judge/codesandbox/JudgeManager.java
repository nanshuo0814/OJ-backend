package com.nanshuo.project.judge.codesandbox;

import com.nanshuo.project.judge.model.JudgeInfo;
import com.nanshuo.project.judge.strategy.DefaultJudgeStrategy;
import com.nanshuo.project.judge.strategy.JavaLanguageJudgeStrategy;
import com.nanshuo.project.judge.strategy.JudgeContext;
import com.nanshuo.project.judge.strategy.JudgeStrategy;
import com.nanshuo.project.model.domain.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/10
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext 判断语境
     * @return {@link JudgeInfo }
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
