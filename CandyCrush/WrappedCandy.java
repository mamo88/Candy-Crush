package CandyCrush;

public class WrappedCandy extends Candy {
	
	public WrappedCandy(int color, Board board,int row,int column) {
		super(color, board,row,column);		
	}

	@Override
	public void crush() { //crush wrapped - crush 3X3 around the candy, refill board, and do it again
		if (!wasCrushed){	
			
			wasCrushed=true;
			
			if (row>=1 && column>=1 && this.board.getCandies()[row-1][column-1]!=null) this.board.getCandies()[row-1][column-1].crush();
			if (row>=1 && this.board.getCandies()[row-1][column]!=null) this.board.getCandies()[row-1][column].crush();
			if (row>=1 && column<=7 && this.board.getCandies()[row-1][column+1]!=null) this.board.getCandies()[row-1][column+1].crush();
			if (column>=1 && this.board.getCandies()[row][column-1]!=null) this.board.getCandies()[row][column-1].crush();
			if (column<=7 && this.board.getCandies()[row][column+1]!=null) this.board.getCandies()[row][column+1].crush();
			if (row<=7 && column>=1 && this.board.getCandies()[row+1][column-1]!=null) this.board.getCandies()[row+1][column-1].crush();
			if (row<=7 && this.board.getCandies()[row+1][column]!=null) this.board.getCandies()[row+1][column].crush();
			if (row<=7 && column<=7 && this.board.getCandies()[row+1][column+1]!=null) this.board.getCandies()[row+1][column+1].crush();
					
			this.getBoard().refill();
					
			this.board.getCandies()[row][column]=null;
			this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
			
			if (row>=1 && column>=1) this.board.getCandies()[row-1][column-1].crush();
			if (row>=1) this.board.getCandies()[row-1][column].crush();
			if (row>=1 && column<=7) this.board.getCandies()[row-1][column+1].crush();
			if (column>=1) this.board.getCandies()[row][column-1].crush();
			if (column<=7) this.board.getCandies()[row][column+1].crush();
			if (row<=7 && column>=1) this.board.getCandies()[row+1][column-1].crush();
			if (row<=7) this.board.getCandies()[row+1][column].crush();
			if (row<=7 && column<=7) this.board.getCandies()[row+1][column+1].crush();
		}
	}

