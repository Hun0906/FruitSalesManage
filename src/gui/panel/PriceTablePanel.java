package gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class PriceTablePanel extends JPanel {
	/**
	 * 가격표 패널
	 */
	private String header[] = {"과일 이름", "도매가", "판매가", "재고량"};
	private JButton regBtn = new JButton("등록");
	private JButton modifyBtn = new JButton("수정");
	private JButton removeBtn = new JButton("삭제");
	
	private JTable table;
	
	public PriceTablePanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("가격표", JLabel.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);
		
		String contents[][] = DatabaseManager.getFruits();
		
		table = new JTable(contents, header);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		tableCellCenter(table);
		resizeColumnWidth(table);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttom = new JPanel();
		buttom.add(regBtn);
		buttom.add(modifyBtn);
		buttom.add(removeBtn);
		add(buttom, BorderLayout.SOUTH);

		regBtn.addActionListener(new BtnEvent());
		modifyBtn.addActionListener(new BtnEvent());
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
			if (e.getSource() == regBtn) { // 등록
				PriceModifyFrame.frame.setInformation("", "", "", "", "");
			} else if(e.getSource() == modifyBtn) { // 수정		
				int row = table.getSelectedRow();
				String name = table.getModel().getValueAt(row, 0).toString();
				String data[] = DatabaseManager.getFruit(name);
				PriceModifyFrame.frame.setInformation(data[0], data[1], data[2], data[3], data[4]);
			} else if (e.getSource() == removeBtn) { // 삭제
				int row = table.getSelectedRow();
				String name = table.getModel().getValueAt(row, 0).toString();
				DatabaseManager.removeFruit(name);
				JOptionPane.showMessageDialog(MainFrame.frame, name + "(이)가 삭제됐습니다.");
				return;
			}
			PriceModifyFrame.frame.showFrame();
		}
		
	}
}
