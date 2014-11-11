package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/messages")
public class MessagesService extends BaseService {

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/wall")
    public List<Message> getWallPosts () {
        List<Message> messages = null;

        List<MessagePO> messagePOs = DAOFactory.getInstance().getMessageDAO().loadWallMessages();

        messages = new ArrayList<Message>();
        for (MessagePO po : messagePOs) {
            Message dto = ConverterUtils.convert(po);
            messages.add(dto);
        }
        return messages;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{author}/{target}")
    public List<Message> getPMs (@PathParam("author") String author, @PathParam("target") String target) {
        List<Message> messages = null;

        List<MessagePO> messagePOs = DAOFactory.getInstance().getMessageDAO().loadPrivateMessages(author, target);

        messages = new ArrayList<Message>();
        for (MessagePO po : messagePOs) {
            Message dto = ConverterUtils.convert(po);
            messages.add(dto);
        }
        return messages;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/announcements")
    public List<Message> getAnnouncements () {
        List<Message> messages = null;

        List<MessagePO> messagePOs = DAOFactory.getInstance().getMessageDAO().loadAnnouncement();

        messages = new ArrayList<Message>();
        for (MessagePO po : messagePOs) {
            Message dto = ConverterUtils.convert(po);
            messages.add(dto);
        }

        return messages;
    }



}
