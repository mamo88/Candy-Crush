package CandyCrush;

public interface Visitor {
	public void Visit(RegularCandy regular);
	public void Visit(HorizontalCandy horizontal);
	public void Visit(VerticalCandy vertical);
	public void Visit(WrappedCandy wrapped);
	public void Visit(ChocolateCandy chocolate);
}
