package com.sportspartner.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.sportspartner.models.SActivityOutline;
import com.sportspartner.R;
import com.sportspartner.models.Profile;
import com.sportspartner.service.ActivityService;
import com.sportspartner.service.ProfileService;
import com.sportspartner.service.serviceresult.ModelResult;
import com.sportspartner.util.ActivityCallBack;
import com.sportspartner.util.LoginDBHelper;
import com.sportspartner.util.listviewadapter.MyActivityAdapter;
import java.util.ArrayList;

public class ProfileActivity extends BasicActivity {
    //userEmail
    private String usermail;

    //the main view of this activity
    RefreshLayout refreshLayout;

    private Profile profile = new Profile();

    // ListView adapters
    private MyActivityAdapter historyListAdapter;
    private MyActivityAdapter upcommingListAdapter;

    //Android widgets in the profile xml
    private View history;
    private ImageView profilePhoto;
    private TextView profileName;
    private TextView gender;
    private TextView age;
    private TextView location;
    private TextView interest;
    private ListView historyActivityList;
    private ListView upcommingActivityList;
    private RatingBar puntuality;
    private RatingBar participation;
    private TextView titleBasicInfo;
    private TextView titleComment;
    private TextView titleUpcommingActivity;
    private TextView titleHistoryActivity;

    //reshresh helper data
    private int upcommingCount = 0; // the count of loaded upcomming activities
    private int historyCount = 0; // the count of loaded history activities
    private boolean upcommingFinished = false; // no more upcomming activity to load
    private boolean historyFinished = false; // no more history activity to load
    private final int REFRESH_LIMIT = 3;

    /**
     * OnCreate method for this Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content1 = (ViewGroup) findViewById(R.id.layout_home);
        getLayoutInflater().inflate(R.layout.activity_profile, content1, true);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        //set title of toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");

        // get the userEmail from SQLite
        LoginDBHelper dbHelper = LoginDBHelper.getInstance(ProfileActivity.this);
        usermail= dbHelper.getEmail();

        //find all the widgets by Id
        View basicInfo = findViewById(R.id.personal_info);
        profilePhoto = (ImageView) basicInfo.findViewById(R.id.profile_photo);
        profileName = (TextView) basicInfo.findViewById(R.id.profile_name);
        gender = (TextView) basicInfo.findViewById(R.id.gender);
        age = (TextView) basicInfo.findViewById(R.id.age);
        location = (TextView) basicInfo.findViewById(R.id.location);
        interest = (TextView) basicInfo.findViewById(R.id.interests);

        puntuality = (RatingBar) findViewById(R.id.rating_punctuality);
        participation = (RatingBar) findViewById(R.id.rating_participation);

        historyActivityList = (ListView) findViewById(R.id.list_history_activities);
        upcommingActivityList = (ListView) findViewById(R.id.list_upcomming_activties);

        //find all the titles by Id
        View basic = findViewById(R.id.title_basic_info);
        View comment = findViewById(R.id.title_others_say);
        View upcomming = findViewById(R.id.title_upcomming_activty);
        history = findViewById(R.id.title_history_activty);
        titleBasicInfo = (TextView) basic.findViewById(R.id.title);
        titleComment = (TextView) comment.findViewById(R.id.title);
        titleUpcommingActivity = (TextView) upcomming.findViewById(R.id.title);
        titleHistoryActivity = (TextView) history.findViewById(R.id.title);

        //set all the content
        setTitle();
        setProfileInfo();
        setUpcommingActivity();
        setHistoryActivity();
        setRefresh();
        refresh();
        refresh();
    }

    /**
     * Set the text of textView of some titles
     */
    private void setTitle(){
        titleBasicInfo.setText("Basic Info");
        titleComment.setText("Comments");
        titleUpcommingActivity.setText("Upcomming Activity");
        titleHistoryActivity.setText("History Activity");
        history.setVisibility(View.INVISIBLE);
    }

    /**
     * Set the profileInfo:
     * Sent the request
     * Call the ProfileInfoHandler if success
     */
    private void setProfileInfo(){
        //get basic info
        ProfileService.getProfileInfo(this, usermail, new ActivityCallBack<Profile>(){
            @Override
            public void getModelOnSuccess(ModelResult<Profile> userProfileResult){
                ProfileInfoHandler(userProfileResult);
            }
        });
    }

    /**
     * Handle the result of response from the server
     * Fill the corresponding content of the UI
     * @param userProfileResult The result from the server
     */
    private void ProfileInfoHandler(ModelResult<Profile> userProfileResult) {
        // handle the result of request here
        String message = userProfileResult.getMessage();
        Boolean status = userProfileResult.isStatus();

        if (status){
            //if successfully get the data, then get the data
            profile = userProfileResult.getModel();
        }
        else{
            //if failure, show a toast
            Toast toast = Toast.makeText(ProfileActivity.this, "Load ProfileInfo Error: " + message, Toast.LENGTH_LONG);
            toast.show();
        }

        //set the BasicInfo
        //TODO get photo
        //profile.getIcon();
        //profilePhoto.setBackground();

        profileName.setText(profile.getUserName());
        age.setText(profile.getAge());
        gender.setText(profile.getGender());
        location.setText(profile.getAddress());

        // TODO get interest from service
        puntuality.setRating((float)profile.getPunctuality());
        participation.setRating((float)profile.getParticipation());
    }

