package agents2.rem.handlers;

import agents2.rem.core.EventBase;
import agents2.rem.events.Event;

public interface EventHandler extends EventBase {

	public void run(Event e,Object... args);

}
