package ssu.groupstudy.domain.study.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.groupstudy.domain.user.domain.User;
import ssu.groupstudy.global.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rel_user_study")
@Getter
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "studyId", nullable = false)
    private Study study;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false) // TODO : 강퇴 칼럼 삭제 검토
    private char banishYn;

    @Builder
    public Participant(User user, Study study) {
        this.user = user;
        this.study = study;
        this.color = generateColor(); // TODO : 색상 입력 구현 (color picker?)
        this.banishYn = 'N';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(user, that.user) && Objects.equals(study, that.study);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, study);
    }

    // TODO : 초기에 색상 자동 결정 (초기에 선택 불가)
    private String generateColor() {
        return "";
    }

    public boolean isBanished() {
        return (banishYn == 'Y');
    }
}
