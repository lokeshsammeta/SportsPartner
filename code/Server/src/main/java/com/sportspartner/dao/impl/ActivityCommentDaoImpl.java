package com.sportspartner.dao.impl;

import com.sportspartner.dao.ActivityCommentDao;
import com.sportspartner.model.ActivityComment;
import com.sportspartner.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ActivityCommentDaoImpl implements ActivityCommentDao{

    @Override
    public List<ActivityComment> getAllActivityComments(String activityId) {
        Connection c = new ConnectionUtil().connectDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ActivityComment> activityComments = new ArrayList<ActivityComment>();
        try {
            stmt = c.prepareStatement("SELECT * FROM \"Comment_Activity\" WHERE \"activityId\" = ?;");
            stmt.setString(1, activityId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                activityId = rs.getString("activityId");
                String commentId = rs.getString("commentId");
                String authorId = rs.getString("authorId");
                Timestamp timestamp = rs.getTimestamp("time");
                Date time = new Date( timestamp.getTime());
                String content = rs.getString("content");
                activityComments.add(new ActivityComment(activityId,commentId,authorId,time,content));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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
        return activityComments;
    }

    @Override
    public boolean hasActivityComment(ActivityComment activityComment) {
        Connection c = new ConnectionUtil().connectDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean hasActivityComment = false;
        String activityId = activityComment.getActivityId();
        String commentId = activityComment.getCommentId();
        String authorId = activityComment.getAuthorId();
        Date time = activityComment.getTime();
        Timestamp timestamp = new Timestamp(time.getTime());
        String content = activityComment.getContent();

        try {
            stmt = c.prepareStatement("SELECT * FROM \"Comment_Activity\" WHERE \"activityId\" = ? AND \"commentId\" = ? AND \"authorId\" = ? AND \"time\" = ? AND \"content\" = ?;");
            stmt.setString(1, activityId);
            stmt.setString(2, commentId);
            stmt.setString(3, authorId);
            stmt.setTimestamp(4, timestamp);
            stmt.setString(5,content);

            rs = stmt.executeQuery();
            if (rs.next()) {
                hasActivityComment = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
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
        return hasActivityComment;
    }

    @Override
    public boolean newActivityComment(ActivityComment activityComment) {
        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String activityId = activityComment.getActivityId();
        String commentId = activityComment.getCommentId();
        String authorId = activityComment.getAuthorId();
        Date time = activityComment.getTime();
        Timestamp timestamp = new Timestamp(time.getTime());
        String content = activityComment.getContent();
        boolean result = false;
        try {
            stmt = c.prepareStatement("INSERT INTO \"Comment_Activity\" (\"activityId\", \"commentId\", \"authorId\", \"time\", \"content\")"+
                    "VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, activityId);
            stmt.setString(2, commentId);
            stmt.setString(3, authorId);
            stmt.setTimestamp(4, timestamp);
            stmt.setString(5,content);
            rs = stmt.executeUpdate();
            if(rs>0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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

    @Override
    public boolean updateActivityComment(ActivityComment activityComment) {
        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String activityId = activityComment.getActivityId();
        String commentId = activityComment.getCommentId();
        String authorId = activityComment.getAuthorId();
        Date time = activityComment.getTime();
        Timestamp timestamp = new Timestamp(time.getTime());
        String content = activityComment.getContent();

        boolean result = false;
        try {
            stmt = c.prepareStatement("UPDATE \"Activity_Member\" SET \"authorId\" = ? ,\"time\" = ?, \"content\" = ? WHERE \"activityId\"=? AND \"commentId\" = ?;");
            stmt.setString(1, authorId);
            stmt.setTimestamp(2, timestamp);
            stmt.setString(3,content);
            stmt.setString(4, activityId);
            stmt.setString(5, commentId);
            rs = stmt.executeUpdate();
            if(rs>0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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

    @Override
    public boolean deleteActivityComment(ActivityComment activityComment) {
        Connection c = new ConnectionUtil().connectDB();

        PreparedStatement stmt = null;
        int rs;
        String activityId = activityComment.getActivityId();
        String commentId = activityComment.getCommentId();
        String authorId = activityComment.getAuthorId();
        Date time = activityComment.getTime();
        Timestamp timestamp = new Timestamp(time.getTime());
        String content = activityComment.getContent();
        boolean result = false;

        try {
            stmt = c.prepareStatement("DELETE FROM \"Comment_Activity\" WHERE \"activityId\"=? AND \"commentId\" = ?  AND \"authorId\" = ?  AND \"time\" = ?  AND \"content\" = ?;");
            stmt.setString(1, activityId);
            stmt.setString(2, commentId);
            stmt.setString(3, authorId);
            stmt.setTimestamp(4, timestamp);
            stmt.setString(5,content);
            rs = stmt.executeUpdate();
            if(rs>0){
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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