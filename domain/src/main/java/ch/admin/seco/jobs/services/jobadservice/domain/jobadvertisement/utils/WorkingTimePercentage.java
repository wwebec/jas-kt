package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

public class WorkingTimePercentage {

    private int min;
    private int max;

    public WorkingTimePercentage(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static WorkingTimePercentage evaluate(Number min, Number max) {
        if (min == null && max == null) {
            return defaultPercentage();
        }
        if (min == null) {
            return new WorkingTimePercentage(max.intValue(), max.intValue());
        }
        if (max == null) {
            return new WorkingTimePercentage(min.intValue(), 100);
        }
        return new WorkingTimePercentage(min.intValue(), max.intValue());
    }

    public static WorkingTimePercentage defaultPercentage() {
        return new WorkingTimePercentage(100, 100);
    }

}
