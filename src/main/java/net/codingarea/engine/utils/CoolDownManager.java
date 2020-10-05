package net.codingarea.engine.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class CoolDownManager {

	protected final Map<Object, Long> last = new HashMap<>();
	protected long millis;

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

	public boolean isOnCoolDown(Object entity) {
		Long last = this.last.get(entity);
		return last != null && last >= (System.currentTimeMillis() - millis);
	}

	public long getCoolDownLeft(Object entity) {
		Long last = this.last.get(entity);
		if (last == null) return 0;
		long until = last + millis;
		return until - System.currentTimeMillis();
	}

	public void addToCoolDown(Object entity) {
		last.put(entity, System.currentTimeMillis());
	}

	public boolean checkCoolDown(Object entity) {
		boolean onCoolDown = isOnCoolDown(entity);
		addToCoolDown(entity);
		return onCoolDown;
	}

	public void clear() {
		last.clear();
	}

}
