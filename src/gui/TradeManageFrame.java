package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.DatabaseManager;

public class TradeManageFrame extends JFrame implements Runnable {
	public static TradeManageFrame frame = null; 
	private static boolean isClosed = true;
	private boolean isSellMode = false;
	private int id = -1;
	private JPanel contentPane;
	private JPanel left, right, rightPane;
	private JLabel title, etcText;
	private JPanel header;
	private JButton apply = new JButton("Ȯ��");
	private JButton close = new JButton("���");
	private HashMap<String, Row> rows = new HashMap<String, Row>();
	
	/**
	 * �԰�, �Ǹ� CRUD ������
	 */
	public TradeManageFrame() {
		setUndecorated(true);
		setSize(700, 400);

		
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.BLACK));
		contentPane.setLayout(new BorderLayout());		
		
		title = new JLabel("", JLabel.CENTER);
		title.setFont(new Font("���� ���", Font.BOLD, 15));
		contentPane.add(title, BorderLayout.NORTH);		
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 2));
		
		left = new JPanel();
		center.add(left);		
		contentPane.add(center, BorderLayout.CENTER);

		rightPane = new JPanel(new BorderLayout());
		
		
		
		right = new JPanel();
		right.setLayout(new GridLayout(0, 1));
		rightPane.add(right, BorderLayout.CENTER);
		center.add(rightPane);
		
		JPanel bottom = new JPanel();
		bottom.add(apply);
		bottom.add(close);
		contentPane.add(bottom, BorderLayout.SOUTH);

		apply.addActionListener(new BtnEvent());
		close.addActionListener(new BtnEvent());
		setContentPane(contentPane);
		frame = this;
	}

	public void init(boolean isSell, String name, int amount, int etc, int id) {
		if (!isClosed)  // �̹� ���� â�� ������
			return;
		this.id = id;
		rows.clear();
		rightPane.removeAll();
		left.removeAll();
		isSellMode = isSell;
		title.setText(isSellMode ? "�Ǹ�" : "�԰�");
		JPanel header = new JPanel(new GridLayout(1, 3));
		header.add(new JLabel("���� ����", JLabel.CENTER));
		header.add(new JLabel("����", JLabel.CENTER));
		header.add(new JLabel(isSell ? "���񽺷�" : "�ν���", JLabel.CENTER));
		rightPane.add(header, BorderLayout.NORTH);
		
		right.removeAll();
		rightPane.add(right, BorderLayout.CENTER);

		String fruits[][] = DatabaseManager.getFruits();
		// name, wsPrice, sellPrice, stock, path
		for (String[] fruit : fruits) {
			addImage(fruit[0], fruit[4]);
		}
		rightPane.repaint();
		rightPane.revalidate();
		left.repaint();
		left.revalidate();
	}
	
	public void addRow(String name, int amount, int etc) {
		JPanel row = new Row(name, amount, etc);
		right.add(row);		
		right.repaint();
		right.revalidate();
	}	
	
	public void addImage(String name, String path) {
		if (path == null || path.equals(""))
			path = "no_image.jpg";
		JPanel ImagePanel = new ImagePanel("img/" + path, name);
		left.add(ImagePanel);
	}
	
	public void showFrame() { 
		if (!isClosed) { // �̹� ���� â�� ������
			JOptionPane.showMessageDialog(this, "�̹� â�� �����ֽ��ϴ�.");
			return;
		}
		setVisible(true);
		isClosed = false;
		Point loc = MainFrame.frame.getLocation();
		int x = (loc.x + MainFrame.frame.getWidth()) / 2; // ȭ�� �� ���� ������ ��� ��ǥ
		int y = (loc.y + MainFrame.frame.getHeight()) / 2; // ȭ�� �� ���� ������ ��� ��ǥ
		int width = getWidth();
		int height = getHeight();
		this.setLocation(x - (width / 2), y - (height / 2));
		Thread topThread = new Thread(this);
		topThread.start();
	}

	public void hideFrame() {
		setVisible(false);
		isClosed = true;
	}
	
	@Override
	public void run() { // �ش� ������ �ֻ������ ��ġ��Ŵ
		while(!isClosed) {
			setAlwaysOnTop(true); 
		}
	}
	

	
	class ImagePanel extends JPanel {
		private final String path;
		private final String name;
		
		public ImagePanel(String path, String name) {
			this.setLayout(new BorderLayout());
			this.path = path;
			this.name = name;

			ImageIcon icon = new ImageIcon(path);
			Image resize = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
			JLabel img = new JLabel(new ImageIcon(resize));
			this.add(img, BorderLayout.CENTER);
			
			this.add(new JLabel(name, JLabel.CENTER), BorderLayout.SOUTH);		
			this.addMouseListener(new ImageListener(name));
		}
		
	}
	
	class Row extends JPanel {
		private final String name; // ���� �̸�
		private int amount; // ����
		private int etc; // �ν� or ����
		private JTextField nameField = new JTextField(5);
		private JTextField amountField = new JTextField(5);
		private JTextField etcField = new JTextField(5);
		
		
		public Row(String name, int amount, int etc) {
			this.setLayout(new GridLayout(1, 3));
			this.name = name;
			this.amount = amount;
			this.etc = etc;
			nameField.setText(name);
			amountField.setText(amount + "");
			etcField.setText(etc + "");
			nameField.setEnabled(false);
			JPanel namePanel = new JPanel();
			JPanel amountPanel = new JPanel();
			JPanel etcPanel = new JPanel();
			namePanel.add(nameField);
			amountPanel.add(amountField);
			etcPanel.add(etcField);
			this.add(namePanel);
			this.add(amountPanel);
			this.add(etcPanel);
			rows.put(name, this);
		}
		
		public int getAmount() { 
			return Integer.parseInt(amountField.getText());
		}
		
		public int getEtc() {
			return Integer.parseInt(etcField.getText());
		}
		
		public String getName() {
			return this.name;
		}
	}

	class BtnEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == apply) { // ����
				ArrayList<String[]> array = new ArrayList<String[]>();
				for (String name : rows.keySet()) {
					Row row = rows.get(name);
					String amount = row.getAmount() + "";
					String etc = row.getEtc() + "";
					array.add(new String[] {name, amount, etc});
				}
				if (isSellMode) { // �Ǹ� ����
						for (String[] row : array) {
							String name = row[0];
							int amount = Integer.parseInt(row[1]);
							int service = Integer.parseInt(row[2]);
							String fruit[] = DatabaseManager.getFruit(name);
							int wsPrice = Integer.parseInt(fruit[1]);
							int sellPrice = Integer.parseInt(fruit[2]);
							int price = Integer.parseInt(fruit[1]);
							int stock = Integer.parseInt(fruit[3]) - amount - service;
							DatabaseManager.addSellLog(name, amount, service, stock, price * amount);
							DatabaseManager.modifyFruit(name, wsPrice, sellPrice, stock);
						}
				} else { // �԰� ����
						for (String[] row : array) {
							String name = row[0];
							int amount = Integer.parseInt(row[1]);
							int loss = Integer.parseInt(row[2]);
							String fruit[] = DatabaseManager.getFruit(name);
							int wsPrice = Integer.parseInt(fruit[1]);
							int sellPrice = Integer.parseInt(fruit[2]);
							int price = Integer.parseInt(fruit[1]);
							int stock = Integer.parseInt(fruit[3]) + amount - loss;
							DatabaseManager.addWholeSaleLog(name, amount, loss, stock, price * amount);
							DatabaseManager.modifyFruit(name, wsPrice, sellPrice, stock);
						}
					
				}
			} else if(e.getSource() == close) { // ���
			}
			hideFrame();
		}
	}
	
	class ImageListener implements MouseListener {
		private final String name;
		public ImageListener(String name) {
			this.name = name;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(rows.get(name) == null) {
				addRow(name, 0, 0);
			} else {
				JOptionPane.showMessageDialog(frame, name + "�� �̹� �������Դϴ�.");
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {			
		}
		
	}
	
}
