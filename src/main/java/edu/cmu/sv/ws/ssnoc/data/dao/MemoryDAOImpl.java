package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * DAO implementation for saving User information in the H2 database.
 *
 */
public class MemoryDAOImpl extends BaseDAOImpl implements IMemoryDAO {
    public void save() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_MEMORY);) {
            Date date = new Date();
            date.setTime(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            File file = new File("/");

            stmt.setTimestamp(1, Timestamp.valueOf(sdf.format(date)));
            stmt.setLong(2, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
            stmt.setLong(3, Runtime.getRuntime().freeMemory() / 1024);
            stmt.setLong(4, (file.getTotalSpace() - file.getFreeSpace()) / 1024);
            stmt.setLong(5, file.getFreeSpace() / 1024);
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public void start() {

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.RESET_MEMORY);) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public List<MemoryPO> end() {

        List<MemoryPO> memoryPOs = new ArrayList<MemoryPO>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.GET_MEMORY_RESULTS);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MemoryPO mem = new MemoryPO();
                mem.setTimestamp(rs.getString(1).replace(".0" , ""));
                mem.setUsedVolatile(rs.getLong(2));
                mem.setLeftVolatile(rs.getLong(3));
                mem.setUsedNonVolatile(rs.getLong(4));
                mem.setLeftNonVolatile(rs.getLong(5));
                memoryPOs.add(mem);
            }
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.RESET_MEMORY);) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }

        return memoryPOs;
    }
}
