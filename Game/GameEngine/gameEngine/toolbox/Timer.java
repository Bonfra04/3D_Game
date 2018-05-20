package gameEngine.toolbox;

public class Timer {

	public long t1 = System.currentTimeMillis();
	public long t2 = 0;

	public boolean running = false;

	public void start() {
		if (!running) {
			t2 = 0;
			t1 = System.currentTimeMillis();
		}
		running = true;
	}

	public void update() {
		if (running) {
			t2 += System.currentTimeMillis() - t1;
			t1 = System.currentTimeMillis();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
		this.t2 = 0;
	}

	public long getTime() {
		return t2;
	}
}
