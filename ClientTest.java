import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args) {
		Client client = new Client("172.30.1.9");
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startRunning();
	}
}
