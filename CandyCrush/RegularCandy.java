package CandyCrush;

public class RegularCandy extends Candy {

	public RegularCandy(int color, Board board,int row,int column) {
		super(color, board,row,column);		
	}

	@Override
	public void crush() { //crushing a regular candy - set to null and add points
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
	}

	@Override
	public void Visit(RegularCandy regular) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(HorizontalCandy horizontal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(VerticalCandy vertical) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(WrappedCandy wrapped) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Visit(ChocolateCandy chocolate) { //special combine with chocolate - crush all the candies with 'this' color
		int savedColor=this.getColor();
		this.board.getCandies()[row][column]=null;
		this.getBoard().getGame().setScoreInt(this.getBoard().getGame().getScoreInt()+this.getBoard().getPointFactor());
		this.board.getCandies()[chocolate.row][chocolate.column]=null;
		chocolate=null;
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
	public void accept(Visitor visitor) { //part of the visitor visited pattern
		visitor.Visit(this);
		
	}

}
