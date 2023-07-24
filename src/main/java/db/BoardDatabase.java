package db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import model.board.Board;

public class BoardDatabase {

	private static Map<Integer, Board> boards = Maps.newHashMap();
	private static AtomicInteger idCounter = new AtomicInteger(0);

	public static void save(Board board) {
		Integer boardId = idCounter.incrementAndGet();
		board.setBoardId(boardId);
		boards.put(boardId, board);
	}

	public static List<Board> getList() {
		return Lists.newArrayList(boards.values());
	}
}
