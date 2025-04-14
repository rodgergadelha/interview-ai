package com.ai.interview.domain.repository;

import com.ai.interview.domain.model.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    @EntityGraph(attributePaths = {"interview", "nextInterviewQuestion"})
    Optional<InterviewQuestion> findByUuid(UUID uuid);
}