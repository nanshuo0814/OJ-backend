package com.nanshuo.project.judge.impl;

import com.nanshuo.project.judge.codesandbox.CodeSandBox;
import com.nanshuo.project.judge.model.ExecuteCodeRequest;
import com.nanshuo.project.judge.model.ExecuteCodeResponse;

/**
 * 远程沙箱（实际调用接口的沙箱）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/09
 */
public class RemoteSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
