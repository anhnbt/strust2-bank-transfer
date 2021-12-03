/*
 * Copyright 2021 by AnhNBT
 */
package vn.aptech.strust2banktransfer;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
 */
public class LoginAction extends ActionSupport implements Preparable {
    private Integer accountIdRequest;
    private String username;
    private String password;
    private Double totalAmount;
    private Map<Integer, String> accounts;

    @Override
    public String execute() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                String queryString = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(queryString);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    HttpServletRequest request = ServletActionContext.getRequest();
                    HttpSession session = request.getSession();

                    // Lưu trữ username vào trong session
                    session.setAttribute("loginedUsername", this.username);
                    this.accountIdRequest = rs.getInt("id");
                    this.totalAmount = rs.getDouble("totalAmount");
                    return SUCCESS;
                } else {
                    addActionError(getText("error.login"));
                    return ERROR;
                }
            }
        } catch (Exception e) {
            // to do
            System.out.println(e.getMessage());
        }
        return ERROR;
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

    public Integer getAccountIdRequest() {
        return accountIdRequest;
    }

    public void setAccountIdRequest(Integer accountIdRequest) {
        this.accountIdRequest = accountIdRequest;
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

    public Map<Integer, String> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<Integer, String> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void prepare() throws Exception {
        accounts = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession session = request.getSession();
            PreparedStatement pstmt = null;
            if (session.getAttribute("loginedUsername") != null) {
                // Bỏ qua user đang đăng nhập
                pstmt = conn.prepareStatement("SELECT * FROM users WHERE username <> ? ORDER BY id DESC");
                pstmt.setString(1, (String) session.getAttribute("loginedUsername"));
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM users ORDER BY id DESC");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                this.accounts.put(rs.getInt("id"), rs.getString("username"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
