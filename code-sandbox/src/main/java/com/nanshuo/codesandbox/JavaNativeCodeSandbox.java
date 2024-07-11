package com.nanshuo.codesandbox;

import com.nanshuo.codesandbox.model.ExecuteCodeRequest;
import com.nanshuo.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;


/**
 * Java 原生代码沙箱实现（直接复用模板方法）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/11
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}

