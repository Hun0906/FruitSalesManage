package gui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import db.DatabaseManager;
import gui.MainFrame;
import gui.PriceModifyFrame;
import gui.TradeManageFrame;

public class WholeSalePanel extends JPanel {

	/**
	 * 입고 패널
	 */

	private String header[] = { "고유번호", "날짜", "과일 이름", "입고량", "로스량", "재고량", "금액" };
	private JButton insertBtn = new JButton("입고 등록");
	private JButton removeBtn = new JButton("삭제");

	private JTable table;

	public WholeSalePanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("입고 현황", JLabel.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);

		String contents[][] = DatabaseManager.getWholeSaleLog();

		table = new JTable(contents, header);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		tableCellCenter(table);
		resizeColumnWidth(table);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.add(insertBtn);
		bottom.add(removeBtn);
		add(bottom, BorderLayout.SOUTH);
		
		
		insertBtn.addActionListener(new BtnEvent());
		removeBtn.addActionListener(new BtnEvent());
	}
	
	public void tableCellCenter(JTable t) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = t.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 50; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}

	class BtnEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == insertBtn) { // 등록
				TradeManageFrame.frame.init(false, "", 0, 0, -1);
				TradeManageFrame.frame.showFrame();
			} else if (e.getSource() == removeBtn) { // 삭제
				int row = table.getSelectedRow();
				int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());				
				DatabaseManager.removeWholeSaleLog(id);
				JOptionPane.showMessageDialog(MainFrame.frame, "삭제가 완료됐습니다.");
				
			}
		}
	}

}
