package skchew;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WPConfig extends JPanel {

	public WPConfig() {
			
			JPanel wpLPanel = new JPanel();
			wpLPanel.setLayout(new BorderLayout());
	
			wpLPanel.add(new JLabel ("WayPoint Configuration"), BorderLayout.NORTH);
			wpLPanel.add (new JLabel ("Select your countries"), BorderLayout.WEST);
			wpLPanel.add (new JLabel ("Your quick link list"), BorderLayout.EAST);
	
			add(wpLPanel);
			WPConfig2 panel2 = new WPConfig2();
	}
	
}
