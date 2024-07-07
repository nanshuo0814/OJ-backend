package com.nanshuo.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nanshuo.project.model.domain.QuestionSubmit;
import com.nanshuo.project.model.domain.User;
import com.nanshuo.project.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.nanshuo.project.model.vo.question.QuestionSubmitVO;

/**
* @author dell
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-07-07 13:26:10
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 获取查询包装器
     *
     * @param questionQueryRequest question查询请求
     * @return {@code QueryWrapper<Question>}
     */
    LambdaQueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest);

    /**
     * get问题提交vopage
     *
     * @param questionSubmitPage 问题提交页面
     * @param loginUser          登录用户
     * @return {@link Page }<{@link QuestionSubmitVO }>
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    /**
     * 获取问题提交vo
     *
     * @param questionSubmit 问题提交
     * @param loginUser      登录用户
     * @return {@link QuestionSubmitVO }
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);
}
