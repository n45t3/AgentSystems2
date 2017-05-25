package agents2.rem.events;

import java.util.Map;

public interface EventContext {

	public Object[] getPreContext();

	public Object[] getHandlerArgs();

	public Object[] getPostContext();

}
