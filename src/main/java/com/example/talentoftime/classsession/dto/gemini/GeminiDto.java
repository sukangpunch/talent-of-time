package com.example.talentoftime.classsession.dto.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GeminiDto(
        @JsonProperty("candidates")
        List<Candidate> candidates
) {
        public record Candidate(
                @JsonProperty("content")
                Content content
        ) {
                public record Content(
                        @JsonProperty("parts")
                        List<Part> parts
                ) { }
        }

        public record Part(
                @JsonProperty("text")
                String text
        ) { }
}

