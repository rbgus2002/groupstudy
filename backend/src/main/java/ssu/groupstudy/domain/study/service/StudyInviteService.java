package ssu.groupstudy.domain.study.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.groupstudy.domain.study.domain.Study;
import ssu.groupstudy.domain.study.exception.StudyNotFoundException;
import ssu.groupstudy.domain.study.repository.StudyRepository;
import ssu.groupstudy.domain.user.domain.User;
import ssu.groupstudy.domain.user.exception.UserNotFoundException;
import ssu.groupstudy.domain.user.repository.UserRepository;
import ssu.groupstudy.global.ResultCode;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudyInviteService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void inviteUser(Long userId, Long studyId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(ResultCode.USER_NOT_FOUND));
        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(() -> new StudyNotFoundException(ResultCode.STUDY_NOT_FOUND));

        study.invite(user);
    }

    @Transactional
    public void leaveUser(Long userId, Long studyId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(ResultCode.USER_NOT_FOUND));
        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(() -> new StudyNotFoundException(ResultCode.STUDY_NOT_FOUND));

        study.leave(user);
    }
}
