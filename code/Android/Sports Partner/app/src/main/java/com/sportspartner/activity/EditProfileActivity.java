package com.sportspartner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.sportspartner.R;
import com.sportspartner.models.Profile;
import com.sportspartner.models.Sport;
import com.sportspartner.service.ProfileService;
import com.sportspartner.service.ResourceService;
import com.sportspartner.service.ModelResult;
import com.sportspartner.service.ActivityCallBack;
import com.sportspartner.util.chat.Connection;
import com.sportspartner.util.dbHelper.LoginDBHelper;
import com.sportspartner.util.PicassoImageLoader;
import com.sportspartner.util.adapter.Divider;
import com.sportspartner.util.adapter.EditInterestAdapter;
import com.sportspartner.util.adapter.InterestAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditProfileActivity extends BasicActivity implements NumberPicker.OnValueChangeListener {// {
    private final ArrayList<String> listGender= new ArrayList<String>(){{
        add("Female");
        add("Male");
        add("Other");
    }};
    private int genderPosition;

    //Image
    private ImagePicker imagePicker;

    private final int cropWidth = 280;
    private final int cropHeight = 280;
    private final int outputX = 800;
    private final int outputY = 800;

    private ArrayList<ImageItem> images = null;

    //profile
    private Profile profile = new Profile();
    private ArrayList<Sport> interests = new ArrayList<Sport>();
    private String interestsUpdated = "";
    private String userEmail;
    private ArrayList<Sport> allSports = new ArrayList<Sport>();
    private ArrayList<Sport> interestSportUpdate = new ArrayList<Sport>();

    //Adapter
    InterestAdapter interestAdapter;
    EditInterestAdapter editInterestAdapter;

    //widget
    private ImageView photoView;
    private EditText userName;
    private EditText gender;
    //private Spinner gender;
    private EditText age;
    private EditText city;
    private RecyclerView interestRecyclerView;
    private LinearLayout LinearRecycler;

    /**
     * Load the EditProfileActivity
     * Find Widgets by Id
     * Set onClick listener
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.layout_home);
        getLayoutInflater().inflate(R.layout.activity_edit_profile, content, true);

        //set title of toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");

        final Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra("profile");
        interests = new ArrayList<Sport>((ArrayList<Sport>) intent.getSerializableExtra("interest"));

        userEmail = LoginDBHelper.getInstance(this).getEmail();

        //find all widgets by id
        photoView = (ImageView) findViewById(R.id.profile_photo);
        userName = (EditText) findViewById(R.id.profile_name);
        gender = (EditText) findViewById(R.id.edit_gender);
        //gender = (Spinner) findViewById(R.id.spinner_gender);
        age = (EditText) findViewById(R.id.edit_age);
        city = (EditText) findViewById(R.id.text_city);
        interestRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        LinearRecycler = (LinearLayout) findViewById(R.id.ListView_Recycler);

        //set all the contents
        if (profile.getAge() == 0){
            age.setText("");
        }
        else {
            age.setText(Integer.toString(profile.getAge()));
        }
        gender.setText(profile.getGender());
        city.setText(profile.getAddress());
        userName.setText(profile.getUserName());

        //set gender onCilck listener
        gender.setOnClickListener(myGenderListener);

        //set profile image
        ResourceService.getImage(this, profile.getIconUUID(), ResourceService.IMAGE_SMALL,
                new ActivityCallBack<Bitmap>() {
                    @Override
                    public void getModelOnSuccess(ModelResult<Bitmap> modelResult) {
                        if (modelResult.isStatus()) {
                            photoView.setImageBitmap(modelResult.getModel());
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Profile photo load error: " + modelResult.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("EditProfAct", modelResult.getMessage());
                        }
                    }
                });

        //set interestAdapter
        interestAdapter = new InterestAdapter(interests,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        interestRecyclerView.setLayoutManager(mLayoutManager);
        interestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //interestRecyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.HORIZONTAL));
        interestRecyclerView.setAdapter(interestAdapter);

        /**
         * set onclick listener for Interest
         * * get all  sports
         * * show the interests select dialog
         */
        LinearRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInterestClick();
            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.onPhotoClick();
            }
        });
    }

    /**
     * Show the NumberPicker Dialog
     * Set the content of the textView according to selection result of the user
     * @param strings    The String shows in the NumberPicker
     * @param textView  The textView which should be changed
     */
    //all types of listener
    private void showDialog(final String[] strings, final TextView textView){
        final Dialog d = new Dialog(EditProfileActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.layout_dialog);
        final Button b1 = (Button) d.findViewById(R.id.dialog_button_set);
        Button b2 = (Button) d.findViewById(R.id.dialog_button_cancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.dialog_numPicker);
        np.setDisplayedValues(strings);
        np.setMinValue(0);
        np.setMaxValue(strings.length - 1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                textView.setText(np.getDisplayedValues()[np.getValue()]);
                genderPosition = np.getValue()%listGender.size();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    /**
     * myGenderListener:
     * An object of OnClickListener,
     * Set the content of the string, which will be shown in the Dialog
     * Show a dialog
     */
    private View.OnClickListener myGenderListener = new View.OnClickListener() {
        public void onClick(View v) {
            String[] sports = new String[listGender.size()];
            for (int i = 0; i < listGender.size(); i++){
                sports[i] = listGender.get(i);
            }
            showDialog(sports, gender);
        }
    };


    /**
     * show a choose sport dialog when the interest row is clicked
     */
    private void onInterestClick(){
        // get all sports
        ResourceService.getAllSports(EditProfileActivity.this, new ActivityCallBack<ArrayList<Sport>>() {
            @Override
            public void getModelOnSuccess(ModelResult<ArrayList<Sport>> result) {
                // handle the result of request here
                String message = result.getMessage();
                Boolean status = result.isStatus();

                if (status){
                    //if successfully get Activity, get the data
                    //setSelected(true) to all my interests
                    allSports = new ArrayList<>(result.getModel());
                    Log.d("get allSports", String.valueOf(allSports.size()));

                    HashMap<String, Sport> mapAllSports = new HashMap<>();
                    for (Sport sport : allSports){
                        mapAllSports.put(sport.getSportId(),sport);
                    }

                    //set all myInterest to isSelected
                    for (Sport sport : interests){
                        sport.setSelected(true);
                        mapAllSports.put(sport.getSportId(), sport);
                    }

                    //get new allSports array
                    allSports = new ArrayList<>();
                    Iterator mapIterator = mapAllSports.entrySet().iterator();
                    while (mapIterator.hasNext()) {
                        Map.Entry pair = (Map.Entry)mapIterator.next();
                        allSports.add((Sport) pair.getValue());
                    }

                    EditProfileActivity.this.showDialog();
                }
                else {
                    //if failure, show a toast
                    Toast.makeText(EditProfileActivity.this, "Load sports Error: " + message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * show another Dialog with recyclerView for user to multiChose the sports
     */
    private void showDialog(){
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        //set content
        dialog.setTitle("NumberPicker");
        dialog.setContentView(R.layout.layout_dialog_edit_interest);

        //find widgets
        final Button save = (Button) dialog.findViewById(R.id.save);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.RecyclerView);

        //set RecyclerView adapter
        Log.d("adapter allSports", String.valueOf(allSports.size()));
        editInterestAdapter = new EditInterestAdapter(allSports, this);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new Divider(this, LinearLayoutManager.HORIZONTAL));
        recycler.setAdapter(editInterestAdapter);

        //Set ClickListener
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
            //update the interestUpdated object according to the multi choose
                for (Sport sport : allSports){
                    if (sport.getSelected()){
                        interestsUpdated += sport.getSportId() + ",";
                        interestSportUpdate.add(sport);
                    }
                }

                interestsUpdated = interestsUpdated.substring(0, interestsUpdated.length() - 1);
                Log.d("interestsUpdated",interestsUpdated);

                //Update interests
                ProfileService.updateInterests(EditProfileActivity.this,profile.getUserId(),interestsUpdated, new ActivityCallBack(){
                    @Override
                    public void getModelOnSuccess(ModelResult booleanResult) {
                        String message = booleanResult.getMessage();
                        if (booleanResult.isStatus()) {
                            Toast toast = Toast.makeText(EditProfileActivity.this, "Update Interests Success!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else{
                            Toast toast = Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                interestAdapter.updateInterests(interestSportUpdate);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * go to ImageGridActivity when the user click his profile photo
     */
    public void onPhotoClick(){

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());
        imagePicker.setImageLoader(new PicassoImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setSelectLimit(1);

        // square image
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = cropWidth;
        Integer height = cropHeight;
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);

        // set output size
        imagePicker.setOutPutX(outputX);
        imagePicker.setOutPutY(outputY);

        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(com.lzy.imagepicker.ui.ImageGridActivity.EXTRAS_IMAGES, new ArrayList< ImageItem >());
        startActivityForResult(intent, 100);

    }

    /**
     * upload bitmap when back to this activity from ImageGridActivity activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images.size()>0) {
                    String imagePath = images.get(0).path;
                    File imgFile = new File(imagePath);
                    Log.d("imageTest", "imagePath: "+imagePath);
                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        updateIcon(myBitmap);

                    }
                }
            } else {
                Toast.makeText(this, "No data sent back", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Update user profile photo
     * @param bitmap The bitmap of the new photo
     */
    private void updateIcon(Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);
        ResourceService.uploadUserIcon(this, bitmap, new ActivityCallBack<String>(){
            public void getModelOnSuccess(ModelResult<String> result){
                if (!result.isStatus()){
                    Toast.makeText(EditProfileActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                profile.setIconUUID(result.getModel());
            }
        });
    }

    /**
     * Set the visibility of the Edit button on the toolbar to visible
     * set the icon to save
     * @param menu The menu on the top right of the toolbar
     * @return True if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //change the visibility of toolbar edit button
        MenuItem editItem = menu.getItem(0);
        editItem.setVisible(true);
        editItem.setIcon(getResources().getDrawable(R.drawable.save));
        return true;
    }

    /**
     * Set the onClick action to the save button--update profile
     * back to the profile activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.toolbar_edit:
                updateProfile();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void updateProfile() {
        //set profile
        profile.setUserName(userName.getText().toString());
        profile.setGender(gender.getText().toString());
        profile.setAge(Integer.parseInt(age.getText().toString()));
        profile.setAddress(city.getText().toString());

        //update
        ProfileService.updateProfile(this, userEmail, profile, new ActivityCallBack(){
            public void getModelOnSuccess(ModelResult result) {
                updateProfileHandler(result);
            }
        });
    }

    /**
     * handle the result of the profile info from the server
     * @param result If update successfully, back to profile activity.
     */
    private void updateProfileHandler(ModelResult result) {
        // handle the result here
        String message = result.getMessage();
        if (result.isStatus()) {
            Connection.updateSendBird(this,userEmail);
            Toast toast = Toast.makeText(EditProfileActivity.this, "Update Success!", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userId", userEmail);
            this.startActivity(intent);
            finish();
        }
        else{
            Toast toast = Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * When user push the hardware back button on the photo
     * Call finish()
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
