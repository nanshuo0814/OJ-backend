package com.nanshuo.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanshuo.project.common.ErrorCode;
import com.nanshuo.project.constant.PageConstant;
import com.nanshuo.project.exception.BusinessException;
import com.nanshuo.project.mapper.QuestionSubmitMapper;
import com.nanshuo.project.model.domain.QuestionSubmit;
import com.nanshuo.project.model.domain.User;
import com.nanshuo.project.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.nanshuo.project.model.enums.question.QuestionSubmitStatusEnum;
import com.nanshuo.project.model.enums.sort.QuestionSubmitSortFieldEnums;
import com.nanshuo.project.model.vo.question.QuestionSubmitVO;
import com.nanshuo.project.service.QuestionSubmitService;
import com.nanshuo.project.service.UserService;
import com.nanshuo.project.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dell
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-07-07 13:26:10
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private UserService userService;

    /**
     * 获取查询包装器
     *
     * @param questionQueryRequest question查询请求
     * @return {@code QueryWrapper<Question>}
     */
    @Override
    public LambdaQueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest) {
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        String language = questionQueryRequest.getLanguage();
        Long questionId = questionQueryRequest.getQuestionId();
        Integer status = questionQueryRequest.getStatus();
        Long userId = questionQueryRequest.getUserId();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), QuestionSubmit::getUserId, userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), QuestionSubmit::getLanguage, language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), QuestionSubmit::getQuestionId, questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, QuestionSubmit::getStatus, status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(PageConstant.SORT_ORDER_ASC),
                isSortField(sortField));
        return queryWrapper;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }


    /**
     * 是否为排序字段
     *
     * @param sortField 排序字段
     * @return {@code SFunction<Question, ?>}
     */
    private SFunction<QuestionSubmit, ?> isSortField(String sortField) {
        if (Objects.equals(sortField, "")) {
            sortField = QuestionSubmitSortFieldEnums.UPDATE_TIME.name();
        }
        if (SqlUtils.validSortField(sortField)) {
            return QuestionSubmitSortFieldEnums.fromString(sortField)
                    .map(QuestionSubmitSortFieldEnums::getFieldGetter)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR, "错误的排序字段"));
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "排序字段无效");
        }
    }
}




