package model.board;

import java.time.LocalDate;

public class Board {

	private Integer boardId;
	private String writer;
	private String title;
	private String contents;
	private LocalDate createdAt;

	private Board(String writer, String title, String contents, LocalDate createdAt) {
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createdAt = createdAt;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public static class Builder {

		private String boardId;
		private String writer;
		private String title;
		private String contents;
		private LocalDate createdAt;

		public Builder() {}

		public Board.Builder boardId(String boardId) {
			this.boardId = boardId;
			return this;
		}

		public Board.Builder writer(String writer) {
			this.writer = writer;
			return this;
		}

		public Board.Builder title(String title) {
			this.title = title;
			return this;
		}

		public Board.Builder contents(String contents) {
			this.contents = contents;
			return this;
		}

		public Board.Builder createdAt(LocalDate createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Board build() {
			return new Board(writer, title, contents, createdAt);
		}
	}

	public static Board.Builder builder() {
		return new Board.Builder();
	}
}
