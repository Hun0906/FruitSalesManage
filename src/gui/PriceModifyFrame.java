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
	private JButton apply = new JButton("적용");
	private JButton close = new JButton("취소");
	private JTextField nameField = new JTextField(20);
	private JTextField wsPriceField = new JTextField(20);
	private JTextField sellPriceField = new JTextField(20);
	private JTextField stockField = new JTextField(20);
	private JTextField pathField = new JTextField(45);
	private JLabel title;
	private boolean modifyMode = false;

	/**
	 * 가격표 CRUD 프레임
	 */
	
	public PriceModifyFrame() {
		setUndecorated(true);
		setSize(250, 200);
		
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.BLACK));
		contentPane.setLayout(new BorderLayout());
		
		title = new JLabel("", JLabel.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		contentPane.add(title, BorderLayout.NORTH);
		
		JPanel center = new JPanel(new GridLayout(5, 2));		
		JLabel name = new JLabel("과일 이름 : ", JLabel.RIGHT);
		JLabel wsPrice = new JLabel("도매가 : ", JLabel.RIGHT);
		JLabel sellPrice = new JLabel("판매가 : ", JLabel.RIGHT);
		JLabel stock = new JLabel("재고량 : ", JLabel.RIGHT);
		JLabel path = new JLabel("이미지 경로 : ", JLabel.RIGHT);
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
		if (!isClosed) // 이미 열린 창이 있으면
			return;

		
		if (!name.equals("")) { // 수정
			title.setText("수정");
			nameField.setEnabled(false);
			modifyMode = true;
		} else { // 등록
			title.setText("등록");
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
		if (!isClosed) { // 이미 열린 창이 있으면
			JOptionPane.showMessageDialog(this, "이미 창이 열려있습니다.");
			return;
		}
		setVisible(true);
		isClosed = false;
		Point loc = MainFrame.frame.getLocation();
		int x = (loc.x + MainFrame.frame.getWidth()) / 2; // 화면 상 메인 프레임 가운데 좌표
		int y = (loc.y + MainFrame.frame.getHeight()) / 2; // 화면 상 메인 프레임 가운데 좌표
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
	public void run() { // 해당 프레임 최상단으로 위치시킴
		while(!isClosed) {
			setAlwaysOnTop(true);
		}
	}
	
	class BtnEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == apply) { // 적용
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
				JOptionPane.showMessageDialog(frame, "적용이 완료됐습니다.");
			} else if(e.getSource() == close) { // 취소			
			}
			hideFrame();
		}
		
	}
}
