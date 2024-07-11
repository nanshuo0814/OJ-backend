package com.nanshuo.codesandbox.model;

import lombok.Data;

/**
 * 处理消息
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/11
 */
@Data
public class ExecuteMessage {

    private int exitValue;
    private String message;
    private String errorMessage;
    private Long time;
    private Long memory;
}
