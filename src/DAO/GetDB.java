package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import model.User;

/**
 *
 * @author admin
 */
public class GetDB {

    public static Connection con = null;
    String dbUrl = "jdbc:mysql://localhost:3306/game?autoReconnect=true&useSSL=false";
    String dbClass = "com.mysql.cj.jdbc.Driver";
    String username = "root";
    String password = "anhpro109";

    public GetDB() {
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            e.getStackTrace().toString();
        }
    }

    public User getUserDB(User user2) throws SQLException {
        PreparedStatement ps = null;
        String query = "Select * FROM user WHERE username ='" + user2.getUsername() + "'";
        ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        while (rs.next()) {
            user.setName(rs.getString("name"));
            user.setPoint(rs.getDouble("point"));
            user.setTime(rs.getTime("time"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setWinGames(rs.getInt("winGames"));
        }
        return user;
    }

    public ArrayList<User> getUserListDB() throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        PreparedStatement ps = null;
        String query = "Select * FROM user";
        ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setPoint(rs.getDouble("point"));
            user.setTime(rs.getTime("time"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setWinGames(rs.getInt("winGames"));
            list.add(user);
        }
        return list;
    }

    public boolean addUserDB(User user) {
        try {
            PreparedStatement preparedStmt = null;
            String query1 = "SELECT * FROM user WHERE user.username = ?;";
            preparedStmt = con.prepareStatement(query1);
            preparedStmt.setString(1, user.getUsername());
            ResultSet rs = preparedStmt.executeQuery();
            if (!rs.next()) {
                String query2 = "Insert into user(username,password,name,point,winGames,time) values(?,?,?,?,?,?)";
                PreparedStatement preparedStmt2 = con.prepareStatement(query2);
                preparedStmt2.setString(1, user.getUsername());
                preparedStmt2.setString(2, user.getPassword());
                preparedStmt2.setString(3, user.getName());
                preparedStmt2.setDouble(4, 0.0);
                preparedStmt2.setInt(5, 0);
                Time time = new Time(00, 00, 00);
                preparedStmt2.setTime(6, time);
                preparedStmt2.execute();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(User user) throws Exception {
        String query = "Select * FROM user WHERE username ='" + user.getUsername()
                + "' AND password ='" + user.getPassword() + "'";
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    public void updatePoint(User u, double point) {
        User u1;
        try {
            u1 = getUserDB(u);
            u1.setPoint(u1.getPoint() + point);
            String query = "update user set point = " + u1.getPoint() + " where username = " + "'" + u1.getUsername() + "'";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void addGameWin(User u) {
        User u1;
        try {
            u1 = getUserDB(u);
            u1.setWinGames(u1.getWinGames() + 1);
            String query = "update user set winGames = " + u1.getWinGames() + " where username = " + "'" + u1.getUsername() + "'";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void updateTime(User u, Time time) {
        User u1;
        try {
            u1 = getUserDB(u);
            String[] s0 = u1.getTime().toString().split(":");
            String[] s1 = time.toString().split(":");
            String[] s2 = null;
            int h, m, se;
            h = Integer.parseInt(s0[0]) + Integer.parseInt(s1[0]);
            m = Integer.parseInt(s0[1]) + Integer.parseInt(s1[1]);
            se = Integer.parseInt(s0[2]) + Integer.parseInt(s1[2]);
            if (se > 59) {
                se = se % 60;
                m++;
                if (m > 59) {
                    m = m % 60;
                    h++;
                }
            } else {
                if (m > 59) {
                    m = m % 60;
                    h++;
                }
            }
            Time time1 = new Time(h, m, se);
            u1.setTime(time1);
            String query = "update user set time = " + "'" + u1.getTime() + "'" + " where username = " + "'" + u1.getUsername() + "'";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
