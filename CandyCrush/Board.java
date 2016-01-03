package CandyCrush;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;



public class Board {
	
	private Candy[][] candies; 
	private Game game;
	private boolean isFirstCandy;
	private Candy FirstCandy;
	private int pointFactor;

	public Game getGame(){ //getters
		return this.game;
	}
	
	public int getPointFactor(){
		return this.pointFactor;
	}

	public Candy[][] getCandies(){
		return this.candies;
	}
	
	public void setCandy(Candy candy,int row,int column){ //setters
		this.candies[row][column]=candy;
	}
	
	public void setPointFactor(int pf){
		this.pointFactor=pf;
	}
	
	
	
	public Board(Game game){ //constructor
		this.game=game;
		candies=new Candy[9][9];
		for (int i=0; i<9; i=i+1){ //randomly draw a board (without 3 in a row/column)
			for (int j=0; j<9; j=j+1){
				Candy candy= setRandomCandy(i,j);
				while (isTriplat(candy))//if triplat- will set another candy
					candy= setRandomCandy(i,j);
				candies[i][j]=candy;
			}
		}
		this.isFirstCandy=true;
		this.FirstCandy=null;
		this.pointFactor=10;
	}


	public Candy setRandomCandy(int i,int j){ //decides which Candy will be set
		Candy candy=null;
		int rand=(int) (Math.random()*6); //draw a color randomly
		if (rand==0) candy=new RegularCandy(0,this,i,j);
		else if (rand==1) candy=new RegularCandy(1,this,i,j);
		else if (rand==2) candy=new RegularCandy(2,this,i,j);
		else if (rand==3) candy=new RegularCandy(3,this,i,j);
		else if (rand==4) candy=new RegularCandy(4,this,i,j);
		else if (rand==5) candy=new RegularCandy(5,this,i,j);
		return candy;		
	}

	public void move(int row,int column) throws IOException{
		Candy candy=this.getCandies()[row][column];
		if (isFirstCandy){//first candy to be pressed on- need to wait to second candy in order to switch
			this.FirstCandy=candy;
			isFirstCandy=false;
		}
		else{//second candy
			if (isLegalMove(candy,this.FirstCandy)){//if the move is legal- switch the candies and redraw board
				this.candySwitch(candy, this.FirstCandy); //switch
				if (isSpecialCombine(candy,FirstCandy)){ //case 1: combination between 2 special candies
					candy.combine(FirstCandy);
				}
				if (isFiveSome(candy) || isFiveSome(this.FirstCandy)){ //case 2: creates 5 in a row/column - chocolate
					this.explode5(candy);
					this.explode5(this.FirstCandy);
				}
				if (isFiveSomeLT(candy) || isFiveSomeLT(this.FirstCandy)){  //case 3: creats T/L - wrapped
					this.explode5LT(candy);
					this.explode5LT(this.FirstCandy);
				}
				if (isFourSomeHorizontal(candy) || isFourSomeHorizontal(this.FirstCandy)){ //case 4: creates 4 in a row - stripes
					this.explode4H(candy);
					this.explode4H(this.FirstCandy);
				}
				if (isFourSomeVertical(candy) || isFourSomeVertical(this.FirstCandy)){ //case 4: creates 4 in a column - stripes
					this.explode4V(candy);
					this.explode4V(this.FirstCandy);
				}
				if (isTriplat(candy) || isTriplat(this.FirstCandy)){ //case 5: creates 3 in a row
					this.explode3(candy);
					this.explode3(this.FirstCandy);
				}	
				this.refill(); //refill the board randomly
				
				while (fiveSomeOnBoard()){ //chain reaction loop
					for (int i=0; i<9; i=i+1){
						for (int j=0; j<9; j=j+1){
							if(isFiveSome(candies[i][j])){
								this.pointFactor=this.pointFactor*2;
								this.explode5(candies[i][j]);
								this.refill();
							}
						}
					}
				}
				while (fiveSomeLTOnBoard()){ //chain reaction loop
					for (int i=0; i<9; i=i+1){
						for (int j=0; j<9; j=j+1){
							if(isFiveSomeLT(candies[i][j])){
								this.pointFactor=this.pointFactor*2;
								this.explode5LT(candies[i][j]);
								this.refill();
							}
						}
					}
				}
				while (fourSomeHorizontalOnBoard()){ //chain reaction loop
					for (int i=0; i<9; i=i+1){
						for (int j=0; j<9; j=j+1){
							if(isFourSomeHorizontal(candies[i][j])){
								this.pointFactor=this.pointFactor*2;
								this.explode4H(candies[i][j]);
								this.refill();
							}
						}
					}
				}
				while (fourSomeVerticalOnBoard()){ //chain reaction loop
					for (int i=0; i<9; i=i+1){
						for (int j=0; j<9; j=j+1){
							if(isFourSomeVertical(candies[i][j])){
								this.pointFactor=this.pointFactor*2;
								this.explode4V(candies[i][j]);
								this.refill();
							}
						}
					}
				}
				while (triplatOnBoard()){ //chain reaction loop
					for (int i=0; i<9; i=i+1){ 
						for (int j=0; j<9; j=j+1){
							if(isTriplat(candies[i][j])){
								this.pointFactor=this.pointFactor*2;
								this.explode3(candies[i][j]);
								this.refill();
							}
						}
					}
				}
				this.game.setScoreLabel(); //update score
				this.game.reDrawBoard(); //draw the new board
				this.getGame().setTurn(); //update turnLabel
				this.game.setTurnLabel(); //""
				for (int i=0; i<9; i=i+1){ //reset the board crush status
					for (int j=0; j<9; j=j+1){
						this.candies[i][j].setWasCrushed(false);
					}
				}
			}
			isFirstCandy=true; //reset
			FirstCandy=null; //reset
			this.pointFactor=10; //reset
		}
	}
	
	
	private boolean isSpecialCombine(Candy candy1, Candy candy2) { //special combination
		if (candy1==null || candy2==null) return false; //case 1: one of the candies null X
		if (candy1 instanceof ChocolateCandy || candy2 instanceof ChocolateCandy) return true; //case 2: one of them is a chocolate V
		if (!(candy1 instanceof RegularCandy) && !(candy2 instanceof RegularCandy)) return true; //case 3: none is regular V
		return false;
	}

