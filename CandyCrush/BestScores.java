package CandyCrush;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class BestScores extends JFrame implements MouseListener {
	
	private JTable table;
	private JTableHeader head;
	
	public BestScores(Game game){
		super("Best Scores:");
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout());
		String[] headLines={"Name","Score"}; //create the headlines
		this.table=new JTable(game.getBest(),headLines); //create table
		this.head=table.getTableHeader();
		head.setFont(new Font("David", Font.BOLD, 26)); //set font
		table.setShowHorizontalLines(true); //show the table as a grid
        table.setShowVerticalLines(true); //""
        table.setRowHeight(32);
        table.setFont(new Font("David", Font.BOLD, 22)); //set the text: font+size
        DefaultTableCellRenderer dt = new DefaultTableCellRenderer(); //center the score column 
        dt.setHorizontalAlignment(JLabel.CENTER); //""
        table.getColumnModel().getColumn(1).setCellRenderer(dt); //""
        table.setBackground(new Color(0,0,0,0)); //set transparent background
        head.setBackground(new Color(0,0,0,0)); //""
        head.setForeground(Color.BLUE); //differ the header from the rest of the table
        this.getContentPane().add(head,BorderLayout.NORTH);
		this.getContentPane().add(table,BorderLayout.CENTER);
		this.addMouseListener(this); //only for repaint causes (if the window is dragged "out" of the screen)
		table.addMouseListener(this); //""
		head.addMouseListener(this); //""
		this.setSize(300,380);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { //repaint after every event to prevent graphical "errors"
		this.repaint();
		this.table.repaint();
		this.head.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { //""
		this.repaint();
		this.table.repaint();
		this.head.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) { //""
		this.repaint();
		this.table.repaint();
		this.head.repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) { //""
		this.repaint();
		this.table.repaint();
		this.head.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) { //""
		this.repaint();
		this.table.repaint();
		this.head.repaint();
	}

}

