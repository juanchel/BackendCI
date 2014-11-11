package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save the wall posts yo
 * 
 */
public class MessagePO {

    private String content;
    private String timestamp;
    private String author;
    private String target;
    private boolean isPublic;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {

        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getPublic() {
        return isPublic;
    }

    public void setPublic(boolean ifPublic) {
        this.isPublic = ifPublic;
    }

    @Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
