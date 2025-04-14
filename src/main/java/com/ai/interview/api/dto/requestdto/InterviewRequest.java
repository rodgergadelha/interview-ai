package com.ai.interview.api.dto.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InterviewRequest(
    @NotBlank String jobTitle,
    @NotBlank String jobLevel,
    @NotBlank String interviewLanguage,
    @Min(1) @Max(10) int numberOfQuestions
) {}