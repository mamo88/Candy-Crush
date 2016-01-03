package CandyCrush;

public class VerticalCandy extends Candy {
	
	public VerticalCandy(int color, Board board,int row,int column) {
		super(color, board,row,column);		
	}

	@Override
	public void crush() { //crush vertical - crush all the column
		if (!wasCrushed){
			wasCrushed=true;
			for (int i=0; i<9; i=i+1)
				if (this.board.getCandies()[i][column]!=null && i!=row) 
					this.board.getCandies()[i][column].crush();
			this.board.getCandies()[row][column]=null;
			this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		}	
	}

	@Override
	public void Visit(RegularCandy regular) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Visit(HorizontalCandy horizontal) { //special combine with horizontal - crush all the row and all the column
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[horizontal.row][horizontal.column]=null;
		horizontal=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //crush column
			if (this.board.getCandies()[i][column]!=null && i!=row)
				this.board.getCandies()[i][column].crush();
		}
		for (int j=0; j<9; j=j+1){ //crush row
			if (this.board.getCandies()[row][j]!=null && j!=column)
				this.board.getCandies()[row][j].crush();
		}		
	}

	@Override
	public void Visit(VerticalCandy vertical) { //special combine with vertical - the same as horizontal
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[vertical.row][vertical.column]=null;
		vertical=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){
			if (this.board.getCandies()[i][column]!=null && i!=row)
				this.board.getCandies()[i][column].crush();
		}
		for (int j=0; j<9; j=j+1){
			if (this.board.getCandies()[row][j]!=null && j!=column)
				this.board.getCandies()[row][j].crush();
		}
	}

	@Override
	public void Visit(WrappedCandy wrapped) { //special combine with wrapped - crush 3 rows and 3 columns
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[wrapped.row][wrapped.column]=null;
		wrapped=null;
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
	public void Visit(ChocolateCandy chocolate) { //special combine with chocolate 
		int savedColor=this.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[chocolate.row][chocolate.column]=null;
		chocolate=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //turn all of the same color to vertical/horizontal randomly
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					if ((int)(Math.random()*2)==1)
						this.board.getCandies()[i][j]=new VerticalCandy(savedColor,board,i,j);
					else
						this.board.getCandies()[i][j]=new HorizontalCandy(savedColor,board,i,j);
				}
			}
		}
	
		for (int i=0; i<9; i=i+1){ //crush all of that color (all vertical/horizontal due to previous loops)
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
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
