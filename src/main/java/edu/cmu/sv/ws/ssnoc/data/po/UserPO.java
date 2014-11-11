package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save all user information in the system.
 * This contains information like the user's name, his role, his account status
 * and the password information entered by the user when signing up. <br/>
 * Information is saved in SSN_USERS table.
 * 
 */
public class UserPO {
	private long userId;
	private String userName;
	private String password;
	private String salt;
    private int emergency_status;
    private int role;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

    public int getEmergency_status() { return emergency_status;}

    public void setEmergency_status(int emergency_status) { this.emergency_status = emergency_status ;};

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
