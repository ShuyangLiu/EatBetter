package ur.disorderapp.model;


import ur.disorderapp.EnumValues.GoalStatus;

public class Goal
{
    private double progress;//the percentage of the progress
    private GoalStatus status;
    private String name;


    public Goal(double progress, GoalStatus status, String name)
    {
        this.progress = progress;
        this.status = status;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "progress=" + progress +
                ", status=" + status +
                '}';
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
