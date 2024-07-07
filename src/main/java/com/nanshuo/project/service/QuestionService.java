package com.nanshuo.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nanshuo.project.model.domain.Question;
import com.nanshuo.project.model.dto.question.QuestionQueryRequest;
import com.nanshuo.project.model.vo.question.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dell
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-07-07 10:11:18
 */
public interface QuestionService extends IService<Question> {

    /**
     * 有效题目
     *
     * @param question 问题
     * @param add      添加
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取题目封装视图
     *
     * @param question    question
     * @param request 请求
     * @return {@code QuestionVO}
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 获取查询包装器
     *
     * @param questionQueryRequest question查询请求
     * @return {@code QueryWrapper<Question>}
     */
    LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 分页获取题目封装
     *
     * @param questionPage 题目页面
     * @param request  请求
     * @return {@code Page<QuestionVO>}
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

}
