package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.dto.TestResult;

import java.util.List;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User information in the system.
 *
 */
public interface IMemoryDAO {

    void save();

    void start();

    public List<MemoryPO> end();
}
