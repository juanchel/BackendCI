package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnauthorizedUserException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ValidationException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;
import org.h2.util.StringUtils;

import javax.crypto.SecretKey;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to admin.
 *
 */

@Path("/admin")
public class AdminService extends BaseService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/password/{userName}")
    public Response changePW(@PathParam("userName") String userName, User user) {

        UserPO po = new UserPO();
        po.setUserName(userName);
        po.setPassword(user.getPassword());
        po = SSNCipher.encryptPassword(po);

        DAOFactory.getInstance().getUserDAO().updatePW(po.getUserName(), po.getPassword(), po.getSalt());

        return ok();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/username/{userName}")
    public Response changeUsername(@PathParam("userName") String userName, User user) {

        UserPO po = new UserPO();
        po.setUserName(user.getUserName());

        DAOFactory.getInstance().getUserDAO().updateUsername(userName, po.getUserName());

        return ok();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/role/{userName}")
    public Response changeRole(@PathParam("userName") String userName, User user) {

        UserPO po = new UserPO();
        po.setUserName(userName);
        po.setRole(user.getRole());

        DAOFactory.getInstance().getUserDAO().updateRole(po.getUserName(), po.getRole());

        return ok();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/active/{userName}/{active}")
    public Response changeActive(@PathParam("userName") String userName, @PathParam("active") String active) {

        boolean a = false;
        if (active.contains("1")) {
            a = true;
        }

        DAOFactory.getInstance().getUserDAO().updateActive(userName, a);

        return ok();
    }
}
