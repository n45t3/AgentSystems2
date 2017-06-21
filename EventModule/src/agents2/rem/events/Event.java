package agents2.rem.events;

public interface Event {

	default public void onCreate(Object... args) {

	}

	default public void beforeHandling(Object... args) {

	}

	default public void onHandling(Object... args) {

	}

	default public void afterHandling(Object... args) {

	}

}
