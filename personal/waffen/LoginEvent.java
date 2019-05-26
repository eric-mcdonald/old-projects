
public class LoginEvent implements Event {

	public String username, password;
	public boolean drawLoginScreen;
	public LoginEvent(String username, String password, boolean drawLoginScreen) {
		this.username = username;
		this.password = password;
		this.drawLoginScreen = drawLoginScreen;
	}
}
