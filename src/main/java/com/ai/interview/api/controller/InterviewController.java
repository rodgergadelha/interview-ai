package com.ai.interview.api.controller;

import com.ai.interview.api.dto.requestdto.InterviewRequest;
import com.ai.interview.api.dto.responsedto.InterviewQuestionFeedbackResponse;
import com.ai.interview.api.dto.responsedto.InterviewQuestionResponse;
import com.ai.interview.domain.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewQuestionResponse createInterviewAndGetFirstQuestion(@Valid @RequestBody InterviewRequest request) {
        return service.createInterviewAndGetFirstQuestion(request);
    }

    @GetMapping("/{interviewUuid}/feedbacks")
    @ResponseStatus(HttpStatus.OK)
    public List<InterviewQuestionFeedbackResponse> getFeedbacks(@PathVariable UUID interviewUuid) {
        return service.getFeedbacks(interviewUuid);
    }
}