package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IExchangeDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Path("/message")
public class MessageService extends BaseService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{userName}")
    public Response postOnWall (@PathParam("userName") String userName, Message m) {

        Message resp = new Message();
        m.setPublic(true);
        m.setAuthor(userName);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ts = sdf.format(date);

        m.setTimestamp(ts);

        IExchangeDAO dao = DAOFactory.getInstance().getExchangeDAO();
        MessagePO po = ConverterUtils.convert(m);
        dao.save(po);
        resp = ConverterUtils.convert(po);

        return created(resp);
    }


    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/announcement/{userName}/")
    public Response postAnnouncement (@PathParam("userName") String userName, Message m) {

        Message resp = new Message();
        m.setPublic(true);
        m.setAuthor(userName);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ts = sdf.format(date);

        m.setTimestamp(ts);

        IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
        MessagePO po = ConverterUtils.convert(m);
        dao.saveAnnouncement(po);
        resp = ConverterUtils.convert(po);

        return created(resp);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{author}/{target}")
    public Response sendPM (@PathParam("author") String author, @PathParam("target") String target, Message m) {

        Message resp = new Message();
        m.setPublic(false);
        m.setAuthor(author);
        m.setTarget(target);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ts = sdf.format(date);

        m.setTimestamp(ts);

        IExchangeDAO dao = DAOFactory.getInstance().getExchangeDAO();
        MessagePO po = ConverterUtils.convert(m);
        dao.save(po);
        resp = ConverterUtils.convert(po);

        return created(resp);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{messageId}")
    public String getPMs (@PathParam("messageId") String id) {
        String msg = null;
        msg = DAOFactory.getInstance().getMessageDAO().loadMessageById(Integer.parseInt(id));
        return msg;
    }
}
