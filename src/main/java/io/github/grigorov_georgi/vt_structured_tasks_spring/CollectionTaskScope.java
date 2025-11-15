package io.github.grigorov_georgi.vt_structured_tasks_spring;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

public class CollectionTaskScope<T> extends StructuredTaskScope<T> {

    private final Collection<T> results = new ConcurrentLinkedQueue<>();
    private final Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        if (subtask.state() == Subtask.State.SUCCESS) {
            results.add(subtask.get());
        } else if (subtask.state() == Subtask.State.FAILED) {
            exceptions.add(subtask.exception());
        }
    }

    public List<T> getResults() {
        return List.copyOf(results);
    }
}
