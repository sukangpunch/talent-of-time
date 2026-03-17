package com.example.talentoftime.classsession.infrastructure;

import com.example.talentoftime.classsession.dto.ClassSessionParseResultResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ClassSessionExtractFromImageService {

    List<ClassSessionParseResultResponse> extractSessionsFromImage(MultipartFile file);

}
