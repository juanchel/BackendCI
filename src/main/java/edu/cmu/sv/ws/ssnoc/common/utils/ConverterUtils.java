package edu.cmu.sv.ws.ssnoc.common.utils;

import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

/**
 * This is a utility class used to convert PO (Persistent Objects) and View
 * Objects into DTO (Data Transfer Objects) objects, and vice versa. <br/>
 * Rather than having the conversion code in all classes in the rest package,
 * they are maintained here for code re-usability and modularity.
 * 
 */
public class ConverterUtils {
	/**
	 * Convert UserPO to User DTO object.
	 * 
	 * @param po
	 *            - User PO object
	 * 
	 * @return - User DTO Object
	 */
	public static final User convert(UserPO po) {
		if (po == null) {
			return null;
		}

		User dto = new User();
		dto.setUserName(po.getUserName());
        dto.setEmergency_status(po.getEmergency_status());
        dto.setRole(po.getRole());
        dto.setActive(po.isActive());
		return dto;
	}

	/**
	 * Convert User DTO to UserPO object
	 * 
	 * @param dto
	 *            - User DTO object
	 * 
	 * @return - UserPO object
	 */
	public static final UserPO convert(User dto) {
		if (dto == null) {
			return null;
		}

		UserPO po = new UserPO();
		po.setUserName(dto.getUserName());
		po.setPassword(dto.getPassword());
        po.setEmergency_status(dto.getEmergency_status());
        po.setRole(dto.getRole());
        po.setActive(dto.isActive());

		return po;
	}

    public static final Message convert(MessagePO po) {
        Message dto = new Message();
        dto.setAuthor(po.getAuthor());
        dto.setContent(po.getContent());
        dto.setTarget(po.getTarget());
        dto.setTimestamp(po.getTimestamp());
        dto.setPublic(po.getPublic());
        return dto;
    }

    public static final MessagePO convert(Message dto) {
        MessagePO po = new MessagePO();
        po.setAuthor(dto.getAuthor());
        po.setContent(dto.getContent());
        po.setTarget(dto.getTarget());
        po.setTimestamp(dto.getTimestamp());
        po.setPublic(dto.getPublic());
        return po;
    }
}
