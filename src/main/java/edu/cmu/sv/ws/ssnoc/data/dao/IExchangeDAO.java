package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.TestResult;

import java.sql.Timestamp;
import java.util.List;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User information in the system.
 *
 */
public interface IExchangeDAO {
    boolean save(MessagePO messagePO);

    List<MessagePO> loadWallMessages();

    List<MessagePO> loadPrivateMessages(String author, String target);

    List<List<UserPO>> getClusters(Timestamp timestamp);

}
