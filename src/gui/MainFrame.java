package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.DatabaseManager;
import gui.panel.PriceTablePanel;
import gui.panel.SellPanel;
import gui.panel.SellStatusPanel;
import gui.panel.WholeSalePanel;

public class MainFrame extends JFrame {
	public static MainFrame frame = null;
	private JPanel contentPane;
	private JPanel switchPanel;
	private JPanel menuPanel;
	private JButton priceTableBtn = new JButton("가격표");
	private JButton wholeSaleBtn = new JButton("입고");
	private JButton sellBtn = new JButton("판매");
	private JButton statusBtn = new JButton("매출현황");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DatabaseManager.connectDatabase();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
					new PriceModifyFrame();
					new TradeManageFrame();
					DatabaseManager.getLog(0, System.currentTimeMillis(), "사과");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1300, 700);
		setTitle("과일 재고 관리 프로그램");
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		
		switchPanel = new JPanel();
		switchPanel.setLayout(new BorderLayout());
		contentPane.add(switchPanel, BorderLayout.CENTER);
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(0, 1));

		menuPanel.add(priceTableBtn);
		menuPanel.add(wholeSaleBtn);
		menuPanel.add(sellBtn);
		menuPanel.add(statusBtn);
		contentPane.add(menuPanel, BorderLayout.EAST);

		priceTableBtn.addActionListener(new MenuSwitcher());
		wholeSaleBtn.addActionListener(new MenuSwitcher());
		sellBtn.addActionListener(new MenuSwitcher());
		statusBtn.addActionListener(new MenuSwitcher());
		setContentPane(contentPane);
		switchPanel(new PriceTablePanel());
		frame = this;
	}
	
	public void switchPanel(JPanel panel) {
		switchPanel.removeAll();
		switchPanel.add(panel);
		switchPanel.repaint();
		switchPanel.revalidate();
		switchPanel.setBackground(Color.blue);
	}

	class MenuSwitcher implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			if (btn == priceTableBtn) {
				switchPanel(new PriceTablePanel());
			} else if (btn == wholeSaleBtn) {
				switchPanel(new WholeSalePanel());				
			} else if (btn == sellBtn) {
				switchPanel(new SellPanel());				
			} else if (btn == statusBtn) {
				switchPanel(new SellStatusPanel());				
			}
		} 
		
	}
}
