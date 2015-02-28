import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class ClipboardManager {

	public ArrayList<ClipboardItem> itemList = new ArrayList<ClipboardItem>();
	JFrame frame = new JFrame();
	
	public static void main(String[] args) {
		ClipboardManager cbm = new ClipboardManager();
	}
	
// ClipboardManager constructor
	public ClipboardManager() {
		frame.setTitle("Clipboard Manager");
		frame.setPreferredSize(new Dimension(450, 600));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.pack();
		frame.setVisible(true);
		
		String currCopy = getClipboard();
		if(currCopy != null)
			createClipboardItem(currCopy);
		
		Thread t = new Thread(new CheckClipboard());
        t.setDaemon(true);
        t.start();
	}
	

	public void createClipboardItem(String currCopy) {
		itemList.add(new ClipboardItem(this, currCopy));
		int i = (itemList.size() == 1) ? 1 : 2;
		itemList.get(itemList.size()-i).setBackground(Color.gray);
		itemList.get(itemList.size()-1).setBackground(Color.yellow);
		frame.add(itemList.get(itemList.size()-1));
		frame.revalidate();
		frame.repaint();
	}

// rearrange clipboard item to current order
	public void updateClipboardItem(ClipboardItem updateItem) {
		for(int i = 0; i < itemList.size(); i++) {
			if(itemList.get(i).getLabel().getText().equals(updateItem.getLabel().getText())) {
				frame.remove(itemList.get(i));
				itemList.remove(itemList.get(i));
				break;
			}
		}
		itemList.get(itemList.size()-1).setBackground(Color.gray);
		itemList.add(updateItem);
		itemList.get(itemList.size()-1).setBackground(Color.yellow);
		setClipboard(updateItem.getText());
		frame.add(itemList.get(itemList.size()-1));
		frame.revalidate();
		frame.repaint();
	}
	
// get current string on clipboard
	public String getClipboard() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		 try {
	        if (t != null) {
	        	if(t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	        		String clipboard = (String)t.getTransferData(DataFlavor.stringFlavor);
	        		return clipboard;
	        	}    	
	        }
	    } catch (Exception e) {
	    }
		return "INVALID INPUT";
	}
	
// reassign clipboard to new item
	public void setClipboard(String s) {
        StringSelection ss = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

// check every 1 second if there is a new entry in the clipboard
	public class CheckClipboard implements Runnable {
		@Override
		public void run() {
			String lastCopy = getClipboard();
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				String currCopy = getClipboard();
				boolean isNewItem = true;
				if(currCopy != null && !currCopy.equals(lastCopy)) {
					for(ClipboardItem item: itemList) {
						if(item.getText().equals(currCopy)) {
							isNewItem = false;
							break;
						}
					}
					if(isNewItem) {
						createClipboardItem(currCopy);
						lastCopy = currCopy;
					}
				}
			}
		}
	}
}
