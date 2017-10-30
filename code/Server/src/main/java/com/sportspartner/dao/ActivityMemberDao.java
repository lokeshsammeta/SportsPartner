package com.sportspartner.dao;

import com.sportspartner.model.ActivityMember;

import java.util.List;

public interface ActivityMemberDao {
    public List<ActivityMember> getAllActivitymembers(String activityId);
    public boolean hasActivityMember(ActivityMember activityMember);
    public boolean newActivityMember(ActivityMember activityMember);
    public boolean updateActivityMember(ActivityMember activityMember, String newUserId);
    public boolean deleteActivityMember(ActivityMember activityMember);
}
