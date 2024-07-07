package com.nanshuo.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanshuo.project.common.ErrorCode;
import com.nanshuo.project.constant.PageConstant;
import com.nanshuo.project.exception.BusinessException;
import com.nanshuo.project.mapper.QuestionMapper;
import com.nanshuo.project.model.domain.Question;
import com.nanshuo.project.model.domain.User;
import com.nanshuo.project.model.dto.question.QuestionQueryRequest;
import com.nanshuo.project.model.enums.sort.QuestionSortFieldEnums;
import com.nanshuo.project.model.vo.question.QuestionVO;
import com.nanshuo.project.model.vo.user.UserSafetyVO;
import com.nanshuo.project.service.QuestionService;
import com.nanshuo.project.service.UserService;
import com.nanshuo.project.utils.SqlUtils;
import com.nanshuo.project.utils.ThrowUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author dell
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-07-07 10:11:18
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    /**
     * 有效题目
     *
     * @param question question
     * @param add  添加
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取题目封装
     *
     * @param question    question
     * @param request 请求
     * @return {@code QuestionVO}
     */
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserSafetyVO userVO = userService.getUserSafeVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    /**
     * 获取查询包装器
     *
     * @param questionQueryRequest question查询请求
     * @return {@code QueryWrapper<Question>}
     */
    @Override
    public LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        String answer = questionQueryRequest.getAnswer();
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tagList = questionQueryRequest.getTags();
        Long userId = questionQueryRequest.getUserId();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), Question::getTitle, title);
        queryWrapper.like(StringUtils.isNotBlank(content), Question::getContent, content);
        queryWrapper.like(StringUtils.isNotBlank(answer), Question::getAnswer, answer);
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like(Question::getTags, "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), Question::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), Question::getUserId, userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(PageConstant.SORT_ORDER_ASC),
                isSortField(sortField));
        return queryWrapper;
    }

    /**
     * 分页获取题目封装
     *
     * @param questionPage 题目页面
     * @param request  请求
     * @return {@code Page<QuestionVO>}
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserSafeVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    /**
     * 是否为排序字段
     *
     * @param sortField 排序字段
     * @return {@code SFunction<Question, ?>}
     */
    private SFunction<Question, ?> isSortField(String sortField) {
        if (Objects.equals(sortField, "")) {
            sortField = QuestionSortFieldEnums.UPDATE_TIME.name();
        }
        if (SqlUtils.validSortField(sortField)) {
            return QuestionSortFieldEnums.fromString(sortField)
                    .map(QuestionSortFieldEnums::getFieldGetter)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR, "错误的排序字段"));
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "排序字段无效");
        }
    }


}




