package edu.cmu.sv.ws.ssnoc.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/users")
public class UsersService extends BaseService {

    /**
     * This method is used to get the buddies of a user
     *
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{userName}/chatbuddies")
    public List<User> getBuddies(@PathParam("userName") String userName) {
        List<UserPO> budspo = null;
        List<User> buds = new ArrayList<User>();

        try {
            budspo = DAOFactory.getInstance().getMessageDAO().loadChatBuddies(userName);
            for (UserPO po : budspo) {
                User dto = ConverterUtils.convert(po);
                buds.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return buds;
    }

	/**
	 * This method loads all active users in the system.
	 * 
	 * @return - List of all active users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "users")
	public List<User> loadUsers() {
		Log.enter();

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();

			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}


    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/clusters/{hours}")
    public String getClusters (@PathParam("hours") String hours) {

        List<List<User>> clusters = new ArrayList<List<User>>();

        if (hours == "" || hours == null) {
            hours = "0";
        }

        Date date = new Date();
        date.setTime(date.getTime() - (Integer.valueOf(hours)*3600000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = Timestamp.valueOf(sdf.format(date));

        try {
            List<List<UserPO>> userPOLists = DAOFactory.getInstance().getExchangeDAO().getClusters(ts);


            for (List<UserPO> poList : userPOLists) {

                List<User> dtoList = new ArrayList<User>();
                for (UserPO po : poList) {

                    User dto = ConverterUtils.convert(po);
                    dtoList.add(dto);
                }
                clusters.add(dtoList);
            }

        } catch (Exception e) {
            handleException(e);
        }

        return clusters.toString();
    }
}
