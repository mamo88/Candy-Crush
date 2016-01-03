package CandyCrush;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Game extends JFrame implements ActionListener, MouseListener {

	private Board board;
	private BoardGui boardGui;
	private JButton bestButton;
	private JButton restartButton;
	private int scoreInt;
	private JLabel scoreLabel;
	private String[][] bestScores;
	private int turn;
	private JLabel turnLabel;
	
	public BoardGui getBoardGui(){ //getters
		return this.boardGui;
	}
	public Board getBoard(){
		return this.board;
	}
	public JButton getBestButton(){
		return this.bestButton;
	}
	public JButton getRestartButton(){
		return this.restartButton;
	}
	public int getScoreInt(){
		return this.scoreInt;
	}
	public JLabel getScoreLabel(){
		return this.scoreLabel;
	}
	public JLabel getTurnLabel(){
		return this.turnLabel;
	}
	public String[][] getBest(){
		return this.bestScores;
	}
	public void setScoreInt(int score){ //setters
		this.scoreInt=score;
	}
	public void setScoreLabel(){ //setter without input. set the scoreLabel to the current score value.
		this.scoreLabel.setText("Score: "+this.scoreInt);
	}
	public void setTurnLabel(){ //setter without input. set the scoreLabel to the current score value.
		this.turnLabel.setText("Turns Left: "+this.turn);
	}
	public void setTurn() throws IOException{ //setter without input. set the scoreLabel to the current score value.
		this.turn=this.turn-1;
		if (turn==0){ //end game when it reaches 0
			this.setTurnLabel();
			this.board.endGame();
		}
				
	}

	public Game() throws IOException{ //constructor
		super("Candy Crush Saga"); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setResizable(false);
		JLabel back=new JLabel(new ImageIcon("pictures//background6.jpg"));
		this.setContentPane(back); //set the background image
		this.getContentPane().setLayout(new BorderLayout()); //set layout to BorderLayout (North for buttons, center for playing board)
		this.board=new Board(this);
		this.boardGui=new BoardGui(this, board); //create the board 
		this.boardGui.setBorder(new EmptyBorder(70, 500, 10, 10)); //set the place of the board in the grid
		bestButton = new JButton("Best Scores"); //create the buttons
		bestButton.setFont(new Font("Blackadder ITC", Font.BOLD, 24));
		bestButton.setBackground(Color.MAGENTA);
		bestButton.addActionListener(this); //""
		bestButton.setFocusable(false); //make sure the focus is always on the main window
		restartButton = new JButton("Start Over"); //""
		restartButton.setFont(new Font("Blackadder ITC", Font.BOLD, 24));
		restartButton.setBackground(Color.MAGENTA);
		restartButton.addActionListener(this); //""
		restartButton.setFocusable(false); //""
		turn=20;
		turnLabel = new JLabel("Turns Left: "+turn); //create the score label with int value
		turnLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		turnLabel.setFont(new Font("Blackadder ITC", Font.BOLD, 20));
		turnLabel.setOpaque(true); //to enable setBackground
		turnLabel.setBackground(new Color(238,130,238));
		scoreInt = 0;
		bestScores=new String[10][2]; //create 2D array that holds name and score of the best scores
		Board.readFromFile(bestScores); //set the best scores array according to file
		scoreLabel = new JLabel("Score: "+scoreInt); //create the score label with int value
		scoreLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		scoreLabel.setFont(new Font("Blackadder ITC", Font.BOLD, 20));
		scoreLabel.setOpaque(true); //to enable setBackground
		scoreLabel.setBackground(new Color(238,130,238));
		JPanel northPanel = new JPanel(); //create a sub-panel for the "northern" buttons (and label)
		northPanel.add(bestButton); //""
		northPanel.add(restartButton); //""
		northPanel.add(turnLabel);
		northPanel.add(scoreLabel); //""
		northPanel.setBorder(new EmptyBorder(80,510,35,20)); //place the northPanel on the grid
		northPanel.setBackground(new Color(0,0,0,0)); //set the background color to transparent
		boardGui.setBackground(new Color(0,0,0,0)); //set the background color to transparent
		this.getContentPane().add(northPanel,BorderLayout.NORTH); //add the buttons to the "north"
		this.getContentPane().add(boardGui, BorderLayout.CENTER); //add the board to the "center"
		this.addMouseListener(this);
		this.setSize(980,700); //set standard size
		this.setVisible(true); //show the window
	}


	

	public static void main(String[] args) throws IOException {
		Game game=new Game();
	}

	
	public void restartGame(){//build new board and draws boardGui accordingly
		this.board=new Board(this);
		this.reDrawBoard();
		this.setScoreInt(0); //change score to 0
		this.setScoreLabel();
		this.turn=20;
		this.setTurnLabel();
	}
	
	public void reDrawBoard(){//removes old boardGui, builds and inserts new boardGui 
		this.getContentPane().remove(boardGui);
		this.boardGui=new BoardGui(this,board);
		this.boardGui.setBorder(new EmptyBorder(70, 500, 10, 10)); //set the place of the board in the grid
		boardGui.setBackground(new Color(0,0,0,0)); //set the background color to transparent
		this.getContentPane().add(boardGui,BorderLayout.CENTER);
		this.setVisible(true);
		this.repaint();
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.getRestartButton()){ //restart the game 
			this.restartGame();
		}
		else if (e.getSource()==this.getBestButton()){ //open table with best scores
			try {
				Board.readFromFile(this.bestScores);
				BestScores best=new BestScores(this); 
			} catch (IOException e1) {
				//will never get here
			}
		}
		else{
			for (int i=0; i<9; i=i+1){ //one of boardGui buttons was pressed
				for (int j=0; j<9; j=j+1){
					if (e.getSource()==this.boardGui.getButtons()[i][j]){
						try {
							board.move(i,j);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return;
					}
				}
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.repaint();
		this.boardGui.repaint();		
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.repaint();
		this.boardGui.repaint();		
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		this.repaint();
		this.boardGui.repaint();		
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		this.repaint();
		this.boardGui.repaint();		
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.repaint();
		this.boardGui.repaint();		
		
	}

}
