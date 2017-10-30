package com.sportspartner.dao.impl;

import com.sportspartner.dao.UserDao;
import com.sportspartner.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sportspartner.util.ConnectionUtil;


public class UserDaoImpl implements UserDao {


    public List<User> getAllUsers() {

        Connection c = new ConnectionUtil().connectDB();

        List<User> users = new ArrayList<User>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM \"User\";");

            while (rs.next()) {
                String userId = rs.getString("userId");
                String password = rs.getString("password");
                String type = rs.getString("type");

                users.add(new User(userId, password, type));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                rs.close();
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return users;
    }

    public User getUser(String userId) {

        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user = null;

        try {
            stmt = c.prepareStatement("SELECT * FROM \"User\" WHERE \"userId\" = ?;");
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getString("userId");
                String password = rs.getString("password");
                String type = rs.getString("type");
                user = new User(userId, password, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        } finally {
            try {
                rs.close();
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return user;
    }
    public boolean newUser(User user){

        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String userId = user.getUserId();
        String password = user.getPassword();
        String type = user.getType();

        boolean result = false;

        try {
            stmt = c.prepareStatement("INSERT INTO \"User\" (\"userId\", \"password\", \"type\")"+
                "VALUES (?, ?, ?)");
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.setString(3, type);
            rs = stmt.executeUpdate();
            if(rs>0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
    public boolean updateUser(User user){

        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String userId = user.getUserId();
        String password = user.getPassword();
        String type = user.getType();

        boolean result = false;
        try {
            stmt = c.prepareStatement("UPDATE \"User\" SET \"password\" = ?, \"type\" = ? WHERE \"userId\"=?;");
            stmt.setString(1, password);
            stmt.setString(2, type);
            stmt.setString(3, userId);
            rs = stmt.executeUpdate();
            if(rs>0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean deleteUser(String userId){

        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;

        boolean result = false;

        try {
            stmt = c.prepareStatement("DELETE FROM \"User\" WHERE \"userId\"=?;");
            stmt.setString(1, userId);
            rs = stmt.executeUpdate();
            if(rs>0){
                System.out.println("Row "+ rs + "is DELETED.");
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
}