package ssu.groupstudy.domain.task.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ssu.groupstudy.domain.round.domain.UserRound;
import ssu.groupstudy.global.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false, length = 30)
    private String detail;

    //TODO 사용여부 검토 후 ENUM 처리?
    @Column(nullable = false)
    private String taskType;

    @Column(nullable = false)
    private char doneYn;

    @Column(nullable = false)
    private char deleteYn;

    @ManyToOne
    @JoinColumn(name="userRoundId", nullable = false)
    private UserRound userRound;
}