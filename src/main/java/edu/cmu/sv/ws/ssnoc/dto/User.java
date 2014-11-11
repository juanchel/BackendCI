package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

/**
 * This object contains user information that is responded as part of the REST
 * API request.
 * 
 */
public class User {
	private String userName;
	private String password;
    private int emergency_status;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private int role;

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

    public int getEmergency_status() { return emergency_status;}

    public void setEmergency_status(int emergency_status) { this.emergency_status = emergency_status ;};


	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
