package com.example.persitence.api.validation;

import com.example.persitence.api.model.Head;
import com.example.persitence.api.model.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeConsistencyValidator implements ConstraintValidator<CheckTimeConsistency, Head> {

    public boolean isValid(Head head, ConstraintValidatorContext context) {
        Collection<State> states = head.getStates();
        if (states.stream().anyMatch(state -> Objects.isNull(state.getStateBegin()) || Objects.isNull(state.getStateEnd()))) {
            return true;
        }

        List<State> timeLine = states
                .stream()
                .sorted(Comparator.comparing(State::getStateBegin))
                .collect(Collectors.toList());

        if (timeLine.stream().anyMatch(state -> state.getStateBegin().isAfter(state.getStateEnd()))) {
            return false;
        }

        return IntStream.range(1, timeLine.size())
                .mapToObj(i -> timeLine.get(i - 1).getStateEnd().equals(timeLine.get(i).getStateBegin()))
                .allMatch(isValid -> isValid);


    }
}
