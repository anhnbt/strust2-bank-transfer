/*
 * Copyright 2021 by AnhNBT
 */
package vn.aptech.strust2banktransfer;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
 */
public class TransferAction extends ActionSupport implements Preparable {

    // Người thụ hưởng
    private Integer accountIdReceiver;

    // Số tiền gửi
    private Double amount;
    private Double totalAmount;
    // Người gửi
    private Integer accountIdRequest;
    private Map<Integer, String> accounts = new HashMap<>();

    @Override
    public String execute() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + this.getAccountIdRequest());
            if (rs.next()) {
                this.accountIdRequest = rs.getInt("id");
                this.totalAmount = rs.getDouble("totalAmount");
                if (this.totalAmount > this.getAmount()) {
                    // Trừ tiền người gửi
                    PreparedStatement updateThis = conn.prepareStatement("UPDATE users SET totalAmount = totalAmount - ? WHERE id = ?");
                    conn.setAutoCommit(false);
                    
                    updateThis.setDouble(1, this.getAmount());
                    updateThis.setInt(2, this.getAccountIdRequest());
                    updateThis.executeUpdate();

                    // Cộng tiền người nhận
                    Integer receiverId = (Integer) this.getAccountIdReceiver();
                    PreparedStatement updateThat = conn.prepareStatement("UPDATE users SET totalAmount = totalAmount + ? WHERE id = ?");
                    updateThat.setDouble(1, this.getAmount());
                    updateThat.setInt(2, receiverId);
                    updateThat.executeUpdate();

                    // Lưu thông tin giao dịch vào bảng transaction
                    PreparedStatement insert = conn.prepareStatement("INSERT INTO transaction (requestId, receiverId, amount, transactionType, transactionDate) VALUES (?, ?, ?, ?, ?)");
                    insert.setInt(1, this.getAccountIdRequest());
                    insert.setInt(2, receiverId);
                    insert.setDouble(3, this.getAmount());
                    insert.setString(4, "Chuyen tien");
                    insert.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                    insert.executeUpdate();
                    conn.commit();
                    return SUCCESS;
                } else {
                    addActionError("Số dư tài khoản không đủ! Số tiền gửi phải < " + this.totalAmount);
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ERROR;
    }

    @Override
    public void validate() {
        if (this.getAccountIdReceiver() == null || this.getAccountIdReceiver() == -1) {
            addFieldError("accountIdReceiver", "Thông tin người hưởng không được bỏ trống");
        }
        if (this.getAmount() == null) {
            addFieldError("amount", "Số tiền không được bỏ trống");
        }
    }
    
    public Integer getAccountIdReceiver() {
        return accountIdReceiver;
    }

    public void setAccountIdReceiver(Integer accountIdReceiver) {
        this.accountIdReceiver = accountIdReceiver;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAccountIdRequest() {
        return accountIdRequest;
    }

    public void setAccountIdRequest(Integer accountIdRequest) {
        this.accountIdRequest = accountIdRequest;
    }

    public Map<Integer, String> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<Integer, String> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void prepare() throws Exception {
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
