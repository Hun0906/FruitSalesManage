package gui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import db.DatabaseManager;
import gui.MainFrame;
import gui.TradeManageFrame;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class SellStatusPanel extends JPanel {

	/**
	 * 매출현황 패널
	 */
	private String header[] = { "날짜", "과일 이름", "입고량", "로스량", "판매량", "서비스량", "수익" };
	private JTable table;
	private UtilDateModel dateModel1, dateModel2;
	private JComboBox combo;
	private JButton search = new JButton("검색");
	private DefaultTableModel model;

	public SellStatusPanel() {
		setLayout(new BorderLayout());
		JPanel top = new JPanel();
		dateModel1 = new UtilDateModel();
		JDatePanelImpl datePanel1 = new JDatePanelImpl(dateModel1);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1);

		JLabel spl = new JLabel(" - ");

		dateModel2 = new UtilDateModel();
		JDatePanelImpl datePanel2 = new JDatePanelImpl(dateModel2);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2);

		String fruits[][] = DatabaseManager.getFruits();
		String names[] = new String[fruits.length];
		for (int i = 0; i < fruits.length; i++) {
			names[i] = fruits[i][0];
		}

		combo = new JComboBox(names);
		top.add(datePicker1);
		top.add(spl);
		top.add(datePicker2);
		top.add(combo);
		top.add(search);

		add(top, BorderLayout.NORTH);

		model = new DefaultTableModel(new String[][] {}, header);
		table = new JTable(model);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		tableCellCenter(table);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		search.addActionListener(new BtnEvent());
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
			if (e.getSource() == search) { // 검색
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String startDateStr = dateModel1.getYear() + "-" + (dateModel1.getMonth() + 1) + "-"
							+ dateModel1.getDay() + " 00:00:00";
					String endDateStr = dateModel2.getYear() + "-" + (dateModel2.getMonth() + 1) + "-"
							+ dateModel2.getDay() + " 23:59:59";
					long timeStamp1 = formatter.parse(startDateStr).getTime();
					long timeStamp2 = formatter.parse(endDateStr).getTime();
					model = new DefaultTableModel(
							DatabaseManager.getLog(timeStamp1, timeStamp2, combo.getSelectedItem().toString()), header);
					table.setModel(model);
					tableCellCenter(table);
					model.fireTableDataChanged();
					resizeColumnWidth(table);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}