	public boolean isLegalMove(Candy candy1,Candy candy2){//checks if the switch is legal
		if (!neighbors(candy1,candy2)){ //case 1: not neighbors X
			return false;
		}
		if (isSpecialCombine(candy1, candy2)){ //case 2: combine 2 special ones V
			return true;
		}
		if (candy1.getColor()==candy2.getColor()){ //case 3: the same color X
			return false;
		}
		candySwitch(candy1,candy2);//switch the candies in order to check if the board (after the switch) has triplats
			if (this.game.getBoard().isTriplat(candy1) ||this.game.getBoard().isTriplat(candy2)){
					candySwitch(candy1,candy2);//returns the board to its original order
					return true;
			
			}
		candySwitch(candy1,candy2);//returns the board to its original order
		return false;
	}
	
	public void candySwitch(Candy candy1, Candy candy2){//switch 2 candies on board
		int tmpRow=candy1.getRow();
		int tmpColumn=candy1.getColumn();
		candy1.setRow(candy2.getRow());
		candy1.setColumn(candy2.getColumn());
		candy2.setRow(tmpRow);
		candy2.setColumn(tmpColumn);
		this.game.getBoard().setCandy(candy1,candy1.getRow(),candy1.getColumn());
		this.game.getBoard().setCandy(candy2,candy2.getRow(),candy2.getColumn());

	}
	
	public boolean neighbors(Candy candy1,Candy candy2){// checks if 2 candies are neighbors 
		int row1=candy1.getRow();
		int row2=candy2.getRow();
		int column1=candy1.getColumn();
		int column2=candy2.getColumn();
		if (row1==row2){ //same row, shift 1 column
			if (column1==column2-1||column1==column2+1)
				return true;
		}
		if (column1==column2){// same column, shift 1 row
			if (row1==row2-1||row1==row2+1)
				return true;
		}
		return false;		
	}
	
