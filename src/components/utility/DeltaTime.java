package components.utility;

public class DeltaTime {
    private int deltaTime;
    private long lastTime;

    public DeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }

    public boolean canExecute() {
        if (System.currentTimeMillis() - lastTime > deltaTime) {
            lastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }
}
