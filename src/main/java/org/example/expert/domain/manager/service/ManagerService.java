package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.CommonExceptionStatus;
import org.example.expert.domain.common.exception.CommonExceptions;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.TODO_NOT_FOUND));

        if (todo.getUser() == null) {
            throw new CommonExceptions(CommonExceptionStatus.TODO_USER_AUTH_USER_DOES_NOT_MATCH);
        }

        if (!ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new CommonExceptions(CommonExceptionStatus.TODO_USER_AUTH_USER_DOES_NOT_MATCH);
        }

        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.TODO_USER_AUTH_USER_DOES_NOT_MATCH));

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new CommonExceptions(CommonExceptionStatus.TODO_USER_CANNOT_MANAGER);
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.MANAGER_NOT_FOUND));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.USER_CANNOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.TODO_NOT_FOUND));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new CommonExceptions(CommonExceptionStatus.TODO_USER_DOES_NOT_VAILED);
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new CommonExceptions(CommonExceptionStatus.MANAGER_NOT_FOUND));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new CommonExceptions(CommonExceptionStatus.TODO_IS_NOT_MANAGER);
        }

        managerRepository.delete(manager);
    }
}
