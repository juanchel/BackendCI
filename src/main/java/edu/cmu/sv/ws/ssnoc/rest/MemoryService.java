package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/memory")
public class MemoryService extends BaseService{

    private static boolean running = false;

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/start")
    public Response start () {
        try {
            DAOFactory.getInstance().getMemoryDAO().start();
        } catch (Exception e) {
            handleException(e);
        }

        if (!running)
            new MemoryThread().start();

        return ok();
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/end")
    public List<MemoryPO> endTest () {

        List<MemoryPO> memory = null;
        try {
            memory = DAOFactory.getInstance().getMemoryDAO().end();
        } catch (Exception e) {
            handleException(e);
        }

        running = false;

        return memory;
    }

    private class MemoryThread extends Thread {

        public void run() {
            running = true;
            try {
                for (int i = 0; i<60; i++) {
                    Thread.sleep(60000);
                    DAOFactory.getInstance().getMemoryDAO().save();
                }
            } catch (Exception e) {
                handleException(e);
            }
            running = false;
        }
    }

}
