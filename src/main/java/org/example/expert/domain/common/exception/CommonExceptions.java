package org.example.expert.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonExceptions extends RuntimeException {

    private final CommonExceptionStatus exceptionStatus;
}
