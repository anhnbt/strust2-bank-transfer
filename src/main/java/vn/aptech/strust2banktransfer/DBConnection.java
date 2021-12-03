/*
 * Copyright 2021 by AnhNBT
 */
package vn.aptech.strust2banktransfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
 */
public class DBConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Connecting...");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/struts2_demo?autoReconnect=true&useSSL=false", "anhnbt", "KhoaiTay@2019");
                System.out.println("Connected");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Khong tim thay Driver");
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("Da xay ra loi khi ket noi");
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
}
