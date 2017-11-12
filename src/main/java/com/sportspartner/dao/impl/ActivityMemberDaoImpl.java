package com.sportspartner.dao.impl;

import com.sportspartner.dao.ActivityMemberDao;
import com.sportspartner.model.ActivityMember;
import com.sportspartner.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityMemberDaoImpl implements ActivityMemberDao{
    /**
     * Get all members of an activity.
     * @param activityId
     * @return a list of ActivityMember objects
     */
    @Override
    public List<ActivityMember> getAllActivitymembers(String activityId) {
        Connection c = new ConnectionUtil().connectDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ActivityMember> activityMembers = new ArrayList<ActivityMember>();
        try {
            stmt = c.prepareStatement("SELECT * FROM \"Activity_Member\" WHERE \"activityId\" = ?;");
            stmt.setString(1, activityId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                activityId = rs.getString("activityId");
                String userId = rs.getString("userId");
                activityMembers.add(new ActivityMember(activityId,userId));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
        } finally {
            try {
                rs.close();
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return activityMembers;
    }

    /**
     * Check whether an activity has a member specified by userId.
     * @param activityMember
     * @return "true" or "false" for whether the activity has the member
     */
    @Override
    public boolean hasActivityMember(ActivityMember activityMember) {
        Connection c = new ConnectionUtil().connectDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean hasActivityMember = false;
        String activityId = activityMember.getActivityId();
        String userId = activityMember.getUserId();

        try {
            stmt = c.prepareStatement("SELECT * FROM \"Activity_Member\" WHERE \"activityId\" = ? AND \"userId\" = ?;");
            stmt.setString(1, activityId);
            stmt.setString(2, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                hasActivityMember = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //
        } finally {
            try {
                rs.close();
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return hasActivityMember;
    }

    /**
     * Add a new member to activity.
     * @param activityMember
     * @return "true" or "false" for whether successfully add a new activity member
     */
    @Override
    public boolean newActivityMember(ActivityMember activityMember) {
        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String activityId = activityMember.getActivityId();
        String userId = activityMember.getUserId();
        boolean result = false;

        try {
            stmt = c.prepareStatement("INSERT INTO \"Activity_Member\" (\"activityId\", \"userId\")"+
                    "VALUES (?, ?)");
            stmt.setString(1, activityId);
            stmt.setString(2, userId);
            rs = stmt.executeUpdate();
            if(rs>0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
        } finally {
            try {
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * Delete an activity member.
     * @param activityMember
     * @return "ture" or "false" for whether successfully delete an activity member.
     */
    @Override
    public boolean deleteActivityMember(ActivityMember activityMember) {
        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String activityId = activityMember.getActivityId();
        String userId = activityMember.getUserId();
        boolean result = false;

        try {
            stmt = c.prepareStatement("DELETE FROM \"Activity_Member\" WHERE \"activityId\"=? AND \"userId\" = ?;");
            stmt.setString(1, activityId);
            stmt.setString(2, userId);
            rs = stmt.executeUpdate();
            if(rs>0){
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
        } finally {
            try {
                stmt.close();
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
}
