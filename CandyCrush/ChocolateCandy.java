package CandyCrush;

public class ChocolateCandy extends Candy{
	
	public ChocolateCandy(Board board,int row,int column) {
		super(-1,board,row,column);
	}
	
	@Override
	public void crush() { //crush - if chocolate crushes (not part of special combine) choose color randomly and crush them all
		if (!wasCrushed){
			wasCrushed=true;
			this.board.getCandies()[row][column]=null;
			this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
			int rnd=(int) Math.random()*6;
			for (int i=0; i<9; i=i+1){
				for (int j=0; j<9; j=j+1){
					if (this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==rnd) this.board.getCandies()[i][j].crush();
				}
			}
		}
	}
	@Override
	public void Visit(RegularCandy regular) { //special combine with a regular (crush all the candies with that color and set the chocolate to null
		int savedColor=regular.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[regular.row][regular.column]=null;
		regular=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());

		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
		
	}
	@Override
	public void Visit(HorizontalCandy horizontal) { //special combine with horizontal
		int savedColor=horizontal.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[horizontal.row][horizontal.column]=null;
		horizontal=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //turn all the candies with that color to vertical or horizontal (randomly
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					if ((int)(Math.random()*2)==1)
						this.board.getCandies()[i][j]=new VerticalCandy(savedColor,board,i,j);
					else
						this.board.getCandies()[i][j]=new HorizontalCandy(savedColor,board,i,j);
				}
			}
		}
	
		for (int i=0; i<9; i=i+1){ //crush all of the candies of that color (horizontal/vertical due to the previous loop)
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
	}
	@Override
	public void Visit(VerticalCandy vertical) { //special combine with vertical - (the same as horizontal)
		int savedColor=vertical.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[vertical.row][vertical.column]=null;
		vertical=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					if ((int)(Math.random()*2)==1)
						this.board.getCandies()[i][j]=new VerticalCandy(savedColor,board,i,j);
					else
						this.board.getCandies()[i][j]=new HorizontalCandy(savedColor,board,i,j);
				}
			}
		}
	
		for (int i=0; i<9; i=i+1){
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==savedColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
	}
	@Override
	public void Visit(WrappedCandy wrapped) { //special combine with wrapped
		int savedColor=wrapped.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[wrapped.row][wrapped.column]=null;
		wrapped=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //crush all of the same color
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
		for (int i=0; i<9; i=i+1){ //crush all of the same of a different random color
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null && this.board.getCandies()[i][j].getColor()==newColor){
					this.board.getCandies()[i][j].crush();
				}
			}
		}
		
	}
	@Override
	public void Visit(ChocolateCandy chocolate) { //special combine with chocolate (!!!)
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[chocolate.row][chocolate.column]=null;
		chocolate=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		for (int i=0; i<9; i=i+1){ //crush all the candies on board
			for (int j=0; j<9; j=j+1){
				if(this.board.getCandies()[i][j]!=null){
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
