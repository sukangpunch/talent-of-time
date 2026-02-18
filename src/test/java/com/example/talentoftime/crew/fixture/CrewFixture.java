package com.example.talentoftime.crew.fixture;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class CrewFixture {

    private final CrewRepository crewRepository;
    private final CountRepository countRepository;

    public Crew 크루(String name, CrewType crewType) {
        Crew crew = Crew.create(name, crewType);
        crewRepository.save(crew);

        Arrays.stream(TaskType.values())
                .map(taskType -> Count.createInitial(crew, taskType))
                .forEach(countRepository::save);

        return crew;
    }

    public Crew 오전_크루() {
        return 크루("홍길동", CrewType.MORNING);
    }

    public Crew 미들_크루() {
        return 크루("이순신", CrewType.MIDDLE);
    }

    public Crew 오후_크루() {
        return 크루("김철수", CrewType.AFTERNOON);
    }
}
