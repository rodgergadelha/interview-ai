package com.ai.interview.api.dto.responsedto;

import java.util.UUID;

public record InterviewQuestionResponse(UUID interviewUuid, UUID questionUuid, String question) {}