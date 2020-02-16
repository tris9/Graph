import javax.swing.JFrame;

public class Frame {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500,500);
		frame.setTitle("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Panel());
		frame.setVisible(true);
	}

}
