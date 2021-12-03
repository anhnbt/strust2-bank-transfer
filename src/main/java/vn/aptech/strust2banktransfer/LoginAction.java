/*
 * Copyright 2021 by AnhNBT
 */
package vn.aptech.strust2banktransfer;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
 */
public class LoginAction extends ActionSupport {
    private Integer id;
    private String username;
    private String password;
    private Double totalAmount;

    @Override
    public String execute() throws Exception {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String queryString = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(queryString);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login success");
                this.id = rs.getInt("id");
                this.totalAmount = rs.getDouble("totalAmount");
                return SUCCESS;
            } else {
                System.out.println("Wrong username and password");
                addActionError("Wrong username and password");
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    @Override
    public void validate() {
        if (this.getUsername() == null || getUsername().equals("")) {
            addFieldError("username", "You must enter your username");
        } 
        if (this.getPassword() == null || this.getPassword().equals("")) {
            addFieldError("password", "You must enter your password");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}