	@Override
	public void Visit(RegularCandy regular) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(HorizontalCandy horizontal) { //special combine with horizontal - crush 3 rows and 3 columns
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[horizontal.row][horizontal.column]=null;
		horizontal=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //crush 3 rows //if possible on board)
			if (this.board.getCandies()[i][column]!=null && i!=row)
				this.board.getCandies()[i][column].crush();
			if (column<8 && this.board.getCandies()[i][column+1]!=null)
				this.board.getCandies()[i][column+1].crush();
			if (column>0 && this.board.getCandies()[i][column-1]!=null)
				this.board.getCandies()[i][column-1].crush();
		}
		for (int j=0; j<9; j=j+1){ //crush 3 columns (if possible on board)
			if (this.board.getCandies()[row][j]!=null && j!=column)
				this.board.getCandies()[row][j].crush();
			if (row<8 && this.board.getCandies()[row+1][j]!=null)
				this.board.getCandies()[row+1][j].crush();
			if (row>0 && this.board.getCandies()[row-1][j]!=null)
				this.board.getCandies()[row-1][j].crush();
		}
	}

	@Override
	public void Visit(VerticalCandy vertical) { //special combine with vertical (the same as horizontal
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[vertical.row][vertical.column]=null;
		vertical=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){
			if (this.board.getCandies()[i][column]!=null && i!=row)
				this.board.getCandies()[i][column].crush();
			if (column<8 && this.board.getCandies()[i][column+1]!=null)
				this.board.getCandies()[i][column+1].crush();
			if (column>0 && this.board.getCandies()[i][column-1]!=null)
				this.board.getCandies()[i][column-1].crush();

		}
		for (int j=0; j<9; j=j+1){
			if (this.board.getCandies()[row][j]!=null && j!=column)
				this.board.getCandies()[row][j].crush();
			if (row<8 && this.board.getCandies()[row+1][j]!=null)
				this.board.getCandies()[row+1][j].crush();
			if (row>0 && this.board.getCandies()[row-1][j]!=null)
				this.board.getCandies()[row-1][j].crush();

		}
	}

	@Override
	public void Visit(WrappedCandy wrapped) { //special combine with wrapped - crush 5X5 around candy, refill the board, and do it again
		
		this.board.getCandies()[wrapped.row][wrapped.column]=null;
		wrapped=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		
		if (row>=1 && column>=1 && this.board.getCandies()[row-1][column-1]!=null) this.board.getCandies()[row-1][column-1].crush();
		if (row>=1 && this.board.getCandies()[row-1][column]!=null) this.board.getCandies()[row-1][column].crush();
		if (row>=1 && column<=7 && this.board.getCandies()[row-1][column+1]!=null) this.board.getCandies()[row-1][column+1].crush();
		if (column>=1 && this.board.getCandies()[row][column-1]!=null) this.board.getCandies()[row][column-1].crush();
		if (column<=7 && this.board.getCandies()[row][column+1]!=null) this.board.getCandies()[row][column+1].crush();
		if (row<=7 && column>=1 && this.board.getCandies()[row+1][column-1]!=null) this.board.getCandies()[row+1][column-1].crush();
		if (row<=7 && this.board.getCandies()[row+1][column]!=null) this.board.getCandies()[row+1][column].crush();
		if (row<=7 && column<=7 && this.board.getCandies()[row+1][column+1]!=null) this.board.getCandies()[row+1][column+1].crush();
		
		if (row>=2 && column>=2 && this.board.getCandies()[row-2][column-2]!=null) this.board.getCandies()[row-2][column-2].crush();
		if (row>=2 && column>=1 && this.board.getCandies()[row-2][column-1]!=null) this.board.getCandies()[row-2][column-1].crush();
		if (row>=2 && this.board.getCandies()[row-2][column]!=null) this.board.getCandies()[row-2][column].crush();
		if (row>=2 && column<=7 && this.board.getCandies()[row-2][column+1]!=null) this.board.getCandies()[row-2][column+1].crush();
		if (row>=2 && column<=6 && this.board.getCandies()[row-2][column+2]!=null) this.board.getCandies()[row-2][column+2].crush();
		if (row>=1 && column<=6 && this.board.getCandies()[row-1][column+2]!=null) this.board.getCandies()[row-1][column+2].crush();
		if (column<=6 && this.board.getCandies()[row][column+2]!=null) this.board.getCandies()[row][column+2].crush();
		if (row<=7 && column<=6 && this.board.getCandies()[row+1][column+2]!=null) this.board.getCandies()[row+1][column+2].crush();
		if (row<=6 && column<=6 && this.board.getCandies()[row+2][column+2]!=null) this.board.getCandies()[row+2][column+2].crush();
		if (row<=6 && column<=7 && this.board.getCandies()[row+2][column+1]!=null) this.board.getCandies()[row+2][column+1].crush();
		if (row<=6 && this.board.getCandies()[row+2][column]!=null) this.board.getCandies()[row+2][column].crush();
		if (row<=6 && column>=1 && this.board.getCandies()[row+2][column-1]!=null) this.board.getCandies()[row+2][column-1].crush();
		if (row<=6 && column>=2 && this.board.getCandies()[row+2][column-2]!=null) this.board.getCandies()[row+2][column-2].crush();
		if (row<=7 && column>=2 && this.board.getCandies()[row+1][column-2]!=null) this.board.getCandies()[row+1][column-2].crush();
		if (column>=2 && this.board.getCandies()[row][column-2]!=null) this.board.getCandies()[row][column-2].crush();
		if (row>=1 && column>=2 && this.board.getCandies()[row-1][column-2]!=null) this.board.getCandies()[row-1][column-2].crush();

		
		this.getBoard().refill();
		
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		
		if (row>=1 && column>=1) this.board.getCandies()[row-1][column-1].crush();
		if (row>=1) this.board.getCandies()[row-1][column].crush();
		if (row>=1 && column<=7) this.board.getCandies()[row-1][column+1].crush();
		if (column>=1) this.board.getCandies()[row][column-1].crush();
		if (column<=7) this.board.getCandies()[row][column+1].crush();
		if (row<=7 && column>=1) this.board.getCandies()[row+1][column-1].crush();
		if (row<=7) this.board.getCandies()[row+1][column].crush();
		if (row<=7 && column<=7) this.board.getCandies()[row+1][column+1].crush();
		
		
		if (row>=2 && column>=2) this.board.getCandies()[row-2][column-2].crush();
		if (row>=2 && column>=1) this.board.getCandies()[row-2][column-1].crush();
		if (row>=2) this.board.getCandies()[row-2][column].crush();
		if (row>=2 && column<=7) this.board.getCandies()[row-2][column+1].crush();
		if (row>=2 && column<=6) this.board.getCandies()[row-2][column+2].crush();
		if (row>=1 && column<=6) this.board.getCandies()[row-1][column+2].crush();
		if (column<=6) this.board.getCandies()[row][column+2].crush();
		if (row<=7 && column<=6) this.board.getCandies()[row+1][column+2].crush();
		if (row<=6 && column<=6) this.board.getCandies()[row+2][column+2].crush();
		if (row<=6 && column<=7) this.board.getCandies()[row+2][column+1].crush();
		if (row<=6) this.board.getCandies()[row+2][column].crush();
		if (row<=6 && column>=1) this.board.getCandies()[row+2][column-1].crush();
		if (row<=6 && column>=2) this.board.getCandies()[row+2][column-2].crush();
		if (row<=7 && column>=2) this.board.getCandies()[row+1][column-2].crush();
		if (column>=2) this.board.getCandies()[row][column-2].crush();
		if (row>=1 && column>=2) this.board.getCandies()[row-1][column-2].crush();

	}

	@Override
	public void Visit(ChocolateCandy chocolate) { //special combine with chocolate
		int savedColor=this.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[chocolate.row][chocolate.column]=null;
		chocolate=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //crush all of 'this' color
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
		
		this.board.refill();
		
		int newColor=(int)(Math.random()*6);
		while (newColor==savedColor){
			newColor=(int)(Math.random()*6);
		}
		for (int i=0; i<9; i=i+1){ //crush all of the new different random color
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==newColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
	}

	@Override
	public void accept(Visitor visitor) { //part of the visitor visited pattern
		visitor.Visit(this);
		
	}
}
