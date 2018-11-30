package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Event> mEvents;
    private String mParam2;

    private WeekView mOneDayView;

    private OnFragmentInteractionListener mListener;

    public DayViewFragment() {
        // Required empty public constructor
    }


    public static DayViewFragment newInstance() {
        return new DayViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvents = Database.getEvents();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_view, container, false);

        MonthLoader.MonthChangeListener monthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                ArrayList<WeekViewEvent> weekViewEvents =  new ArrayList<>();
                for (int i = 0; i < mEvents.size(); i++) {

                    Event currentEvent = mEvents.get(i);
                    long weekDayEventID = (long)i;

                    //This method is run for the previous, current, and next month.
                    // We only want to create WeekViewEvent objects on the current month
                    int eventStartDateMonth = currentEvent.retrieveDateInfo(currentEvent.getEventStartDay())[0];
                    if (eventStartDateMonth == newMonth - 1) {
                        WeekViewEvent weekViewEvent = currentEvent.getWeekViewEvent();
                        weekViewEvent.setColor(getResources().getColor(R.color.buzz_gold));
                        weekViewEvent.setId(weekDayEventID);
                        weekViewEvents.add(weekViewEvent);
                    }
                }
                return weekViewEvents;
            }
        };

        mOneDayView = view.findViewById(R.id.calendar_day_view);
        mOneDayView.setMonthChangeListener(monthChangeListener);
        mOneDayView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                int eventPos = (int)event.getId();
                Event tappedEvent = mEvents.get(eventPos);
                goToDetailedEventActivity(tappedEvent);
            }
        });
        return view;
    }

    private void goToDetailedEventActivity(Event event) {
        Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
