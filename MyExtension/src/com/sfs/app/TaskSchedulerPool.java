package com.sfs.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskSchedulerPool {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> currentTask = null;

    public static void scheduleTask(Runnable task, long delay, TimeUnit timeUnit) {
        cancelTimer();
        currentTask = scheduler.schedule(task, delay, timeUnit);
    }

    public static void cancelTimer() {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
    }
}
