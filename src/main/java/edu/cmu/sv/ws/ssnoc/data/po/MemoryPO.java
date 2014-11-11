package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save the wall posts yo
 * 
 */
public class MemoryPO {

    private String timestamp;
    private long usedVolatile;
    private long leftVolatile;
    private long usedNonVolatile;
    private long leftNonVolatile;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getUsedVolatile() {
        return usedVolatile;
    }

    public void setUsedVolatile(long usedVolatile) {
        this.usedVolatile = usedVolatile;
    }

    public long getLeftVolatile() {
        return leftVolatile;
    }

    public void setLeftVolatile(long leftVolatile) {
        this.leftVolatile = leftVolatile;
    }

    public long getUsedNonVolatile() {
        return usedNonVolatile;
    }

    public void setUsedNonVolatile(long usedNonVolatile) {
        this.usedNonVolatile = usedNonVolatile;
    }

    public long getLeftNonVolatile() {
        return leftNonVolatile;
    }

    public void setLeftNonVolatile(long leftNonVolatile) {
        this.leftNonVolatile = leftNonVolatile;
    }

    @Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
