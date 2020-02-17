import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	private JLabel position = new JLabel();
	private JTextField functionText = new JTextField();
	private Point2D.Double[] ptsArr;
	private Equation graphFunction = new Equation("");
	private Point[] XaxisArr;
	private Point[] YaxisArr;
	private int Xaxisdx;
	private int Yaxisdy;
	private Point axisStart;
	private Point axisEnd;
	private Point axisCenter;
	private int count;

	Panel() {
		position = new JLabel("(,)");
		position.setForeground(Color.white);
		functionText.setColumns(10);
		setLayout(new BorderLayout());
		function(graphFunction);
		position.setHorizontalAlignment(JLabel.CENTER);
		add(position, BorderLayout.NORTH);
		add(functionText, BorderLayout.SOUTH);
		addMouseMotionListener(new MouseMoveListener());
		addMouseWheelListener(new WheelListener());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		//draw x-axis
		g2.drawLine((int)XaxisArr[0].getX(), (int)XaxisArr[0].getY(), (int)XaxisArr[1].getX(), (int)XaxisArr[1].getY());
		//draw y-axis
		g2.drawLine((int)YaxisArr[0].getX(), (int)YaxisArr[0].getY(), (int)YaxisArr[1].getX(), (int)YaxisArr[1].getY());
		
		//draw graph
		for (Point2D.Double p : ptsArr) {
			g2.draw(new Ellipse2D.Double(p.x - axisCenter.getX(), p.y + axisCenter.getY(), 1, 1));
		}
		/**
		for (int i = 0; i < ptsArr.length - 1; i++) {
			g2.drawLine((int)(ptsArr[i].getX() - axisCenter.getX()), (int)(ptsArr[i].getY() + axisCenter.getY()), (int)(ptsArr[i+1].getX() - axisCenter.getX()), (int)(ptsArr[i+1].getY() + axisCenter.getY()));
		}
		**/
	}

	public void function(Equation fnc) {
		//init graph points
		double[] ptsX = DoubleStream.iterate(-50, i -> i + 1).limit(200).toArray();
		double[] ptsY = DoubleStream.iterate(-50, i -> i + 1).limit(200).toArray(); //.map(i -> (2 * Math.pow(i,2))).toArray();

		ptsY = applyFunction(ptsY,fnc);
		
		ptsArr = new Point2D.Double[200];
		for (int i = 0; i < 200; i++) {
			ptsArr[i] = new Point2D.Double(0, 0);
		}

		int j = 0;
		while (ptsX[j] < 50) {

			ptsArr[j].x = ptsX[j];
			ptsArr[j].y = -1*ptsY[j];
			j++;
		}
		
		
		
		//init x and y axis points		
		Xaxisdx = 0;
		Yaxisdy = 0;
		axisStart = new Point(0,0);
		axisEnd = new Point(0,0);
		
		XaxisArr = new Point[2];
		YaxisArr = new Point[2];
		
		XaxisArr[0] = new Point(-400, 0); 
		XaxisArr[1] = new Point(400, 0); 
		
		YaxisArr[0] = new Point(0, -400); 
		YaxisArr[1] = new Point(0, 400); 
		axisCenter = new Point(0,0);
		
		count = 0;
	}
	
	double[] applyFunction(double[] pts, Equation fnc) {
		return pts;
	}

	class MouseMoveListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent event) {
			axisStart = event.getPoint();
			
			if(count % 2 == 0) {
				axisEnd = event.getPoint();
			}
			
			Xaxisdx = (int)(axisEnd.getX() - axisStart.getX());
			Yaxisdy = (int)(axisEnd.getY() - axisStart.getY());
			
			axisCenter.translate(1*Xaxisdx, -1*Yaxisdy);
			
			XaxisArr[0].translate(-1*Xaxisdx, -1*Yaxisdy);
			XaxisArr[1].translate(-1*Xaxisdx, -1*Yaxisdy);
			YaxisArr[0].translate(-1*Xaxisdx, -1*Yaxisdy);
			YaxisArr[1].translate(-1*Xaxisdx, -1*Yaxisdy);
	
			position.setText("(" + (event.getPoint().x + (int)axisCenter.getX()) + "," + (-1*Math.abs(event.getPoint().y) + (int)axisCenter.getY()) + ")");
			position.repaint();
			
			repaint();
			count++;
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			position.setLocation(event.getPoint());
			position.setText("(" + ((event.getPoint().x) + (int)axisCenter.getX()) + "," + (-1*Math.abs(event.getPoint().y) + (int)axisCenter.getY()) + ")");
			position.repaint();
			count = 0;
		}
	}

	class WheelListener implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notch = e.getWheelRotation();
			if (notch < 0) {
				for (Point2D.Double p : ptsArr) {
					
					
					p.x = 1.25*(p.x);
					p.y = 1.25*(p.y);
				}
			} else {
				for (Point2D.Double p : ptsArr) {
					p.x = (p.x)/1.25;
					p.y = (p.y)/1.25;
				}
			}
			repaint();
		}
	}
	
	class keyPressListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				graphFunction.populate(functionText.getText());
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
