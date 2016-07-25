import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeClient {

	JFrame frame = new JFrame();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JButton[] board = new JButton[9];
	JButton current = new JButton();
	JLabel display = new JLabel(" ");
	Socket TCP;
	BufferedReader Cinput;
	PrintWriter Coutput;
	String Csign;
	String Osign;

	public TicTacToeClient(String serverAdd) throws UnknownHostException,IOException {
		TCP = new Socket(serverAdd, 7799);
		Cinput = new BufferedReader(new InputStreamReader(TCP.getInputStream()));
		Coutput = new PrintWriter(TCP.getOutputStream(), true);
		frame.setLocation(200, 200);
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setLayout(new BorderLayout(20, 20));
		panel1.setLayout(new GridLayout(3, 3, 2, 2));
		for (int i = 0; i < 9; i++) {
			board[i] = new JButton("");
			final int c = i;
			board[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					current = board[c];
					String loc;
					if (c < 3)
						loc = "0" + c % 3;
					else if (c < 6)
						loc = "1" + c % 3;
					else
						loc = "2" + c % 3;

					Coutput.println("move" + loc);
				}
			});
			panel1.add(board[i]);
		}
		panel2.add(display);
		frame.add(panel1, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void play() throws IOException {
		String command;
		try {
			command = Cinput.readLine();
			if (command.contains("Welcome")) {
				char s = command.charAt(8);
				if (s == 'x') {
					Csign = "X";
					Osign = "O";
					frame.setName("Player X");
				} else {
					Csign = "O";
					Osign = "X";
					frame.setName("Player O");
				}
				display.setText(command);
			}
			while (true) {
				command = Cinput.readLine();

				if (command.equals("win")) {
					display.setText("You Win !!");
					break;
				}

				if (command.equals("lose")) {
					display.setText("You Lose !!");
					break;
				}

				if (command.equals("tie")) {
					display.setText("Tie !!");
					break;
				}

				if (command.startsWith("JUST")) {
					display.setText(command.substring(5));
				}

				if (command.startsWith("valid move")) {
					display.setText("valid Move please wait for other player to move");
					current.setText(Csign);
				}

				if (command.startsWith("oppoent moved")) {
					int x = Integer.parseInt(command.substring(13, 14));
					int y = Integer.parseInt(command.substring(15, 16));
					if (x == 0)
						board[y].setText(Osign);
					else if (x == 1)
						board[y + 3].setText(Osign);
					else if (x == 2)
						board[y + 6].setText(Osign);
					display.setText("Oppoent played, ur turn");
				}
			}

			Coutput.println("close");
		} finally {
			TCP.close();
		}
	}

	public static void main(String[] args) throws Exception {
		String serverAddress = (args.length == 0) ? "localhost" : args[1];
		TicTacToeClient client = new TicTacToeClient(serverAddress);
		client.play();
	}
}
