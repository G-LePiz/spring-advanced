package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.CommonExceptionStatus;
import org.example.expert.domain.common.exception.CommonExceptions;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.USER_CANNOT_FOUND));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
//        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
//                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
//                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
//            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
//        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.USER_CANNOT_FOUND));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new CommonExceptions(CommonExceptionStatus.PASSWORD_CANNOT_SAME);
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new CommonExceptions(CommonExceptionStatus.PASSWORD_IS_WRONG);
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }
}