	//ThreeSome
	public boolean isTriplat(Candy candy){//checks if the candy is part of a triplat
		if (candy==null) return false;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=2 && candies[i-1][j]!=null && candies[i-2][j]!=null){// case down
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor())
				return true;
		}
		if (i<=6 && candies[i+1][j]!=null && candies[i+2][j]!=null){// case up
			if (candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if (i>=1 && i<=7 && candies[i+1][j]!=null && candies[i-1][j]!=null){// case middle
			if (candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i-1][j].getColor())
				return true;
		}
		if (j>=2 && candies[i][j-1]!=null && candies[i][j-2]!=null){//case right
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor())
				return true;
		}
		if (j<=6 && candies[i][j+1]!=null && candies[i][j+2]!=null){//case left
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor())
				return true;
		}
		if (j>=1 && j<=7 && candies[i][j+1]!=null && candies[i][j-1]!=null){//case middle
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j-1].getColor())
				return true;
		}
		return false;
	}
	
	public void explode3(Candy candy){//checks which triplat to explode
		if (candy==null) return;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=2 && candies[i-1][j]!=null && candies[i-2][j]!=null){// case down
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
			}
		}
		if (i<=6 && candies[i+1][j]!=null && candies[i+2][j]!=null){// case up
			if (candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
			}
		}
		if (i>=1 && i<=7 && candies[i+1][j]!=null && candies[i-1][j]!=null){// case middle
			if (candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i-1][j].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
			}
		}
		if (j>=2 && candies[i][j-1]!=null && candies[i][j-2]!=null){// case right
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();
			}
		}
		if (j<=6 && candies[i][j+1]!=null && candies[i][j+2]!=null){// case left
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j+2]!=null) candies[i][j+2].crush();
			}
		}
		if (j>=1 && j<=7 && candies[i][j+1]!=null && candies[i][j-1]!=null){// case middle
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j-1].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
			}
		}
	}
	
	public boolean triplatOnBoard(){ //checks if theres 3 in ar row
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(isTriplat(candies[i][j]))
					return true;
			}
		}
		return false;
	}
	//ThreeSome

	//FourSome
	public boolean isFourSomeHorizontal(Candy candy){//checks if the candy is part of a Horizontal FourSome
		if (candy==null) return false;
		int i=candy.getRow();
		int j=candy.getColumn();
		if (j>=1 && j<=6 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//case inner left {0100}
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor())
				return true;
		}
		if (j>=2 && j<=7 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i][j-2]!=null){//case inner right {0010}
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j-2].getColor())
				return true;
		}
		return false;
	}

	public boolean isFourSomeVertical(Candy candy){//checks if the candy is part of a Vertical FourSome
		if (candy==null) return false;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=1 && i<=6 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){// case inner down
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if(i>=2 && i<=7 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i-2][j]!=null){// case inner up
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i-2][j].getColor())
				return true;
		}

		return false;
	}

	
	public void explode4H(Candy candy){//explodes the candies of a Horizontal FourSome
		if (candy==null) return;
		int i=candy.getRow();
		int j=candy.getColumn();
		if (j>=1 && j<=6 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//case inner left {0100}
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new VerticalCandy(savedColor,this,i,j);
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
			}
		}
		
		if (j>=2 && j<=7 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i][j-2]!=null){//case inner right {0010}
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j-2].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new VerticalCandy(savedColor,this,i,j);
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();
			}
		}
	}
	
	public void explode4V(Candy candy){//explodes the candies of a Vertical FourSome
		if (candy==null) return;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=1 && i<=6 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){// case inner down
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new HorizontalCandy(savedColor,this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
			}
		}
		if(i>=2 && i<=7 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i-2][j]!=null){// case inner up
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i-2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new HorizontalCandy(savedColor,this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
			}
		}
	}

	
	public boolean fourSomeVerticalOnBoard(){ //checks if theres 4 in a column
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(isFourSomeVertical(candies[i][j]))
					return true;
			}
		}
		return false;
	}
	
	public boolean fourSomeHorizontalOnBoard(){ //checks if theres 4 in a row
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(isFourSomeHorizontal(candies[i][j]))
					return true;
			}
		}
		return false;
	}


	//FourSome

	//FiveSomeLT
	public boolean isFiveSomeLT(Candy candy){//checks if the candy is part of T/L
		if (candy==null) return false;
		int i=candy.getRow();
		int j=candy.getColumn();
		
		//case L
		if(i>=2 && j<=6 && candies[i-1][j]!=null && candies[i-2][j]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){// 
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor())
				return true;
		}
		if (j>=2 && i<=6 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if (j<=6 && i<=6 && candies[i][j+1]!=null && candies[i][j+2]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if (j>=2 && i>=2 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i-1][j]!=null && candies[i-2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor())
				return true;
		}
		
		//case T
		if (j>=1 && j<=7 && i<=6 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if (j>=1 && j<=7 && i>=2 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i-1][j]!=null && candies[i-2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor())
				return true;
		}
		if (i>=1 && i<=7 && j<=6 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor())
				return true;
		}
		if (i>=1 && i<=7 && j>=2 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i][j-1]!=null && candies[i][j-2]!=null){//
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor())
				return true;
		}
		return false;
	}

	
	public void explode5LT(Candy candy){ //explode T/L
		if (candy==null) return;
		int i=candy.getRow();
		int j=candy.getColumn();
		
		//case L
		if(i>=2 && j<=6 && candies[i-1][j]!=null && candies[i-2][j]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){// 
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j+2]!=null) candies[i][j+2].crush();

			}
		}
		if (j>=2 && i<=6 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();

			}
		}
		if (j<=6 && i<=6 && candies[i][j+1]!=null && candies[i][j+2]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j+2]!=null) candies[i][j+2].crush();

			}
		}
		if (j>=2 && i>=2 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i-1][j]!=null && candies[i-2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();

			}
		}
		
		//case T
		if (j>=1 && j<=7 && i<=6 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();

			}
		}
		if (j>=1 && j<=7 && i>=2 && candies[i][j-1]!=null && candies[i][j+1]!=null && candies[i-1][j]!=null && candies[i-2][j]!=null){//
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();

			}
		}
		if (i>=1 && i<=7 && j<=6 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j+2]!=null) candies[i][j+2].crush();

			}
		}
		if (i>=1 && i<=7 && j>=2 && candies[i-1][j]!=null && candies[i+1][j]!=null && candies[i][j-1]!=null && candies[i][j-2]!=null){//
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor()){
				int savedColor=candies[i][j].getColor();
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]= new WrappedCandy(savedColor,this,i,j);
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();

			}
		}
	}
	
	public boolean fiveSomeLTOnBoard(){ //checks if theres a T/L on board
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(isFiveSomeLT(candies[i][j]))
					return true;
			}
		}
		return false;
	}

	
	//FiveSomeLT

	
	
	//FiveSome
	public boolean isFiveSome(Candy candy){//checks if the candy is part of a 5 in a row/column
		if (candy==null) return false;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=2 && i<=6 && candies[i-1][j]!=null && candies[i-2][j]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){// case horizontal
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor())
				return true;
		}
		if (j>=2 && j<=6 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//case vertical
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor())
				return true;
		}
		return false;
	}

	public void explode5(Candy candy){ //explode
		if (candy==null) return;
		int i=candy.getRow();
		int j=candy.getColumn();
		if(i>=2 && i<=6 && candies[i-1][j]!=null && candies[i-2][j]!=null && candies[i+1][j]!=null && candies[i+2][j]!=null){// case horizontal
			if (candy.getColor()==candies[i-1][j].getColor() && candy.getColor()==candies[i-2][j].getColor() && candy.getColor()==candies[i+1][j].getColor() && candy.getColor()==candies[i+2][j].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]=new ChocolateCandy(this,i,j);
				if (candies[i+1][j]!=null) candies[i+1][j].crush();
				if (candies[i+2][j]!=null) candies[i+2][j].crush();
				if (candies[i-1][j]!=null) candies[i-1][j].crush();
				if (candies[i-2][j]!=null) candies[i-2][j].crush();
			}
		}
		if (j>=2 && j<=6 && candies[i][j-1]!=null && candies[i][j-2]!=null && candies[i][j+1]!=null && candies[i][j+2]!=null){//case vertical
			if (candy.getColor()==candies[i][j-1].getColor() && candy.getColor()==candies[i][j-2].getColor() && candy.getColor()==candies[i][j+1].getColor() && candy.getColor()==candies[i][j+2].getColor()){
				if (candies[i][j]!=null) candies[i][j].crush();
				candies[i][j]=new ChocolateCandy(this,i,j);
				if (candies[i][j-1]!=null) candies[i][j-1].crush();
				if (candies[i][j-2]!=null) candies[i][j-2].crush();
				if (candies[i][j+1]!=null) candies[i][j+1].crush();
				if (candies[i][j+2]!=null) candies[i][j+2].crush();
			}
		}
	}
	
	public boolean fiveSomeOnBoard(){ //checks if theres 5 in a row/column on board
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(isFiveSome(candies[i][j]))
					return true;
			}
		}
		return false;
	}

	//FiveSome

	
	
	public void refill(){
		for (int i=8;i>=0;i=i-1){//drops candies to the bottom
			for (int j=8;j>=0;j=j-1){
				if(candies[i][j]==null){
					int tmp=i;
					while (tmp>=0){
						if(candies[tmp][j]!=null){
							candies[i][j]=candies[tmp][j];
							candies[i][j].setRow(i);
							candies[tmp][j]=null;
							break;
						}
						tmp=tmp-1;
					}
				}
			}
		}
		for (int i=8;i>=0;i=i-1){//refills the null empty cells with random candies
			for (int j=8;j>=0;j=j-1){
				if (candies[i][j]==null)
					candies[i][j]=this.setRandomCandy(i, j);
			}
		}

	}
	
	public void endGame() throws IOException{ //is called when 'turns' become 0
		JOptionPane.showMessageDialog(null, "Game Over - press OK to start again"); //lose message
		if (this.game.getBest()[9][1]==null || this.game.getBest()[9][1]=="" || this.game.getScoreInt()>Integer.parseInt(this.game.getBest()[9][1])){ //new high score
			String name=JOptionPane.showInputDialog("Very Well! new High Score!, please enter your name:");
			if (name!=null && name.length()>0){ //name was entered
				this.game.getBest()[9][0]=name; //replace the new high score with last
				this.game.getBest()[9][1]=""+this.game.getScoreInt();
				sortByScore(this.game.getBest()); //insertion sort by score value
				Board.writeToFile(this.game.getBest()); //rewrite the new best scores array to file
			}	
			else{ //name wasn't entered
				while (name==null || name.length()==0){
					name=JOptionPane.showInputDialog("You have entered an invalid name - please enter your name!");
					if (name!=null && name.length()>0){ //name was entered
						this.game.getBest()[9][0]=name; //replace the new high score with last
						this.game.getBest()[9][1]=""+this.game.getScoreInt();
						sortByScore(this.game.getBest()); //insertion sort by score value
						Board.writeToFile(this.game.getBest()); //rewrite the new best scores array to file
					}	
				}
			}
		}
		this.game.restartGame(); //draw a new board
	}
	
	private static String[][] sortByScore(String[][] scores){ //insertion sort
		for (int i=0; i<10; i=i+1){
			if (scores[i][1]==null ||scores[i][1]=="")
				scores[i][1]="0";
		}
		for (int i=0; i<9; i=i+1){
			for (int j=i+1; j<10; j=j+1){
				if (Integer.parseInt(scores[i][1])<Integer.parseInt(scores[j][1])){
					String temp1 = scores[i][0];
					String temp2 = scores[i][1];
	                scores[i][0] = scores[j][0];
	                scores[i][1] = scores[j][1];
	                scores[j][0] = temp1;
	                scores[j][1] = temp2;  
				}
			}
		}
		for (int i=0; i<10; i=i+1){
			if (Integer.parseInt(scores[i][1])==0)
				scores[i][1]="";
		}
		return scores;
	}
	
	public static void writeToFile(String[][] scores) throws IOException{
		File file=new File("BestScores"); //create file (if not exists)
		PrintWriter pw = new PrintWriter("BestScores"); //clear the file
		pw.print(""); //""
		pw.close(); //""
		FileWriter writer=new FileWriter(file,true); //open writer
		for (int i=0; i<10; i=i+1){ //write the best scores array into the file
			writer.write(scores[i][0]+","+scores[i][1]); 
			writer.write("\n");
		}
		writer.close(); //close writer
	}
	public static void readFromFile(String[][] scores) throws IOException{
		try {
			FileInputStream fInput = new FileInputStream("BestScores");
			DataInputStream dInput=new DataInputStream(fInput); //""
			InputStreamReader iReader=new InputStreamReader(fInput); //""
			BufferedReader bReader = new BufferedReader(iReader); //""
			String string=bReader.readLine(); //set string to the first line in the file
			int i;
			for (i=0; string!=null && !(string.equals("null,null")) && !(string.equals(",")) && i<10; i=i+1){ //condition - line has "data"
				String[] splited=string.split(",");
				if (splited.length==2){
					scores[i][0]=splited[0]; //put the line from the file in the best scores array
					scores[i][1]=splited[1]; //""
				}
				string=bReader.readLine(); //read the next line
			}
			dInput.close(); //close stuff ???
			while (i<10){ //set the "no-data" lines to empty string (not null)
				scores[i][0]="";
				scores[i][1]="";
				i=i+1;
			}
		} 
		catch (FileNotFoundException e) {
			writeToFile(scores); //create file if doesn't exist yet	
		}
	}	

	
	public void printBoard(){ //used for testing
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if (candies[i][j]==null) System.out.print("null"+"; ");
				else System.out.print(i+","+j+","+" color="+candies[i][j].getColor()+"; ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
}