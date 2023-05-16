package ssu.groupstudy.domain.round.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssu.groupstudy.domain.round.domain.Round;
import ssu.groupstudy.domain.study.domain.Study;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRoundRequest {
    @NotNull
    private Long studyId;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private String studyTime; // TODO : 추후 주소
    private String studyPlace; // TODO : 추후 카카오톡 주소 api 연동

    public Round toEntity(Study study) {
        return Round.builder()
                .study(study)
                .studyPlace(this.studyPlace)
                .studyTime(LocalDateTime.now())
                .build();
    }
}