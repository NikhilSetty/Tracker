package com.ve.tracker.tracker.Helper;

import com.ve.tracker.tracker.Models.LocationPointModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nravishankar on 5/20/2016.
 */
public class StaticHelper {
    static final public String UPDATE_UI = "com.ve.tracker.TrackerService.DataReceived";
    static final public String UPDATE_UI_MESSAGE = "com.ve.tracker.TrackerService.DataMessage";

    static final public String LATTITUBE_LOCATION_OBTAINED = "com.ve.tracker.trackerservice.lattitude";
    static final public String LONGITUDE_LOCATION_OBTAINED = "com.ve.tracker.trackerservice.longitude";

    public static boolean IsServiceRunning = false;

    public static List<LocationPointModel> pointsRecorded = new ArrayList<>();
}

