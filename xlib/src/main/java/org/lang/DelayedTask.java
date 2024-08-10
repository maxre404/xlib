package org.lang;

public class DelayedTask {
    private String tag;
    private Runnable task;

    public DelayedTask(String tag, Runnable task) {
        this.tag = tag;
        this.task = task;
    }

    public String getTag() {
        return tag;
    }

    public Runnable getTask() {
        return task;
    }
}
