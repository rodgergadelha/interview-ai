package com.ai.interview.api.dto.requestdto;

import jakarta.validation.constraints.NotBlank;

public record InterviewAnswerRequest(@NotBlank String answer) {}