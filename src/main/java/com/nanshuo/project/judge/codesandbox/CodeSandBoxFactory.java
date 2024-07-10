package com.nanshuo.project.judge.codesandbox;

import com.nanshuo.project.judge.constant.CodeSandBoxConstants;
import com.nanshuo.project.judge.impl.ExampleSandBox;
import com.nanshuo.project.judge.impl.RemoteSandBox;
import com.nanshuo.project.judge.impl.ThirdPartySandBox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实现）
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/09
 */
public class CodeSandBoxFactory {

    /**
     * 创建代码的沙箱实例
     *
     * @param type 类型
     * @return {@link CodeSandBox }
     */
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case CodeSandBoxConstants.REMOTE:
                return new RemoteSandBox();
            case CodeSandBoxConstants.THIRD_PARTY:
                return new ThirdPartySandBox();
            case CodeSandBoxConstants.EXAMPLE:
            default:
                return new ExampleSandBox();
        }
    }

}
