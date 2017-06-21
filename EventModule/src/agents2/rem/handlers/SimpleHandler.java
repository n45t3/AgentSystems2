package agents2.rem.handlers;

import agents2.rem.events.Event;

public class SimpleHandler extends BasicEventHandler implements EventHandler {

	public SimpleHandler(Class<?> e) {
		super(e);
	}

	public SimpleHandler(Event e) {
		super(e);
	}
	
	@Override
	public void run(Event e,Object... args) {
		e.onHandling(args);
	}

}
