package agents2.rem.events;

import java.util.Map;

import agents2.rem.core.EventBase;

public interface EventClass extends EventBase {

	default public int getProb() {
		return 0;
	}

	default public void modProb(int val) {

	}

	public Map<String, Object> getConstructorArgs();

}
