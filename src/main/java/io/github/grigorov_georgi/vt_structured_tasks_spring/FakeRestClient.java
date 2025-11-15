package io.github.grigorov_georgi.vt_structured_tasks_spring;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class FakeRestClient {

    private final List<Speaker> speakerList = IntStream.range(0, 10)
            .mapToObj(i -> new Speaker("Speaker " + i))
            .toList();

    public List<Speaker> retrieveSpeakers() {
        sleep(1_000);
        return speakerList;
    }

    public Talk retrieveTalk(Speaker speaker) {
        sleep(500);
        return new Talk("Talk for speaker " + speaker);
    }

    @SneakyThrows
    void sleep(int ms) {
        Thread.sleep(ms);
    }

    public CompletableFuture<List<Speaker>> retrieveSpeakerAsync() {
        return supplyAsync(() -> speakerList, delayedExecutor(1_000, MILLISECONDS));
    }

    public CompletableFuture<Talk> retrieveTalkAsync(Speaker speaker) {
        return supplyAsync(() -> new Talk("Async talk of " + speaker), delayedExecutor(500, MILLISECONDS));
    }
}
