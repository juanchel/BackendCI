package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

/**
 * This object contains user information that is responded as part of the REST
 * API request.
 * 
 */
public class TestResult {
	private float post;

    public float getGet() {
        return get;
    }

    public void setGet(float get) {
        this.get = get;
    }

    public float getPost() {
        return post;
    }

    public void setPost(float post) {
        this.post = post;
    }

    private float get;

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
