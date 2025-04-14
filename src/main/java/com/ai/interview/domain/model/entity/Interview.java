package com.ai.interview.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "interview")
public class Interview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private final UUID uuid = UUID.randomUUID();

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_level", nullable = false)
    private String jobLevel;

    @Column(name = "language", nullable = false)
    private String language;

    @OneToMany(mappedBy = "interview")
    private final List<InterviewQuestion> questions = new ArrayList<>();
}