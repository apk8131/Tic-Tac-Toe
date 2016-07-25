import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TictacToeServer {

	char board[][] = new char[3][3];
	Player currentPlayer;

	public TictacToeServer() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				board[i][j] = 'n';
	}

	public boolean isWinner() {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != 'n') {
				if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
					return true;
				}
			}
		}

		for (int i = 0; i < 3; i++) {
			if (board[0][i] != 'n') {
				if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
					return true;
				}
			}
		}

		if (board[0][0] != 'n' && board[0][0] == board[1][1]
				&& board[1][1] == board[2][2])
			return true;

		if (board[0][2] != 'n' && board[0][2] == board[1][1]
				&& board[1][1] == board[2][0])
			return true;

		return false;

	}

	public boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == 'n')
					return false;
			}
		}
		return true;
	}

	public synchronized boolean validMove(int x, int y, Player P)
			throws IOException {
		if (P == currentPlayer && board[x][y] == 'n') {
			board[x][y] = currentPlayer.sign;
			currentPlayer = currentPlayer.opponent;
			currentPlayer.opponentMove(x, y);
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		ServerSocket TTTserver = new ServerSocket(7799);
		System.out.println("Tic Tac Toe Server is Running");
		try {
			while (true) {

				TictacToeServer obj = new TictacToeServer();
				TictacToeServer.Player X = obj.new Player();
				TictacToeServer.Player O = obj.new Player();
				X.initPlayer(TTTserver.accept(), 'x', O);
				O.initPlayer(TTTserver.accept(), 'o', X);
				obj.currentPlayer = X;
				X.start();
				O.start();
			}
		} finally {
			TTTserver.close();
		}
	}

	class Player extends Thread {
		char sign;
		Player opponent;
		Socket TCP;
		BufferedReader input;
		PrintWriter output;

		public void initPlayer(Socket TCP, char sign, Player opponent) {
			this.TCP = TCP;
			this.sign = sign;
			this.opponent = opponent;
			try {
				input = new BufferedReader(new InputStreamReader(
						TCP.getInputStream()));
				output = new PrintWriter(TCP.getOutputStream(), true);
				output.println("Welcome " + sign);
				output.println(" Waiting for opponent to connect \n");
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		public void opponentMove(int x, int y) throws IOException {
			output.println("oppoent moved" + x + " " + y);
			if (isWinner())
				output.println("lose");
			else if (isBoardFull())
				output.println("tie");
		}

		public void run() {
			try {
				if (sign == 'x')
					output.println("JUST both players connected, your turn");
				else
					output.println("JUST both players connected");

				while (true) {
					String in = input.readLine();
					if (in.startsWith("move")) {
						int xtemp = Integer.parseInt(in.substring(4, 5));
						int ytemp = Integer.parseInt(in.substring(5, 6));
						if (validMove(xtemp, ytemp, this)) {
							output.println("valid move");
							if (isWinner())
								output.println("win");
							else if (isBoardFull())
								output.println("tie");
						} else {
							output.println("JUST invalid move");
						}
					} else if (in.equals("close"))
						return;
				}

			} catch (IOException e) {
				System.out.println("Player disconnected " + e);
			} finally {
				try {
					TCP.close();
				} catch (IOException e) {
				}
			}
		}
	}
}