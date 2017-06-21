package agents2.rem.receivers;

import agents2.rem.core.EventBase;
import agents2.rem.events.Event;

public interface EventReceiver extends EventBase {

	public default void send(Event e) {

	}

}
