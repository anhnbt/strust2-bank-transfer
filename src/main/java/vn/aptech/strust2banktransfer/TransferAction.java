/*
 * Copyright 2021 by AnhNBT
 */
package vn.aptech.strust2banktransfer;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
 */
public class TransferAction extends ActionSupport {

    // Người thụ hưởng
    private Integer accountIdReceiver;

    // Số tiền gửi
    private Double amount;
    private Double totalAmount;
    // Người gửi
    private Integer accountIdRequest;

    @Override
    public String execute() throws Exception {
        int[] result = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT totalAmount FROM users WHERE id = " + this.getAccountIdRequest());
            if (rs.next()) {
                double amountFromAccount = rs.getDouble("totalAmount");
                if (amountFromAccount > this.getAmount()) {
                    // Trừ tiền người gửi
                    PreparedStatement updateThis = conn.prepareStatement("UPDATE users SET totalAmount = totalAmount - ? WHERE id = ?");
                    conn.setAutoCommit(false);
                    
                    updateThis.setDouble(1, this.getAmount());
                    updateThis.setInt(2, this.getAccountIdRequest());
                    updateThis.executeUpdate();

                    // Cộng tiền người nhận
                    PreparedStatement updateThat = conn.prepareStatement("UPDATE users SET totalAmount = totalAmount + ? WHERE id = ?");
                    updateThat.setDouble(1, this.getAmount());
                    updateThat.setInt(2, this.getAccountIdReceiver());
                    updateThat.executeUpdate();

                    // Lưu thông tin giao dịch vào bảng transaction
                    PreparedStatement insert = conn.prepareStatement("INSERT INTO transaction (requestId, receiverId, amount, transactionType, transactionDate) VALUES (?, ?, ?, ?, ?)");
                    insert.setInt(1, this.getAccountIdRequest());
                    insert.setInt(2, this.getAccountIdReceiver());
                    insert.setDouble(3, this.getAmount());
                    insert.setString(4, "Chuyen tien");
                    insert.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                    insert.executeUpdate();
                    conn.commit();
                    return SUCCESS;
                } else {
                    // Số dư không đủ tiền
                    return "khongdutien";
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

}
