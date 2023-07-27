package model.board;

import java.time.LocalDate;
import java.util.Map;

public class BoardFactory {

	private static final String TITLE = "title";
	private static final String WRITER = "writer";
	private static final String CONTENTS = "contents";

	public static Board createBoard(Map<String, Object> map) {
		return Board.builder()
			.title((String)map.get(TITLE))
            .writer((String)map.get(WRITER))
            .contents((String)map.get(CONTENTS))
			.createdAt(LocalDate.now())
			.build();
	}
}
