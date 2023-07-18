package model.user;

public class User {

	private String userId;
	private String password;
	private String name;
	private String email;

	private User(String userId, String password, String name, String email) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
	}

	public static class Builder {

		private String userId;
		private String password;
		private String name;
		private String email;

		public Builder() {}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public User build() {
			return new User(userId, password, name, email);
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}
