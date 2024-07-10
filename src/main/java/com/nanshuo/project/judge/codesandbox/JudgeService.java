package com.nanshuo.project.judge.codesandbox;

import com.nanshuo.project.model.domain.QuestionSubmit;

/**
 * 判题服务
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/10
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId 问题提交id
     * @return {@link QuestionSubmit }
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
