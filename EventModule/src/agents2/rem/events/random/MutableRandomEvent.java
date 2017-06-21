package agents2.rem.events.random;

import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.events.EventClass;

public class MutableRandomEvent extends RandomEvent implements EventClass {

	@Override
	public void modProb(int val) {
		this.prob = this.prob + val >= 0 ? this.prob + val : 0;
	}

	public MutableRandomEvent(Class<?> e, Map<String, Object> args, int prob) {
		super(e, args, prob);
	}

	public MutableRandomEvent(Event e, Map<String, Object> args, int prob) {
		super(e, args, prob);
	}

}
