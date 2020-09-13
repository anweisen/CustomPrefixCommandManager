package net.anweisen.commandmanager.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class CoolDownManager<T> {

	private final Map<T, Long> last = new HashMap<>();
	private long millis;

	public CoolDownManager(long millis) {
		this.millis = millis;
	}

	public CoolDownManager(float seconds) {
		this.millis = (long) (seconds * 1000);
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public void setSeconds(float seconds) {
		this.millis = (long) (seconds * 1000);
	}

	public boolean isOnCoolDown(T entity) {
		Long last = this.last.get(entity);
		return last != null && last >= (System.currentTimeMillis() - millis);
	}

	public void addToCoolDown(T entity) {
		last.put(entity, System.currentTimeMillis());
	}

	public boolean checkCoolDown(T entity) {
		boolean onCoolDown = isOnCoolDown(entity);
		addToCoolDown(entity);
		return onCoolDown;
	}

	public void clear() {
		last.clear();
	}

}
