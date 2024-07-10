package com.nanshuo.project.judge.impl;

import com.nanshuo.project.judge.codesandbox.CodeSandBox;
import com.nanshuo.project.judge.model.ExecuteCodeRequest;
import com.nanshuo.project.judge.model.ExecuteCodeResponse;

/**
 * 第三方沙箱（调用网上第三方的沙箱）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/09
 */
public class ThirdPartySandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
