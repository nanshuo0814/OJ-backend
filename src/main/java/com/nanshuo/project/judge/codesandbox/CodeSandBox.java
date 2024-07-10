package com.nanshuo.project.judge.codesandbox;

import com.nanshuo.project.judge.model.ExecuteCodeRequest;
import com.nanshuo.project.judge.model.ExecuteCodeResponse;

/**
 * 代码沙盒
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/09
 */
public interface CodeSandBox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest 执行代码请求
     * @return {@link ExecuteCodeResponse }
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
