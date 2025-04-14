package com.ai.interview.api.controller;

import com.ai.interview.api.dto.requestdto.InterviewAnswerRequest;
import com.ai.interview.api.dto.responsedto.InterviewQuestionResponse;
import com.ai.interview.domain.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static java.util.Objects.nonNull;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview-questions")
public class InterviewQuestionController {

    private final InterviewQuestionService service;

    @PatchMapping("/{questionUuid}")
    public ResponseEntity<InterviewQuestionResponse> answerQuestionAndGetNextOne(@PathVariable UUID questionUuid,
                                                                                 @Valid @RequestBody InterviewAnswerRequest request) {
        var response = service.answerQuestionAndGetNextOne(questionUuid, request);
        return nonNull(response) ? ResponseEntity.ok(response) : ResponseEntity.noContent().build();
    }
}