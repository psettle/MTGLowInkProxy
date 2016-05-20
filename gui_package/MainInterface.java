package gui_package;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cards.Card;
import cards.Creature;
import cards.InvalidCardNameException;
import cards.Planeswalker;

import pdf_driver.MagicProxyFile;

public class MainInterface implements ActionListener, WindowListener {
	
	private JTextArea cardlist;
	private JButton submit;
	private JTextField filename;
	private JFrame mainFrame;
	
	public MainInterface()
	{
		filename = new JTextField(20);
		filename.addActionListener(this);
		JLabel fileLabel = new JLabel("Filename:");
		JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filePanel.add(fileLabel);
		filePanel.add(filename);
		
		cardlist = new JTextArea();
		
		submit = new JButton("Create Proxies");
		submit.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(submit);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(filePanel, BorderLayout.NORTH);
		mainPanel.add(cardlist, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		mainFrame = new JFrame("MTG Low Ink Proxy Generator");
		mainFrame.setSize(600, 600);
		mainFrame.setResizable(false);
		mainFrame.add(mainPanel);
		mainFrame.addWindowListener(this);
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);
	}
	
	public static void main(String[] args)
	{
		new MainInterface();
	}
	
	public static Card createCard(String name) throws InvalidCardNameException
	{
		Card temp = new Card(name);
		ArrayList<String> type = temp.getTypes();
		if(type.contains("Creature")){
			return new Creature(name);
		} else if (type.contains("Planeswalker")){
			return new Planeswalker(name);
		} else {
			return temp;
		}
	}




	/**
	 * Parses the textArea and filename to create a pdf of proxies
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		Scanner input = new Scanner(cardlist.getText());
		String list_of_not_cards = "";
		MagicProxyFile proxies = new MagicProxyFile();
		proxies.setFilename(filename.getText() + ".pdf");
		while(input.hasNextLine())
		{
			String line = input.nextLine();
			Scanner partial_input = new Scanner(line);
			int count = 1;
			if(partial_input.hasNextInt())
			{
				count = partial_input.nextInt();
			}
			String cardname = null;
			if(partial_input.hasNextLine())
			{
				cardname = partial_input.nextLine().trim();
			}
			if(cardname == null)
			{
				continue;
			}
			try
			{
				Card card = createCard(cardname);
				for(int i = 0; i < count; i++)
				{
					proxies.addCard(card);
				}
			}
			catch(InvalidCardNameException exception)
			{
				list_of_not_cards += cardname + '\n';
			}
			partial_input.close();
		}
		Boolean successful = proxies.createProxies();
		if(!successful)
		{
			JOptionPane.showMessageDialog(mainFrame,
				    "Error Creating PDF, ensure the filename is valid, and the file is closed.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		else if(!list_of_not_cards.equals(""))
		{
			JOptionPane.showMessageDialog(mainFrame,
				    "The PDF " + filename.getText() + ".pdf was created. It can be found at: " + System.getProperty("user.dir") + ".\nHowever the following cards could not be identified:\n" + list_of_not_cards,
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(mainFrame,
				    "The PDF " + filename.getText() + ".pdf was created. It can be found at: " + System.getProperty("user.dir"),
				    "Success",
				    JOptionPane.PLAIN_MESSAGE);
		}
		input.close();
	}

	public void windowOpened(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		mainFrame.dispose();
		
	}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
