package io.github.grigorov_georgi.vt_structured_tasks_spring;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;

@RestController
@RequestMapping("/api")
public class Controller {

    private final FakeRestClient client;

    public Controller(FakeRestClient client) {
        this.client = client;
    }

    @GetMapping("/speakers")
    public List<Speaker> getSpeakers() {
        return client.retrieveSpeakers();
    }

    @GetMapping("/speakers-async")
    public CompletableFuture<List<Speaker>> getSpeakersAsync() {
        return client.retrieveSpeakerAsync();
    }

    @GetMapping("/talks")
    public List<Talk> getTalks() {
        var speakers = client.retrieveSpeakers();
        return speakers.stream()
                .map(client::retrieveTalk)
                .toList();
    }

    @SneakyThrows
    @GetMapping("/talks-optimized")
    public List<Talk> getTalksOptimized() {
        var speakers = client.retrieveSpeakers();

        try(var scope = new StructuredTaskScope<Talk>()) {
            List<StructuredTaskScope.Subtask<Talk>> tasks =  speakers.stream()
                    .map(speaker -> scope.fork(() -> client.retrieveTalk(speaker)))
                    .toList();

            scope.join();

            return tasks.stream()
                    .filter(task -> task.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(StructuredTaskScope.Subtask::get)
                    .toList();
        }
    }

    @SneakyThrows
    @GetMapping("/talks-optimized-refactored")
    public List<Talk> getTalksOptimizedRefactored() {
        var speakers = client.retrieveSpeakers();

        try(var scope = new CollectionTaskScope<Talk>()) {
            speakers.forEach(speaker -> scope.fork(() -> client.retrieveTalk(speaker)));
            scope.join();
            return scope.getResults();
        }
    }

    @GetMapping("/talks-async")
    public CompletableFuture<List<Talk>> getTalksAsync() {
        var speakerFuture = client.retrieveSpeakerAsync();
        return speakerFuture
                .thenApply(speakers -> {
                    List<CompletableFuture<Talk>> talkFutures = speakers.stream()
                            .map(client::retrieveTalkAsync)
                            .toList();

                    return talkFutures.stream().map(CompletableFuture::join).toList();
                });
    }
}
