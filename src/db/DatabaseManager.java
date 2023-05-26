package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DatabaseManager {

	final private static String IP = "127.0.0.1";
	final private static int PORT = 3306;
	final private static String SCHEMA = "fruit";
	final private static String DB_ID = "root";
	final private static String DB_PASSWORD = "1234";
	static String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + SCHEMA;

	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;

	public static void connectDatabase() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, DB_ID, DB_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean makeConnection() {

		boolean connect = false;
		try {
			con = DriverManager.getConnection(url, DB_ID, DB_PASSWORD);
			stmt = con.createStatement();
			connect = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connect;
	}

	public static String[][] getFruits() {
		makeConnection();
		ArrayList<String[]> array = new ArrayList<String[]>();
		String[][] table = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM price_table");
			while (rs.next()) {
				String name = rs.getString("name");
				String wsPrice = rs.getInt("wholesale_price") + "";
				String sellPrice = rs.getInt("sell_price") + "";
				String stock = rs.getInt("stock") + "";
				String path = rs.getString("path");
				String row[] = { name, wsPrice, sellPrice, stock, path };
				array.add(row);
			}
			table = new String[array.size()][];
			for (int i = 0; i < array.size(); i++)
				table[i] = array.get(i);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return table;
	}

	public static String[] getFruit(String name) {
		makeConnection();
		String[] row = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM price_table WHERE name = '" + name + "'");
			if (rs.next()) {
				String wsPrice = rs.getInt("wholesale_price") + "";
				String sellPrice = rs.getInt("sell_price") + "";
				String stock = rs.getInt("stock") + "";
				String path = rs.getString("path");
				row = new String[] { name, wsPrice, sellPrice, stock, path };
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return row;
	}

	public static String[] getWholeSaleLog(int id) {
		makeConnection();
		String[] row = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM wholesale WHERE id = " + id + "");
			if (rs.next()) {
				String name = rs.getString("name");
				String wsStock = rs.getInt("wholesale_stock") + "";
				String loss = rs.getInt("loss_amount") + "";
				String stock = rs.getInt("stock") + "";
				String price = rs.getString("price") + "";
				String time = timeFormat(rs.getLong("time"), "yyyy년 MM월 dd일 HH시 mm분");
				row = new String[] { name, wsStock, loss, stock, price, time };
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return row;
	}

	public static String[] getSellLog(int id) {
		makeConnection();
		String[] row = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM sell WHERE id = " + id + "");
			if (rs.next()) {
				String name = rs.getString("name");
				String sellStock = rs.getInt("sell_stock") + "";
				String service = rs.getInt("service") + "";
				String stock = rs.getInt("stock") + "";
				String price = rs.getString("price") + "";
				String time = timeFormat(rs.getLong("time"), "yyyy년 MM월 dd일 HH시 mm분");
				row = new String[] { name, sellStock, service, stock, price, time };
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return row;
	}

	public static String[][] getSellLog() {
		makeConnection();
		ArrayList<String[]> array = new ArrayList<String[]>();
		String[][] table = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM sell ORDER BY time DESC");
			while (rs.next()) {
				String id = rs.getInt("id") + "";
				String name = rs.getString("name");
				String sellStock = rs.getInt("sell_stock") + "";
				String service = rs.getInt("service") + "";
				String stock = rs.getInt("stock") + "";
				String price = rs.getInt("price") + "";
				String time = timeFormat(rs.getLong("time"), "yyyy년 MM월 dd일 HH시 mm분");
				String row[] = { id, time, name, sellStock, service, stock, price };
				array.add(row);
			}
			table = new String[array.size()][];
			for (int i = 0; i < array.size(); i++)
				table[i] = array.get(i);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return table;
	}

	public static String[][] getWholeSaleLog() {
		makeConnection();
		ArrayList<String[]> array = new ArrayList<String[]>();
		String[][] table = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM wholesale ORDER BY time DESC");
			while (rs.next()) {
				String id = rs.getInt("id") + "";
				String name = rs.getString("name");
				String wsStock = rs.getInt("wholesale_stock") + "";
				String loss = rs.getInt("loss_amount") + "";
				String stock = rs.getInt("stock") + "";
				String price = rs.getInt("price") + "";
				String time = timeFormat(rs.getLong("time"), "yyyy년 MM월 dd일 HH시 mm분");
				String row[] = { id, time, name, wsStock, loss, stock, price };
				array.add(row);
			}
			table = new String[array.size()][];
			for (int i = 0; i < array.size(); i++)
				table[i] = array.get(i);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return table;
	}

	public static String[][] getLog(long start, long end, String name) {
		makeConnection();
		ArrayList<String[]> array = new ArrayList<String[]>();
		String[][] table = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM wholesale WHERE name = '" + name + "' AND time >= " + start
					+ " AND time <= " + end + "");
			while (rs.next()) {
				int wsStock = rs.getInt("wholesale_stock");
				int loss = rs.getInt("loss_amount");
				int price = rs.getInt("price");
				long time = rs.getLong("time");
				array.add(new String[] { time + "", name, wsStock + "", loss + "", "-", "-", "-" + price });
			}

			rs.close();
			stmt.close();

			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT * FROM sell WHERE name = '" + name + "' AND time >= " + start + " AND time <= " + end + "");
			while (rs.next()) {
				int sellStock = rs.getInt("sell_stock");
				int service = rs.getInt("service");
				int price = rs.getInt("price");
				long time = rs.getLong("time");
				array.add(new String[] { time + "", name, "-", "-", sellStock + "", service + "", "+" + price });
			}

			Collections.sort(array, new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					long time1 = Long.parseLong(o1[0]);
					long time2 = Long.parseLong(o2[0]);
					if (time1 == time2)
						return 0;
					else if (time1 > time2)
						return 1;
					else
						return -1;
				}

			});

			table = new String[array.size()][];
			for (int i = 0; i < array.size(); i++) {
				array.get(i)[0] = timeFormat(Long.parseLong(array.get(i)[0]), "yyyy년 MM월 dd일 HH시 mm분");
				table[i] = array.get(i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
		return table;
	}

	public static void addFruit(String name, int wsPrice, int sellPrice, int stock, String path) {
		PreparedStatement ps = null;
		makeConnection();
		try {
			ps = con.prepareStatement(
					"INSERT INTO price_table (name, wholesale_price, sell_price, stock, path) VALUES (?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setInt(2, wsPrice);
			ps.setInt(3, sellPrice);
			ps.setInt(4, stock);
			ps.setString(5, path);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void addSellLog(String name, int sellStock, int service, int stock, int price) {
		PreparedStatement ps = null;
		makeConnection();
		try {
			ps = con.prepareStatement(
					"INSERT INTO sell (name, sell_stock, service, stock, price, time) VALUES (?, ?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setInt(2, sellStock);
			ps.setInt(3, service);
			ps.setInt(4, stock);
			ps.setInt(5, price);
			ps.setLong(6, System.currentTimeMillis());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void addWholeSaleLog(String name, int wsStock, int loss, int stock, int price) {
		PreparedStatement ps = null;
		makeConnection();
		try {
			ps = con.prepareStatement(
					"INSERT INTO wholesale (name, wholesale_stock, loss_amount, stock, price, time) VALUES (?, ?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setInt(2, wsStock);
			ps.setInt(3, loss);
			ps.setInt(4, stock);
			ps.setInt(5, price);
			ps.setLong(6, System.currentTimeMillis());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void modifyFruit(String target, int wsPrice, int sellPrice, int stock) {
		modifyFruit(target, wsPrice, sellPrice, stock, null);
	}
	
	public static void modifyFruit(String target, int wsPrice, int sellPrice, int stock, String path) {
		makeConnection();
		try {
			if (path != null) 
				stmt.executeUpdate("UPDATE price_table SET wholesale_price = " + wsPrice + ", sell_price = " + sellPrice
					+ ", stock = " + stock + ", path  = '" + path + "' WHERE name = '" + target + "'");
			else 
				stmt.executeUpdate("UPDATE price_table SET wholesale_price = " + wsPrice + ", sell_price = " + sellPrice
						+ ", stock = " + stock + " WHERE name = '" + target + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void removeFruit(String name) {
		makeConnection();
		try {
			stmt.executeUpdate("DELETE FROM price_table WHERE name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void removeSellLog(int id) {
		String[] sellLog = getSellLog(id);
		String[] fruit = getFruit(sellLog[0]);
		int wsStock = Integer.parseInt(sellLog[1]);
		int service = Integer.parseInt(sellLog[2]);
		int amount = Integer.parseInt(fruit[3]);
		modifyFruit(sellLog[0], Integer.parseInt(fruit[1]), Integer.parseInt(fruit[2]), amount + wsStock + service);
		makeConnection();
		try {
			stmt.executeUpdate("DELETE FROM sell WHERE id = " + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void removeWholeSaleLog(int id) {
		PreparedStatement ps = null;
		String[] wsLog = getWholeSaleLog(id);
		String[] fruit = getFruit(wsLog[0]);
		// name, wsStock, loss, stock, price, time
		int wsStock = Integer.parseInt(wsLog[1]);
		int loss = Integer.parseInt(wsLog[2]);
		int amount = Integer.parseInt(fruit[3]);
		modifyFruit(wsLog[0], Integer.parseInt(fruit[1]), Integer.parseInt(fruit[2]), amount - wsStock + loss);
		makeConnection();
		try {
			stmt.executeUpdate("DELETE FROM wholesale WHERE id = " + id + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeAll();
	}

	public static void closeAll() {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		} finally {
			rs = null;
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
		} finally {
			stmt = null;
		}
	}

	public static String timeFormat(long time, String format) {
		// "yyyy년 MM월 dd일 HH시 mm분"
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
