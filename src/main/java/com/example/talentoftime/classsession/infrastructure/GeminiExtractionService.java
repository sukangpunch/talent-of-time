package com.example.talentoftime.classsession.infrastructure;

import static com.example.talentoftime.classsession.infrastructure.GeminiPromptConst.CLASS_SESSION_IMAGE_EXTRACT_PROMPT;

import com.example.talentoftime.classsession.dto.ClassSessionParseResultResponse;
import com.example.talentoftime.classsession.dto.gemini.GeminiDto;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiExtractionService implements ClassSessionExtractFromImageService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=%s";

    @Override
    public List<ClassSessionParseResultResponse> extractSessionsFromImage(MultipartFile file) {
        try {
            // 1. 이미지를 Base64 인코딩
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType();

            // 2. Gemini API 요청 페이로드(JSON) 구성
            Map<String, Object> requestBody = createGeminiPayload(base64Image, mimeType, CLASS_SESSION_IMAGE_EXTRACT_PROMPT);

            // 3. Gemini API 호출
            String url = String.format(GEMINI_API_URL, apiKey);

            String responseJson = restClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            // 4. 응답 파싱 (Gemini 응답 구조에서 텍스트만 추출)
            String extractedJsonText = parseGeminiResponse(responseJson);

            // 5. 순수 JSON 문자열을 List<ParsedClassSessionDto> 객체로 변환
            return objectMapper.readValue(extractedJsonText, new TypeReference<List<ClassSessionParseResultResponse>>() {});
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gemini 응답 파싱 실패. extractedText: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.IMAGE_EXTRACTION_FAILED);
        }
    }

    private Map<String, Object> createGeminiPayload(String base64Image, String mimeType, String prompt) {
        // 이미지 데이터 객체
        Map<String, Object> inlineData = Map.of(
                "mime_type", mimeType,
                "data", base64Image
        );

        // 프롬프트와 이미지를 Parts 배열에 담기
        List<Map<String, Object>> parts = List.of(
                Map.of("text", prompt),
                Map.of("inline_data", inlineData)
        );

        // 핵심: responseMimeType을 application/json으로 강제
        Map<String, Object> generationConfig = Map.of(
                "responseMimeType", "application/json"
        );

        return Map.of(
                "contents", List.of(Map.of("parts", parts)),
                "generationConfig", generationConfig
        );
    }

    private String parseGeminiResponse(String responseJson) throws Exception {
        // Gemini API 의 기본 응답 JSON 트리를 타고 들어가서 실제 결과 텍스트만 뽑아냄
        GeminiDto response = objectMapper.readValue(responseJson, GeminiDto.class);

        if (response.candidates() == null || response.candidates().isEmpty()) {
            throw new BusinessException(ErrorCode.NO_CANDIDATES_RESPONSE);
        }

        List<GeminiDto.Part> parts = response.candidates().getFirst().content().parts();
        if (parts == null || parts.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_CONTENT_RESPONSE);
        }

        String rawText = parts.getFirst().text();
        return rawText.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();
    }
}
