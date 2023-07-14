package lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NamedLock {

	private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

	public Lock getLock(String name) {
		return locks.computeIfAbsent(name, k -> new ReentrantLock());
	}
}
