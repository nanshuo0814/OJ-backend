package com.nanshuo.project.judge.impl;

import com.nanshuo.project.judge.codesandbox.CodeSandBox;
import com.nanshuo.project.judge.enums.JudgeInfoMessageEnums;
import com.nanshuo.project.judge.model.ExecuteCodeRequest;
import com.nanshuo.project.judge.model.ExecuteCodeResponse;
import com.nanshuo.project.judge.model.JudgeInfo;
import com.nanshuo.project.model.enums.question.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 沙箱示例（为了跑通业务逻辑）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/09
 */
public class ExampleSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnums.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
