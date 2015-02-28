import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ClipboardItem extends JPanel implements ActionListener {

    JLabel label = new JLabel("");
    JButton copyButton = new JButton("Copy");
    String text;
    ClipboardManager manager;

    public ClipboardItem(ClipboardManager cbm, String currCopy) {
    	this.setBackground(Color.gray);
		copyButton.addActionListener(this);
		this.text = currCopy;
		label.setText(currCopy);
		this.setPreferredSize(new Dimension(400, 50));
		this.add(label);
		this.add(copyButton);
		manager = cbm;
    }
    
    public JLabel getLabel() {
		return label;
	}
    
    public String getText() {
		return text;
	}

	public JButton getCopyButton() {
		return copyButton;
	}

	public void actionPerformed(ActionEvent e) {
		manager.updateClipboardItem(this);
    }
}
