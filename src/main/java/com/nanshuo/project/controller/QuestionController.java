package com.nanshuo.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanshuo.project.annotation.Check;
import com.nanshuo.project.common.ApiResponse;
import com.nanshuo.project.common.ApiResult;
import com.nanshuo.project.common.ErrorCode;
import com.nanshuo.project.constant.UserConstant;
import com.nanshuo.project.exception.BusinessException;
import com.nanshuo.project.model.domain.Question;
import com.nanshuo.project.model.domain.QuestionSubmit;
import com.nanshuo.project.model.domain.User;
import com.nanshuo.project.model.dto.IdRequest;
import com.nanshuo.project.model.dto.question.*;
import com.nanshuo.project.model.dto.question_submit.QuestionSubmitAddRequest;
import com.nanshuo.project.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.nanshuo.project.model.vo.question.QuestionSubmitVO;
import com.nanshuo.project.model.vo.question.QuestionVO;
import com.nanshuo.project.service.QuestionService;
import com.nanshuo.project.service.QuestionSubmitService;
import com.nanshuo.project.service.UserService;
import com.nanshuo.project.utils.JsonUtils;
import com.nanshuo.project.utils.ThrowUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口
 *
 * @author <a href="https://github.com/nanshuo0814">nanshuo(南烁)</a>
 * @date 2024/07/07
 */
@RestController
@RequestMapping("/question")
@Slf4j
//@Api(tags = "题目模块")
public class QuestionController {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    // region 增删改查

    /**
     * 添加题目
     *
     * @param questionAddRequest question添加请求
     * @param request        请求
     * @return {@code ApiResponse<Long>}
     */
    @PostMapping("/add")
    @Check(checkAuth = UserConstant.USER_ROLE)
    @ApiOperation(value = "添加题目")
    public ApiResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JsonUtils.objToJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(JsonUtils.objToJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JsonUtils.objToJson(judgeConfig));
        }
        questionService.validQuestion(question, true);
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        question.setFavourNum(0);
        question.setThumbNum(0);
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ApiResult.success(newQuestionId);
    }

    /**
     * 删除题目
     *
     * @param idRequest 删除请求
     * @param request   请求
     * @return {@code ApiResponse<Boolean>}
     */
    @PostMapping("/delete")
    @Check(checkAuth = UserConstant.USER_ROLE)
    @ApiOperation(value = "删除题目")
    public ApiResponse<Boolean> deleteQuestion(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        validateAndCheckAuthForQuestionOperation(request, id);
        return ApiResult.success(questionService.removeById(id));
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest 更新后请求
     * @return {@code ApiResponse<Boolean>}
     */
    @PostMapping("/update")
    @Check(checkAuth = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "更新题目（仅管理员）")
    public ApiResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JsonUtils.objToJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(JsonUtils.objToJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JsonUtils.objToJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResult.success(questionService.updateById(question));
    }

    /**
     * 编辑（用户）
     *
     * @param questionEditRequest question编辑请求
     * @param request         请求
     * @return {@code ApiResponse<Boolean>}
     */
    @PostMapping("/edit")
    @Check(checkAuth = UserConstant.USER_ROLE)
    @ApiOperation(value = "编辑题目")
    public ApiResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {

        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(JsonUtils.objToJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(JsonUtils.objToJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JsonUtils.objToJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        validateAndCheckAuthForQuestionOperation(request, questionEditRequest.getId());
        boolean result = questionService.updateById(question);
        return ApiResult.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param request   请求
     * @param idRequest id请求
     * @return {@code ApiResponse<QuestionVO>}
     */
    @GetMapping("/get/vo")
    @ApiOperation(value = "根据 id 获取")
    public ApiResponse<QuestionVO> getQuestionVOById(IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ApiResult.success(questionService.getQuestionVO(question, request));
    }

    // endregion

    // region 分页查询

    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest question查询请求
     * @return {@code ApiResponse<Page<Question>>}
     */
    @PostMapping("/list/page")
    @Check(checkAuth = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "分页获取列表（仅管理员）")
    public ApiResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ApiResult.success(questionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest question查询请求
     * @param request          请求
     * @return {@code ApiResponse<Page<QuestionVO>>}
     */
    @PostMapping("/list/page/vo")
    @ApiOperation(value = "分页获取列表（封装类）")
    public ApiResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                      HttpServletRequest request) {
        return ApiResult.success(handlePaginationAndValidation(questionQueryRequest, request));
    }

    /**
     * 分页获取当前用户创建的题目列表
     *
     * @param questionQueryRequest question查询请求
     * @param request          请求
     * @return {@code ApiResponse<Page<QuestionVO>>}
     */
    @PostMapping("/my/list/page/vo")
    @ApiOperation(value = "分页获取用户创建的题目")
    public ApiResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                        HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        return ApiResult.success(handlePaginationAndValidation(questionQueryRequest, request));
    }

    /**
     * 处理分页和验证
     *
     * @param questionQueryRequest question查询请求
     * @param request          请求
     * @return {@code Page<QuestionVO>}
     */
    private Page<QuestionVO> handlePaginationAndValidation(QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return questionService.getQuestionVOPage(questionPage, request);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/question_submit/list/page")
    public ApiResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ApiResult.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 问题提交添加请求
     * @param request                  请求
     * @return 提交记录的 id
     */
    @Check(checkAuth = UserConstant.USER_ROLE)
    @PostMapping("/question_submit/do")
    public ApiResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ApiResult.success(questionSubmitId);
    }

    // endregion

    // region 公用方法

    /**
     * 验证并检查题目操作权限
     *
     * @param request 请求
     * @param id      id
     */
    private void validateAndCheckAuthForQuestionOperation(HttpServletRequest request, Long id) {
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可操作（这里假设"编辑"和"删除"操作的权限是一样的）
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    // endregion

}
