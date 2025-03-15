package com.eternalcode.economy;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.commons.scheduler.Task;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class SchedulerImpl implements Scheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public Task run(Runnable runnable) {
        return new TaskImpl(scheduler.submit(runnable));
    }

    @Override
    public Task runAsync(Runnable runnable) {
        return new TaskImpl(scheduler.submit(runnable));
    }

    @Override
    public Task runLater(Runnable runnable, Duration duration) {
        ScheduledFuture<?> future = scheduler.schedule(runnable, duration.toMillis(), TimeUnit.MILLISECONDS);
        return new TaskImpl(future);
    }

    @Override
    public Task runLaterAsync(Runnable runnable, Duration duration) {
        ScheduledFuture<?> future = scheduler.schedule(runnable, duration.toMillis(), TimeUnit.MILLISECONDS);
        return new TaskImpl(future);
    }

    @Override
    public Task timer(Runnable runnable, Duration duration, Duration interval) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(runnable, duration.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS);
        return new TaskImpl(future);
    }

    @Override
    public Task timerAsync(Runnable runnable, Duration duration, Duration interval) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(runnable, duration.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS);
        return new TaskImpl(future);
    }

    @Override
    public <T> CompletableFuture<T> complete(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    @Override
    public <T> CompletableFuture<T> completeAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    private static class TaskImpl implements Task {
        private final Future<?> future;

        public TaskImpl(Future<?> future) {
            this.future = future;
        }

        @Override
        public void cancel() {
            future.cancel(true);
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isRunning() {
            return false;
        }

        @Override
        public boolean isRepeating() {
            return false;
        }
    }
}