    /**
     * Set the list content of UpcomingActivity
     */
    private void setUpcommingActivity() {
        upcommingListAdapter = new MyActivityAdapter(this, new ArrayList<SActivityOutline>());
        upcommingActivityList.setAdapter(upcommingListAdapter);
    }

    /**
     * Set the list content of HistoryActivity
     */
    private void setHistoryActivity(){
        historyListAdapter = new MyActivityAdapter(this, new ArrayList<SActivityOutline>());
        historyActivityList.setAdapter(historyListAdapter);
    }

    /**
     * Handle the result from the ActivityService
     * Fill the content of list of UpcommingActivities
     * @param moreActivitiesResult The result from the ActivityService
     */
    private void loadUpcommingActivitiesHandler(ModelResult<ArrayList<SActivityOutline>> moreActivitiesResult) {
        // handle the result of request here
        String message = moreActivitiesResult.getMessage();
        Boolean status = moreActivitiesResult.isStatus();

        if (status){
            //if successfully get Activities, get the data
            ArrayList<SActivityOutline> moreSAs = moreActivitiesResult.getModel();
            int size = moreSAs.size();
            upcommingCount += size;
            if (size < REFRESH_LIMIT){
                upcommingFinished = true;
            }
            if (size > 0) {
                upcommingListAdapter.appendList(moreSAs);
                upcommingListAdapter.notifyDataSetChanged();
                //set the height of the listview
                MyActivityAdapter.setListViewHeightBasedOnChildren(upcommingActivityList);
            }
        } else {
            //if failure, show a toast
            Toast toast = Toast.makeText(ProfileActivity.this, "Load more activities Error: " + message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Handle the result from the ActivityService
     * Fill the content of list of HistoryActivities
     * @param moreActivitiesResult The result from the ActivityService
     */
    private void loadHistoryActivitiesHandler(ModelResult<ArrayList<SActivityOutline>> moreActivitiesResult) {
        // handle the result of request here
        String message = moreActivitiesResult.getMessage();
        Boolean status = moreActivitiesResult.isStatus();

        if (status){
            //if successfully get Activities, get the data
            ArrayList<SActivityOutline> moreSAs = moreActivitiesResult.getModel();
            int size = moreSAs.size();
            historyCount += size;
            if (size < REFRESH_LIMIT){
                historyFinished = true;
            }
            if (size > 0) {
                historyListAdapter.appendList(moreSAs);
                historyListAdapter.notifyDataSetChanged();
                //set the height of the listview
                MyActivityAdapter.setListViewHeightBasedOnChildren(historyActivityList);
            }
        } else {
            //if failure, show a toast
            Toast toast = Toast.makeText(ProfileActivity.this, "Load more activities Error: " + message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //TODO set listview onclick lisener
    /*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
            }

        });
        */

    /**
     * Refresh: get the content of the list
     * Sent the corresponding request to the server
     * If not reach the end of the Upcoming Activity, get the content of Upcoming Activity
     * Else, get the content of Recommend Activity
     */
    private void refresh(){
        if (!upcommingFinished) {
            //get upcomming activities
            ActivityService.getUpcomingActivities(this, usermail, REFRESH_LIMIT, upcommingCount,
                    new ActivityCallBack<ArrayList<SActivityOutline>>(){
                        @Override
                        public void getModelOnSuccess(ModelResult<ArrayList<SActivityOutline>> result){
                            loadUpcommingActivitiesHandler(result);
                        }
                    });
        } else if (!historyFinished){
            history.setVisibility(View.VISIBLE);
            ActivityService.getHistoryActivities(this, usermail, REFRESH_LIMIT, historyCount,
                    new ActivityCallBack<ArrayList<SActivityOutline>>(){
                        @Override
                        public void getModelOnSuccess(ModelResult<ArrayList<SActivityOutline>> result){
                            loadHistoryActivitiesHandler(result);
                        }
                    });
        } else {
            Toast toast = Toast.makeText(ProfileActivity.this, "no more activities to load", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Assign the setOnLoadmoreListener to the current Layout
     * Set the Animate for the refresh
     * In the Listener, call refresh() function
     */
    private void setRefresh(){
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setAnimatingColor(getResources().getColor(R.color.background_blue)));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refresh();
                refreshlayout.finishLoadmore(100);

            }
        });
    }

    /**
     * Set the visibility of the Edit button on the toolbar to visible
     * @param menu The menu on the top right of the toolbar
     * @return True if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //change the visibility of toolbar edit button
        MenuItem editItem = menu.getItem(0);
        editItem.setVisible(true);
        return true;
    }

}


