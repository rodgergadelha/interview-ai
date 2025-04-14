package com.ai.interview.domain.service;

import com.ai.interview.api.dto.requestdto.InterviewAnswerRequest;
import com.ai.interview.api.dto.responsedto.InterviewQuestionResponse;
import com.ai.interview.domain.model.dto.InterviewQuestionsPromptResponse;
import com.ai.interview.domain.model.entity.Interview;
import com.ai.interview.domain.model.entity.InterviewQuestion;
import com.ai.interview.domain.repository.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewQuestionRepository repository;
    private final ChatModel chatModel;

    public List<InterviewQuestion> generateAndSaveQuestions(Interview interview, int numberOfQuestions) {
        var template = """
                I want to create a list of questions for a technical job interview with the job title: {jobTitle}.
                The language of the questions must be in {language}.
                The number of questions must be {numberOfQuestions}.
                Create the questions with a difficulty that matches the level {jobLevel}.
                """;

        Map<String, Object> params = Map.of(
            "jobTitle", interview.getJobTitle(),
            "jobLevel", interview.getJobLevel(),
            "language", interview.getLanguage(),
            "numberOfQuestions", numberOfQuestions
        );

        var questionsPromptResponse = ChatClient.create(chatModel)
                .prompt()
                .user(u -> u.text(template).params(params))
                .call()
                .entity(InterviewQuestionsPromptResponse.class);

        if(questionsPromptResponse == null || questionsPromptResponse.questions().isEmpty())
            throw new IllegalArgumentException("No questions generated.");

        var interviewQuestions = new LinkedList<InterviewQuestion>();

        questionsPromptResponse.questions().forEach(question -> {
            var interviewQuestion = InterviewQuestion.builder().question(question).interview(interview).build();
            var lastInterviewQuestion = interviewQuestions.peekLast();
            if(nonNull(lastInterviewQuestion)) lastInterviewQuestion.setNextInterviewQuestion(interviewQuestion);
            interviewQuestions.add(interviewQuestion);
        });

        return repository.saveAll(interviewQuestions);
    }

    @Transactional
    public InterviewQuestionResponse answerQuestionAndGetNextOne(UUID questionUuid, InterviewAnswerRequest request) {
        var interviewQuestion = repository.findByUuid(questionUuid)
                .orElseThrow(() -> new IllegalArgumentException("Interview question not found."));

        var interview = interviewQuestion.getInterview();

        var template = """
                I want a helpful feedback for the answer "{answer}" of the question "{question}".
                This question is from a list of questions for a technical job interview with the job title: {jobTitle}.
                The job level is {jobLevel}.
                The language of the feedback must be in {language}.
                The feedback must be in just one paragraph.
                """;

        Map<String, Object> params = Map.of(
            "question", interviewQuestion.getQuestion(),
            "answer", request.answer(),
            "jobTitle", interview.getJobTitle(),
            "jobLevel", interview.getJobLevel(),
            "language", interview.getLanguage()
        );

        var feedback = ChatClient.create(chatModel)
                .prompt()
                .user(u -> u.text(template).params(params))
                .call()
                .content();

        interviewQuestion.setAnswer(request.answer());
        interviewQuestion.setFeedback(feedback);
        return interviewQuestion.hasNext() ? interviewQuestion.getNextInterviewQuestion().toResponse() : null;
    }
}