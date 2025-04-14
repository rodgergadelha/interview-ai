package com.ai.interview.domain.model.entity;

import com.ai.interview.api.dto.responsedto.InterviewQuestionResponse;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "interview_question")
public class InterviewQuestion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private final UUID uuid = UUID.randomUUID();

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "feedback")
    private String feedback;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_next_interview_question")
    private InterviewQuestion nextInterviewQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interview")
    private Interview interview;

    public InterviewQuestionResponse toResponse() {
        return new InterviewQuestionResponse(interview.getUuid(), uuid, question);
    }

    public boolean hasNext() {
        return nextInterviewQuestion != null;
    }
}