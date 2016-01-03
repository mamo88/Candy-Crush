package CandyCrush;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class BoardGui extends JPanel {
	
	private Game game;
	private Board board;
	private JButton[][] buttons;
	
	public Game getGame(){
		return this.game;
	}
	
	public Board getBoard(){
		return this.board;
	}
	public JButton[][] getButtons(){
		return this.buttons;
	}
	

	public BoardGui(Game game, Board board){ //constructor
		super(new GridLayout(9,9));
		this.board=board;
		this.game=game;
		buttons=new JButton[9][9];
		this.drawBoard();		
	}
	public void drawBoard(){//draws boardGui according to board
		for (int i=0; i<9; i=i+1){ 
			for (int j=0; j<9; j=j+1){
				JButton button=new JButton();
				Candy candy=this.board.getCandies()[i][j];
				if (candy instanceof RegularCandy){ //case 1: regular
					if (candy.getColor()==0) button.setIcon(new ImageIcon("pictures//yellow.png"));
					else if (candy.getColor()==1) button.setIcon(new ImageIcon("pictures//green.png"));
					else if (candy.getColor()==2) button.setIcon(new ImageIcon("pictures//purple.png"));
					else if (candy.getColor()==3) button.setIcon(new ImageIcon("pictures//red.png"));
					else if (candy.getColor()==4) button.setIcon(new ImageIcon("pictures//orange.png"));
					else if (candy.getColor()==5) button.setIcon(new ImageIcon("pictures//blue.png"));
				}
				else if (candy instanceof HorizontalCandy){ //case 2: horizontal stripes
					if (candy.getColor()==0) button.setIcon(new ImageIcon("pictures//horizontalyellow.png"));
					else if (candy.getColor()==1) button.setIcon(new ImageIcon("pictures//horizontalgreen.png"));
					else if (candy.getColor()==2) button.setIcon(new ImageIcon("pictures//horizontalpurple.png"));
					else if (candy.getColor()==3) button.setIcon(new ImageIcon("pictures//horizontalred.png"));
					else if (candy.getColor()==4) button.setIcon(new ImageIcon("pictures//horizontalorange.png"));
					else if (candy.getColor()==5) button.setIcon(new ImageIcon("pictures//horizontalblue.png"));
				}
				else if (candy instanceof VerticalCandy){ //case 3: vertical stripes
					if (candy.getColor()==0) button.setIcon(new ImageIcon("pictures//verticalyellow.png"));
					else if (candy.getColor()==1) button.setIcon(new ImageIcon("pictures//verticalgreen.png"));
					else if (candy.getColor()==2) button.setIcon(new ImageIcon("pictures//verticalpurple.png"));
					else if (candy.getColor()==3) button.setIcon(new ImageIcon("pictures//verticalred.png"));
					else if (candy.getColor()==4) button.setIcon(new ImageIcon("pictures//verticalorange.png"));
					else if (candy.getColor()==5) button.setIcon(new ImageIcon("pictures//verticalblue.png"));
				}
				else if (candy instanceof WrappedCandy){ //case 4: wrapped
					if (candy.getColor()==0) button.setIcon(new ImageIcon("pictures//wrappedyellow.png"));
					else if (candy.getColor()==1) button.setIcon(new ImageIcon("pictures//wrappedgreen.png"));
					else if (candy.getColor()==2) button.setIcon(new ImageIcon("pictures//wrappedpurple.png"));
					else if (candy.getColor()==3) button.setIcon(new ImageIcon("pictures//wrappedred.png"));
					else if (candy.getColor()==4) button.setIcon(new ImageIcon("pictures//wrappedorange.png"));
					else if (candy.getColor()==5) button.setIcon(new ImageIcon("pictures//wrappedblue.png"));
				}
				else if (candy instanceof ChocolateCandy){ //case 5: chocolate
					button.setIcon(new ImageIcon("pictures//chocolate.png"));
				}
				button.setOpaque(true);
				button.setHorizontalAlignment(getWidth()/2); //center the label
				button.setVerticalAlignment(getHeight()/2); //""
				button.addActionListener(game);
				this.add(button);
				buttons[i][j]=button;
			}
		}
	}
	

	
}
