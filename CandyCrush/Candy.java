package CandyCrush;

public abstract class Candy implements Visitor, Visited {
	protected int color;
	protected Board board;
	protected int row;
	protected int column;
	protected boolean wasCrushed; //this is a boolean attribute that prevents infinite loops due to special candies crushing each other
	
	//Getters Setters
	public void setColor(int color){
		this.color=color;
	}
	public int getColor(){
		return this.color;
	}
	public Board getBoard(){
		return this.board;
	}
	public void setRow(int row){
		this.row=row;
	}
	public void setColumn(int column){
		this.column=column;
	}
	public int getRow(){
		return this.row;
	}
	public int getColumn(){
		return this.column;
	}
	public void setWasCrushed(boolean bool){
		this.wasCrushed=bool;
	}
	
	//constructor
	public Candy(int color,Board board,int row,int column){//constructor
		this.board=board;
		this.color=color;
		this.row=row;
		this.column=column;
	}
	
	public void combine(Candy other){ //using visitor visited patters
		other.accept(this);
	}
		
	public abstract void crush();
	
	
}
