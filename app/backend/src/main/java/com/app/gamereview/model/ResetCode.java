package com.app.gamereview.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "resetCodes")
public class ResetCode {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    private String code;
    @Indexed(unique = true) // Ensures a unique constraint on userId field
    private String userId; // ID of the associated user
    private Date expirationDate;

    public ResetCode(String code, String userId, Date expirationDate) {

        this.code = code;
        this.userId = userId;
        this.expirationDate = expirationDate;
    }

}
