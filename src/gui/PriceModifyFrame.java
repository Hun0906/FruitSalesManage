package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.DatabaseManager;

public class PriceModifyFrame extends JFrame implements Runnable {
	public static PriceModifyFrame frame = null;
	private static boolean isClosed = true;
	private JPanel contentPane;
	private JButton apply = new JButton("����");
	private JButton close = new JButton("���");
	private JTextField nameField = new JTextField(20);
	private JTextField wsPriceField = new JTextField(20);
	private JTextField sellPriceField = new JTextField(20);
	private JTextField stockField = new JTextField(20);
	private JTextField pathField = new JTextField(45);
	private JLabel title;
	private boolean modifyMode = false;

	/**
	 * ����ǥ CRUD ������
	 */
	
	public PriceModifyFrame() {
		setUndecorated(true);
		setSize(250, 200);
		
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.BLACK));
		contentPane.setLayout(new BorderLayout());
		
		title = new JLabel("", JLabel.CENTER);
		title.setFont(new Font("���� ���", Font.BOLD, 15));
		contentPane.add(title, BorderLayout.NORTH);
		
		JPanel center = new JPanel(new GridLayout(5, 2));		
		JLabel name = new JLabel("���� �̸� : ", JLabel.RIGHT);
		JLabel wsPrice = new JLabel("���Ű� : ", JLabel.RIGHT);
		JLabel sellPrice = new JLabel("�ǸŰ� : ", JLabel.RIGHT);
		JLabel stock = new JLabel("��� : ", JLabel.RIGHT);
		JLabel path = new JLabel("�̹��� ��� : ", JLabel.RIGHT);
		center.add(name);
		center.add(nameField);
		center.add(wsPrice);
		center.add(wsPriceField);
		center.add(sellPrice);
		center.add(sellPriceField);
		center.add(stock);
		center.add(stockField);
		center.add(path);
		center.add(pathField);
		contentPane.add(center, BorderLayout.CENTER);
		
		JPanel buttom = new JPanel();
		buttom.add(apply);
		buttom.add(close);
		contentPane.add(buttom, BorderLayout.SOUTH);

		apply.addActionListener(new BtnEvent());
		close.addActionListener(new BtnEvent());
		
		setContentPane(contentPane);
		frame = this;
	}
	
	public void setInformation(String name, String wholeSalePrice, String sellPrice, String stock, String imgPath) {
		if (!isClosed) // �̹� ���� â�� ������
			return;

		
		if (!name.equals("")) { // ����
			title.setText("����");
			nameField.setEnabled(false);
			modifyMode = true;
		} else { // ���
			title.setText("���");
			nameField.setEnabled(true);
			modifyMode = false;
		}
		nameField.setText(name);
		wsPriceField.setText(wholeSalePrice);
		sellPriceField.setText(sellPrice);
		stockField.setText(stock);
		pathField.setText(imgPath);
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
	
	class BtnEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == apply) { // ����
				if (modifyMode) {
					DatabaseManager.modifyFruit(nameField.getText(), 
							Integer.parseInt(wsPriceField.getText()), 
							Integer.parseInt(sellPriceField.getText()), 
							Integer.parseInt(stockField.getText()),
							pathField.getText());
				} else {
					DatabaseManager.addFruit(nameField.getText(), 
							Integer.parseInt(wsPriceField.getText()), 
							Integer.parseInt(sellPriceField.getText()), 
							Integer.parseInt(stockField.getText()),
							pathField.getText());
				}
				JOptionPane.showMessageDialog(frame, "������ �Ϸ�ƽ��ϴ�.");
			} else if(e.getSource() == close) { // ���			
			}
			hideFrame();
		}
		
	}
}
