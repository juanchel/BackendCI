package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to admin.
 *
 */

@Path("/search")
public class SearchService extends BaseService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/username")
    public List<User> searchUser(Message message) {
        List<UserPO> userPOs = null;
        List<User> users = new ArrayList<User>();

        try {
            userPOs = DAOFactory.getInstance().getUserDAO().searchUsername(message.getContent());
            for (UserPO po : userPOs) {
                User dto = ConverterUtils.convert(po);
                users.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }

        return users;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/status")
    public List<User> searchStatus(Message message) {
        List<UserPO> userPOs = null;
        List<User> users = new ArrayList<User>();

        try {
            userPOs = DAOFactory.getInstance().getUserDAO().searchStatus(Integer.valueOf(message.getContent()));
            for (UserPO po : userPOs) {
                User dto = ConverterUtils.convert(po);
                users.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }

        return users;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/wall")
    public List<Message> searchWall(Message message) {
        List<MessagePO> messagePOs = null;
        List<Message> messages = new ArrayList<Message>();

        try {
            messagePOs = DAOFactory.getInstance().getMessageDAO().searchWall(message.getContent());
            for (MessagePO po : messagePOs) {
                Message dto = ConverterUtils.convert(po);
                messages.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }

        return messages;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/announcements")
    public List<Message> searchAnnouncements(Message message) {
        List<MessagePO> messagePOs = null;
        List<Message> messages = new ArrayList<Message>();

        try {
            messagePOs = DAOFactory.getInstance().getMessageDAO().searchAnnouncements(message.getContent());
            for (MessagePO po : messagePOs) {
                Message dto = ConverterUtils.convert(po);
                messages.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }

        return messages;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/private/{userName}")
    public List<Message> searchPM(@PathParam("userName") String username, Message message) {
        List<MessagePO> messagePOs = null;
        List<Message> messages = new ArrayList<Message>();

        try {
            messagePOs = DAOFactory.getInstance().getMessageDAO().searchPM(message.getContent(), username);
            for (MessagePO po : messagePOs) {
                Message dto = ConverterUtils.convert(po);
                messages.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }

        return messages;
    }
}
