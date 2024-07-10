package com.nanshuo.project.judge.strategy;


import com.nanshuo.project.judge.model.JudgeInfo;

/**
 * 判题策略
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/10
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext 判断语境
     * @return {@link JudgeInfo }
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
