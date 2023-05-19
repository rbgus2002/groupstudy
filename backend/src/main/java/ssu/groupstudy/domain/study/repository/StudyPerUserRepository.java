package ssu.groupstudy.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.groupstudy.domain.study.domain.Study;
import ssu.groupstudy.domain.study.domain.Participant;
import ssu.groupstudy.domain.user.domain.User;

public interface StudyPerUserRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndStudy(User user, Study study);
}
