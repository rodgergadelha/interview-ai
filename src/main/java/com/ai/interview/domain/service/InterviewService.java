package com.ai.interview.domain.service;

import com.ai.interview.api.dto.requestdto.InterviewRequest;
import com.ai.interview.api.dto.responsedto.InterviewQuestionFeedbackResponse;
import com.ai.interview.api.dto.responsedto.InterviewQuestionResponse;
import com.ai.interview.domain.model.entity.Interview;
import com.ai.interview.domain.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;
    private final InterviewQuestionService interviewQuestionService;

    @Transactional(rollbackFor = Exception.class)
    public InterviewQuestionResponse createInterviewAndGetFirstQuestion(InterviewRequest request) {
        var interview = Interview.builder()
                .jobLevel(request.jobLevel())
                .jobTitle(request.jobTitle())
                .language(request.interviewLanguage())
                .build();

        interview = repository.save(interview);
        var questions = interviewQuestionService.generateAndSaveQuestions(interview, request.numberOfQuestions());
        return questions.getFirst().toResponse();
    }

    public List<InterviewQuestionFeedbackResponse> getFeedbacks(UUID interviewUuid) {
        var interview = repository.findByUuid(interviewUuid)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found."));

        var interviewQuestions = interview.getQuestions();

        var interviewIsNotFinished = interviewQuestions.stream()
                .anyMatch(interviewQuestion -> isNull(interviewQuestion.getAnswer()));

        if(interviewIsNotFinished) throw new IllegalArgumentException("Interview is not finished yet.");

        return interviewQuestions.stream()
                .map(interviewQuestion -> new InterviewQuestionFeedbackResponse(
                    interviewQuestion.getQuestion(),
                    interviewQuestion.getAnswer(),
                    interviewQuestion.getFeedback()
                ))
                .toList();
    }
}