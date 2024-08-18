package com.roomorama.caldroid;//package com.roomorama.caldroid;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.text.format.DateUtils;
//import android.text.format.Time;
//import android.view.ContextThemeWrapper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.TextView;
//import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
//import com.antonyt.infiniteviewpager.InfiniteViewPager;
//import hirondelle.date4j.DateTime;
//import java.lang.reflect.Field;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Formatter;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//import java.util.TimeZone;
//
//// Referenced classes of package com.roomorama.caldroid:
////            CaldroidGridAdapter, MonthPagerAdapter, DateGridFragment, CalendarHelper,
////            WeekdayArrayAdapter, CaldroidListener, SquareTextView, CellView
//
//public class CaldroidFragmentO extends DialogFragment
//{
//    public class DatePageChangeListener
//        implements android.support.v4.view.ViewPager.OnPageChangeListener
//    {
//
//        private ArrayList caldroidGridAdapters;
//        private DateTime currentDateTime;
//        private int currentPage;
//        final CaldroidFragment this$0;
//
//        private int getNext(int i)
//        {
//            return (i + 1) % 4;
//        }
//
//        private int getPrevious(int i)
//        {
//            return (i + 3) % 4;
//        }
//
//        public ArrayList getCaldroidGridAdapters()
//        {
//            return caldroidGridAdapters;
//        }
//
//        public int getCurrent(int i)
//        {
//            return i % 4;
//        }
//
//        public DateTime getCurrentDateTime()
//        {
//            return currentDateTime;
//        }
//
//        public int getCurrentPage()
//        {
//            return currentPage;
//        }
//
//        public void onPageScrollStateChanged(int i)
//        {
//        }
//
//        public void onPageScrolled(int i, float f, int j)
//        {
//        }
//
//        public void onPageSelected(int i)
//        {
//            refreshAdapters(i);
//            setCalendarDateTime(currentDateTime);
//            CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter)caldroidGridAdapters.get(i % 4);
//            dateInMonthsList.clear();
//            dateInMonthsList.addAll(caldroidgridadapter.getDatetimeList());
//        }
//
//        public void refreshAdapters(int i)
//        {
//            CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter)caldroidGridAdapters.get(getCurrent(i));
//            CaldroidGridAdapter caldroidgridadapter1 = (CaldroidGridAdapter)caldroidGridAdapters.get(getPrevious(i));
//            CaldroidGridAdapter caldroidgridadapter2 = (CaldroidGridAdapter)caldroidGridAdapters.get(getNext(i));
//            if (i == currentPage)
//            {
//                caldroidgridadapter.setAdapterDateTime(currentDateTime);
//                caldroidgridadapter.notifyDataSetChanged();
//                caldroidgridadapter1.setAdapterDateTime(currentDateTime.minus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay));
//                caldroidgridadapter1.notifyDataSetChanged();
//                caldroidgridadapter2.setAdapterDateTime(currentDateTime.plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay));
//                caldroidgridadapter2.notifyDataSetChanged();
//            } else
//            if (i > currentPage)
//            {
//                currentDateTime = currentDateTime.plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//                caldroidgridadapter2.setAdapterDateTime(currentDateTime.plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay));
//                caldroidgridadapter2.notifyDataSetChanged();
//            } else
//            {
//                currentDateTime = currentDateTime.minus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//                caldroidgridadapter1.setAdapterDateTime(currentDateTime.minus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay));
//                caldroidgridadapter1.notifyDataSetChanged();
//            }
//            currentPage = i;
//        }
//
//        public void setCaldroidGridAdapters(ArrayList arraylist)
//        {
//            caldroidGridAdapters = arraylist;
//        }
//
//        public void setCurrentDateTime(DateTime datetime)
//        {
//            currentDateTime = datetime;
//            setCalendarDateTime(currentDateTime);
//        }
//
//        public void setCurrentPage(int i)
//        {
//            currentPage = i;
//        }
//
//        public DatePageChangeListener()
//        {
//            this$0 = CaldroidFragment.this;
//            super();
//            currentPage = 1000;
//        }
//    }
//
//
//    public static final String DIALOG_TITLE = "dialogTitle";
//    public static final String DISABLE_DATES = "disableDates";
//    public static final String ENABLE_CLICK_ON_DISABLED_DATES = "enableClickOnDisabledDates";
//    public static final String ENABLE_SWIPE = "enableSwipe";
//    public static int FRIDAY = 0;
//    public static final String MAX_DATE = "maxDate";
//    public static final String MIN_DATE = "minDate";
//    public static int MONDAY = 0;
//    public static final String MONTH = "month";
//    private static final int MONTH_YEAR_FLAG = 52;
//    public static final int NUMBER_OF_PAGES = 4;
//    public static int SATURDAY = 0;
//    public static final String SELECTED_DATES = "selectedDates";
//    public static final String SHOW_NAVIGATION_ARROWS = "showNavigationArrows";
//    public static final String SIX_WEEKS_IN_CALENDAR = "sixWeeksInCalendar";
//    public static final String SQUARE_TEXT_VIEW_CELL = "squareTextViewCell";
//    public static final String START_DAY_OF_WEEK = "startDayOfWeek";
//    public static int SUNDAY = 0;
//    public static final String THEME_RESOURCE = "themeResource";
//    public static int THURSDAY = 0;
//    public static int TUESDAY = 0;
//    public static int WEDNESDAY = 0;
//    public static final String YEAR = "year";
//    public static final String _BACKGROUND_FOR_DATETIME_MAP = "_backgroundForDateTimeMap";
//    public static final String _MAX_DATE_TIME = "_maxDateTime";
//    public static final String _MIN_DATE_TIME = "_minDateTime";
//    public static final String _TEXT_COLOR_FOR_DATETIME_MAP = "_textColorForDateTimeMap";
//    public static int disabledBackgroundDrawable = -1;
//    public static int disabledTextColor = 0xff888888;
//    public String TAG;
//    protected HashMap backgroundForDateTimeMap;
//    protected HashMap caldroidData;
//    private CaldroidListener caldroidListener;
//    private TextView cancelTV;
//    protected ArrayList dateInMonthsList;
//    private android.widget.AdapterView.OnItemClickListener dateItemClickListener;
//    private android.widget.AdapterView.OnItemLongClickListener dateItemLongClickListener;
//    protected ArrayList datePagerAdapters;
//    private InfiniteViewPager dateViewPager;
//    protected String dialogTitle;
//    protected ArrayList disableDates;
//    protected boolean enableClickOnDisabledDates;
//    protected boolean enableSwipe;
//    protected HashMap extraData;
//    private Time firstMonthTime;
//    private ArrayList fragments;
//    private boolean isNext;
//    private boolean isPrev;
//    private Button leftArrowButton;
//    protected DateTime maxDateTime;
//    protected DateTime minDateTime;
//    protected int month;
//    private TextView monthTitleTextView;
//    private Formatter monthYearFormatter;
//    private final StringBuilder monthYearStringBuilder = new StringBuilder(50);
//    private TextView nowTV;
//    private TextView okayTV;
//    private DatePageChangeListener pageChangeListener;
//    private Button rightArrowButton;
//    protected ArrayList selectedDates;
//    protected boolean showNavigationArrows;
//    private boolean sixWeeksInCalendar;
//    protected boolean squareTextViewCell;
//    protected int startDayOfWeek;
//    protected HashMap textColorForDateTimeMap;
//    private int themeResource;
//    private int todayColorPos;
//    private GridView weekdayGridView;
//    protected int year;
//
//    public CaldroidFragment()
//    {
//        TAG = "CaldroidFragment";
//        firstMonthTime = new Time();
//        monthYearFormatter = new Formatter(monthYearStringBuilder, Locale.getDefault());
//        themeResource = com.caldroid.R.style.CaldroidDefault;
//        month = -1;
//        year = -1;
//        disableDates = new ArrayList();
//        selectedDates = new ArrayList();
//        caldroidData = new HashMap();
//        extraData = new HashMap();
//        backgroundForDateTimeMap = new HashMap();
//        textColorForDateTimeMap = new HashMap();
//        startDayOfWeek = SUNDAY;
//        sixWeeksInCalendar = true;
//        datePagerAdapters = new ArrayList();
//        enableSwipe = true;
//        showNavigationArrows = true;
//        enableClickOnDisabledDates = false;
//    }
//
//    public static LayoutInflater getLayoutInflater(Context context, LayoutInflater layoutinflater, int i)
//    {
//        return layoutinflater.cloneInContext(new ContextThemeWrapper(context, i));
//    }
//
//    public static CaldroidFragment newInstance(String s, int i, int j)
//    {
//        CaldroidFragment caldroidfragment = new CaldroidFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("dialogTitle", s);
//        bundle.putInt("month", i);
//        bundle.putInt("year", j);
//        caldroidfragment.setArguments(bundle);
//        return caldroidfragment;
//    }
//
//    private void setupDateGridPages(View view)
//    {
//        Object obj = new DateTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
//        pageChangeListener = new DatePageChangeListener();
//        pageChangeListener.setCurrentDateTime(((DateTime) (obj)));
//        CaldroidGridAdapter caldroidgridadapter = getNewDatesGridAdapter(((DateTime) (obj)).getMonth().intValue(), ((DateTime) (obj)).getYear().intValue());
//        dateInMonthsList = caldroidgridadapter.getDatetimeList();
//        Object obj1 = ((DateTime) (obj)).plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//        CaldroidGridAdapter caldroidgridadapter1 = getNewDatesGridAdapter(((DateTime) (obj1)).getMonth().intValue(), ((DateTime) (obj1)).getYear().intValue());
//        obj1 = ((DateTime) (obj1)).plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//        obj1 = getNewDatesGridAdapter(((DateTime) (obj1)).getMonth().intValue(), ((DateTime) (obj1)).getYear().intValue());
//        obj = ((DateTime) (obj)).minus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//        obj = getNewDatesGridAdapter(((DateTime) (obj)).getMonth().intValue(), ((DateTime) (obj)).getYear().intValue());
//        datePagerAdapters.add(caldroidgridadapter);
//        datePagerAdapters.add(caldroidgridadapter1);
//        datePagerAdapters.add(obj1);
//        datePagerAdapters.add(obj);
//        pageChangeListener.setCaldroidGridAdapters(datePagerAdapters);
//        dateViewPager = (InfiniteViewPager)view.findViewById(com.caldroid.R.id.months_infinite_pager);
//        dateViewPager.setEnabled(enableSwipe);
//        dateViewPager.setSixWeeksInCalendar(sixWeeksInCalendar);
//        dateViewPager.setDatesInMonth(dateInMonthsList);
//        view = new MonthPagerAdapter(getChildFragmentManager());
//        fragments = view.getFragments();
//        for (int i = 0; i < 4; i++)
//        {
//            DateGridFragment dategridfragment = (DateGridFragment)fragments.get(i);
//            CaldroidGridAdapter caldroidgridadapter2 = (CaldroidGridAdapter)datePagerAdapters.get(i);
//            dategridfragment.setGridViewRes(getGridViewRes());
//            dategridfragment.setGridAdapter(caldroidgridadapter2);
//            dategridfragment.setOnItemClickListener(getDateItemClickListener());
//        }
//
//        view = new InfinitePagerAdapter(view);
//        dateViewPager.setAdapter(view);
//        dateViewPager.setOnPageChangeListener(pageChangeListener);
//    }
//
//    public void clearBackgroundResourceForDate(Date date)
//    {
//        date = CalendarHelper.convertDateToDateTime(date);
//        backgroundForDateTimeMap.remove(date);
//    }
//
//    public void clearBackgroundResourceForDateTime(DateTime datetime)
//    {
//        backgroundForDateTimeMap.remove(datetime);
//    }
//
//    public void clearBackgroundResourceForDateTimes(List list)
//    {
//        if (list != null && list.size() != 0)
//        {
//            list = list.iterator();
//            while (list.hasNext())
//            {
//                DateTime datetime = (DateTime)list.next();
//                backgroundForDateTimeMap.remove(datetime);
//            }
//        }
//    }
//
//    public void clearBackgroundResourceForDates(List list)
//    {
//        if (list != null && list.size() != 0)
//        {
//            list = list.iterator();
//            while (list.hasNext())
//            {
//                clearBackgroundResourceForDate((Date)list.next());
//            }
//        }
//    }
//
//    public void clearDisableDates()
//    {
//        disableDates.clear();
//    }
//
//    public void clearSelectedDates()
//    {
//        selectedDates.clear();
//    }
//
//    public void clearTextColorForDate(Date date)
//    {
//        date = CalendarHelper.convertDateToDateTime(date);
//        textColorForDateTimeMap.remove(date);
//    }
//
//    public void clearTextColorForDates(List list)
//    {
//        if (list != null && list.size() != 0)
//        {
//            list = list.iterator();
//            while (list.hasNext())
//            {
//                clearTextColorForDate((Date)list.next());
//            }
//        }
//    }
//
//    public HashMap getBackgroundForDateTimeMap()
//    {
//        return backgroundForDateTimeMap;
//    }
//
//
//    protected void setCustomResources(DateTime datetime, View view, TextView textview)
//    {
//        Object obj = (HashMap)caldroidData.get("_backgroundForDateTimeMap");
//        if (obj != null)
//        {
//            obj = (Integer)((HashMap) (obj)).get(datetime);
//            if (obj != null)
//            {
//                view.setBackgroundResource(((Integer) (obj)).intValue());
//            }
//        }
//        view = (HashMap)caldroidData.get("_textColorForDateTimeMap");
//        if (view != null)
//        {
//            datetime = (Integer)view.get(datetime);
//            if (datetime != null)
//            {
//                textview.setTextColor(resources.getColor(datetime.intValue()));
//            }
//        }
//        datetime = ((CellView)textview).getCustomStates();
//        int i = 0;
//        while (i < datetime.size())
//        {
//            view = (Integer)datetime.get(i);
//            if (view.intValue() == CellView.STATE_PREV_MONTH || view.intValue() == CellView.STATE_NEXT_MONTH)
//            {
//                ((CellView)textview).setTextColor(context.getResources().getColor(com.caldroid.R.color.cal_text_dis_color));
//                ((CellView)textview).setBackgroundColor(context.getResources().getColor(com.caldroid.R.color.cal_bg_color));
//            } else
//            if (view.intValue() == CellView.STATE_TODAY)
//            {
//                ((CellView)textview).setTextColor(context.getResources().getColor(com.caldroid.R.color.cal_text_color));
//                ((CellView)textview).setBackgroundResource(com.caldroid.R.drawable.cal_today_bg);
//            }
//            i++;
//        }
//    }
//    public android.widget.AdapterView.OnItemClickListener getDateItemClickListener()
//    {
//        if (dateItemClickListener == null)
//        {
//            dateItemClickListener = new android.widget.AdapterView.OnItemClickListener() {
//
//                final CaldroidFragment this$0;
//
//                public void onItemClick(AdapterView adapterview, View view, int i, long l)
//                {
//                    DateTime datetime;
//                    int k;
//                    datetime = (DateTime)dateInMonthsList.get(i);
//                    for (int j = 0; j < adapterview.getChildCount(); j++)
//                    {
//                        if (((SquareTextView)adapterview.getChildAt(j)).getCustomStates().size() > 0 && ((Integer)((SquareTextView)adapterview.getChildAt(j)).getCustomStates().get(0)).intValue() == CellView.STATE_TODAY)
//                        {
//                            todayColorPos = j;
//                        }
//                        adapterview.getChildAt(j).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                    }
//
//                    if (datetime.getMonth().intValue() == Calendar.getInstance().get(2) + 1)
//                    {
//                        adapterview.getChildAt(todayColorPos).setBackgroundResource(com.caldroid.R.drawable.cal_today_bg);
//                    }
//                    adapterview.getChildAt(i).setBackgroundResource(com.caldroid.R.drawable.cal_selected_bg);
//                    k = 0;
//                    _L7:
//                    if (k >= adapterview.getChildCount()) goto _L2; else goto _L1
//                    _L1:
//                    ArrayList arraylist;
//                    int i1;
//                    arraylist = ((SquareTextView)adapterview.getChildAt(i)).getCustomStates();
//                    isNext = false;
//                    isPrev = false;
//                    i1 = 0;
//                    _L5:
//                    if (i1 >= arraylist.size()) goto _L4; else goto _L3
//                    _L3:
//                    if (((Integer)arraylist.get(i1)).intValue() == CellView.STATE_PREV_MONTH)
//                    {
//                        adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                        isPrev = true;
//                        adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                    } else
//                    {
//                        label0:
//                        {
//                            isPrev = false;
//                            if (((Integer)arraylist.get(i1)).intValue() != CellView.STATE_NEXT_MONTH)
//                            {
//                                break label0;
//                            }
//                            adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                            isNext = true;
//                        }
//                    }
//                    _L2:
//                    if (isPrev)
//                    {
//                        prevMonth();
//                    }
//                    if (isNext)
//                    {
//                        nextMonth();
//                    }
//                    if (caldroidListener == null || !enableClickOnDisabledDates && (minDateTime != null && datetime.lt(minDateTime) || maxDateTime != null && datetime.gt(maxDateTime) || disableDates != null && disableDates.indexOf(datetime) != -1))
//                    {
//                        return;
//                    } else
//                    {
//                        adapterview = CalendarHelper.convertDateTimeToDate(datetime);
//                        caldroidListener.onSelectDate(adapterview, view);
//                        return;
//                    }
//                    isNext = false;
//                    if (((Integer)arraylist.get(i1)).intValue() == CellView.STATE_TODAY)
//                    {
//                        adapterview.getChildAt(i).setBackgroundResource(com.caldroid.R.drawable.cal_selected_bg);
//                    }
//                    i1++;
//                    goto _L5
//                    _L4:
//                    k++;
//                    if (true) goto _L7; else goto _L6
//                    _L6:
//                }
//
//
//                {
//                    this$0 = CaldroidFragment.this;
//                    super();
//                }
//            };
//        }
//        return dateItemClickListener;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public HashMap getCaldroidData()
//    {
//        caldroidData.clear();
//        caldroidData.put("disableDates", disableDates);
//        caldroidData.put("selectedDates", selectedDates);
//        caldroidData.put("_minDateTime", minDateTime);
//        caldroidData.put("_maxDateTime", maxDateTime);
//        caldroidData.put("startDayOfWeek", Integer.valueOf(startDayOfWeek));
//        caldroidData.put("sixWeeksInCalendar", Boolean.valueOf(sixWeeksInCalendar));
//        caldroidData.put("squareTextViewCell", Boolean.valueOf(squareTextViewCell));
//        caldroidData.put("themeResource", Integer.valueOf(themeResource));
//        caldroidData.put("_backgroundForDateTimeMap", backgroundForDateTimeMap);
//        caldroidData.put("_textColorForDateTimeMap", textColorForDateTimeMap);
//        return caldroidData;
//    }
//
//    public CaldroidListener getCaldroidListener()
//    {
//        return caldroidListener;
//    }
//
//    public int getCurrentVirtualPosition()
//    {
//        int i = dateViewPager.getCurrentItem();
//        return pageChangeListener.getCurrent(i);
//    }
//
//    public android.widget.AdapterView.OnItemClickListener getDateItemClickListener()
//    {
//        if (dateItemClickListener == null)
//        {
//            dateItemClickListener = new android.widget.AdapterView.OnItemClickListener() {
//
//                final CaldroidFragment this$0;
//
//                public void onItemClick(AdapterView adapterview, View view, int i, long l)
//                {
//                    DateTime datetime;
//                    int k;
//                    datetime = (DateTime)dateInMonthsList.get(i);
//                    for (int j = 0; j < adapterview.getChildCount(); j++)
//                    {
//                        if (((SquareTextView)adapterview.getChildAt(j)).getCustomStates().size() > 0 && ((Integer)((SquareTextView)adapterview.getChildAt(j)).getCustomStates().get(0)).intValue() == CellView.STATE_TODAY)
//                        {
//                            todayColorPos = j;
//                        }
//                        adapterview.getChildAt(j).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                    }
//
//                    if (datetime.getMonth().intValue() == Calendar.getInstance().get(2) + 1)
//                    {
//                        adapterview.getChildAt(todayColorPos).setBackgroundResource(com.caldroid.R.drawable.cal_today_bg);
//                    }
//                    adapterview.getChildAt(i).setBackgroundResource(com.caldroid.R.drawable.cal_selected_bg);
//                    k = 0;
//_L7:
//                    if (k >= adapterview.getChildCount()) goto _L2; else goto _L1
//_L1:
//                    ArrayList arraylist;
//                    int i1;
//                    arraylist = ((SquareTextView)adapterview.getChildAt(i)).getCustomStates();
//                    isNext = false;
//                    isPrev = false;
//                    i1 = 0;
//_L5:
//                    if (i1 >= arraylist.size()) goto _L4; else goto _L3
//_L3:
//                    if (((Integer)arraylist.get(i1)).intValue() == CellView.STATE_PREV_MONTH)
//                    {
//                        adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                        isPrev = true;
//                        adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                    } else
//                    {
//label0:
//                        {
//                            isPrev = false;
//                            if (((Integer)arraylist.get(i1)).intValue() != CellView.STATE_NEXT_MONTH)
//                            {
//                                break label0;
//                            }
//                            adapterview.getChildAt(i).setBackgroundColor(getResources().getColor(com.caldroid.R.color.cal_bg_color));
//                            isNext = true;
//                        }
//                    }
//_L2:
//                    if (isPrev)
//                    {
//                        prevMonth();
//                    }
//                    if (isNext)
//                    {
//                        nextMonth();
//                    }
//                    if (caldroidListener == null || !enableClickOnDisabledDates && (minDateTime != null && datetime.lt(minDateTime) || maxDateTime != null && datetime.gt(maxDateTime) || disableDates != null && disableDates.indexOf(datetime) != -1))
//                    {
//                        return;
//                    } else
//                    {
//                        adapterview = CalendarHelper.convertDateTimeToDate(datetime);
//                        caldroidListener.onSelectDate(adapterview, view);
//                        return;
//                    }
//                    isNext = false;
//                    if (((Integer)arraylist.get(i1)).intValue() == CellView.STATE_TODAY)
//                    {
//                        adapterview.getChildAt(i).setBackgroundResource(com.caldroid.R.drawable.cal_selected_bg);
//                    }
//                    i1++;
//                      goto _L5
//_L4:
//                    k++;
//                    if (true) goto _L7; else goto _L6
//_L6:
//                }
//
//
//            {
//                this$0 = CaldroidFragment.this;
//                super();
//            }
//            };
//        }
//        return dateItemClickListener;
//    }
//
//    public android.widget.AdapterView.OnItemLongClickListener getDateItemLongClickListener()
//    {
//        if (dateItemLongClickListener == null)
//        {
//            dateItemLongClickListener = new android.widget.AdapterView.OnItemLongClickListener() {
//
//                final CaldroidFragment this$0;
//
//                public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
//                {
//                    adapterview = (DateTime)dateInMonthsList.get(i);
//                    if (caldroidListener != null)
//                    {
//                        if (!enableClickOnDisabledDates && (minDateTime != null && adapterview.lt(minDateTime) || maxDateTime != null && adapterview.gt(maxDateTime) || disableDates != null && disableDates.indexOf(adapterview) != -1))
//                        {
//                            return false;
//                        }
//                        adapterview = CalendarHelper.convertDateTimeToDate(adapterview);
//                        caldroidListener.onLongClickDate(adapterview, view);
//                    }
//                    return true;
//                }
//
//
//            {
//                this$0 = CaldroidFragment.this;
//                super();
//            }
//            };
//        }
//        return dateItemLongClickListener;
//    }
//
//    public ArrayList getDatePagerAdapters()
//    {
//        return datePagerAdapters;
//    }
//
//    public InfiniteViewPager getDateViewPager()
//    {
//        return dateViewPager;
//    }
//
//    protected ArrayList getDaysOfWeek()
//    {
//        ArrayList arraylist = new ArrayList();
//        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE", Locale.getDefault());
//        DateTime datetime = (new DateTime(Integer.valueOf(2013), Integer.valueOf(2), Integer.valueOf(17), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0))).plusDays(Integer.valueOf(startDayOfWeek - SUNDAY));
//        for (int i = 0; i < 7; i++)
//        {
//            arraylist.add(simpledateformat.format(CalendarHelper.convertDateTimeToDate(datetime)).substring(0, 2));
//            datetime = datetime.plusDays(Integer.valueOf(1));
//        }
//
//        return arraylist;
//    }
//
//    public HashMap getExtraData()
//    {
//        return extraData;
//    }
//
//    public ArrayList getFragments()
//    {
//        return fragments;
//    }
//
//    protected int getGridViewRes()
//    {
//        return com.caldroid.R.layout.date_grid_fragment;
//    }
//
//    public Button getLeftArrowButton()
//    {
//        return leftArrowButton;
//    }
//
//    public TextView getMonthTitleTextView()
//    {
//        return monthTitleTextView;
//    }
//
//    public CaldroidGridAdapter getNewDatesGridAdapter(int i, int j)
//    {
//        return new CaldroidGridAdapter(getActivity(), i, j, getCaldroidData(), extraData);
//    }
//
//    public WeekdayArrayAdapter getNewWeekdayAdapter()
//    {
//        return new WeekdayArrayAdapter(getActivity(), 0x1090003, getDaysOfWeek());
//    }
//
//    public Button getRightArrowButton()
//    {
//        return rightArrowButton;
//    }
//
//    public Bundle getSavedStates()
//    {
//        Bundle bundle = new Bundle();
//        bundle.putInt("month", month);
//        bundle.putInt("year", year);
//        if (dialogTitle != null)
//        {
//            bundle.putString("dialogTitle", dialogTitle);
//        }
//        if (selectedDates != null && selectedDates.size() > 0)
//        {
//            bundle.putStringArrayList("selectedDates", CalendarHelper.convertToStringList(selectedDates));
//        }
//        if (disableDates != null && disableDates.size() > 0)
//        {
//            bundle.putStringArrayList("disableDates", CalendarHelper.convertToStringList(disableDates));
//        }
//        if (minDateTime != null)
//        {
//            bundle.putString("minDate", minDateTime.format("YYYY-MM-DD"));
//        }
//        if (maxDateTime != null)
//        {
//            bundle.putString("maxDate", maxDateTime.format("YYYY-MM-DD"));
//        }
//        bundle.putBoolean("showNavigationArrows", showNavigationArrows);
//        bundle.putBoolean("enableSwipe", enableSwipe);
//        bundle.putInt("startDayOfWeek", startDayOfWeek);
//        bundle.putBoolean("sixWeeksInCalendar", sixWeeksInCalendar);
//        bundle.putInt("themeResource", themeResource);
//        return bundle;
//    }
//
//    public HashMap getTextColorForDateTimeMap()
//    {
//        return textColorForDateTimeMap;
//    }
//
//    public int getThemeResource()
//    {
//        return themeResource;
//    }
//
//    public GridView getWeekdayGridView()
//    {
//        return weekdayGridView;
//    }
//
//    public boolean isEnableSwipe()
//    {
//        return enableSwipe;
//    }
//
//    public boolean isShowNavigationArrows()
//    {
//        return showNavigationArrows;
//    }
//
//    public boolean isSixWeeksInCalendar()
//    {
//        return sixWeeksInCalendar;
//    }
//
//    public void moveToDate(Date date)
//    {
//        moveToDateTime(CalendarHelper.convertDateToDateTime(date));
//    }
//
//    public void moveToDateTime(DateTime datetime)
//    {
//        DateTime datetime1 = new DateTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
//        DateTime datetime2 = datetime1.getEndOfMonth();
//        if (datetime.lt(datetime1))
//        {
//            datetime = datetime.plus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//            pageChangeListener.setCurrentDateTime(datetime);
//            int i = dateViewPager.getCurrentItem();
//            pageChangeListener.refreshAdapters(i);
//            dateViewPager.setCurrentItem(i - 1);
//        } else
//        if (datetime.gt(datetime2))
//        {
//            datetime = datetime.minus(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), hirondelle.date4j.DateTime.DayOverflow.LastDay);
//            pageChangeListener.setCurrentDateTime(datetime);
//            int j = dateViewPager.getCurrentItem();
//            pageChangeListener.refreshAdapters(j);
//            dateViewPager.setCurrentItem(j + 1);
//            return;
//        }
//    }
//
//    public void nextMonth()
//    {
//        dateViewPager.setCurrentItem(pageChangeListener.getCurrentPage() + 1);
//    }
//
//    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
//    {
//        retrieveInitialArgs();
//        setStyle(1, com.caldroid.R.style.MyDialog);
//        if (getDialog() != null)
//        {
//            try
//            {
//                setRetainInstance(true);
//            }
//            // Misplaced declaration of an exception variable
//            catch (Bundle bundle)
//            {
//                bundle.printStackTrace();
//            }
//        }
//        layoutinflater = getLayoutInflater(getActivity(), layoutinflater, themeResource).inflate(com.caldroid.R.layout.calendar_view, viewgroup, false);
//        monthTitleTextView = (TextView)layoutinflater.findViewById(com.caldroid.R.id.calendar_month_year_textview);
//        nowTV = (TextView)layoutinflater.findViewById(com.caldroid.R.id.now);
//        cancelTV = (TextView)layoutinflater.findViewById(com.caldroid.R.id.cancel);
//        okayTV = (TextView)layoutinflater.findViewById(com.caldroid.R.id.okay);
//        leftArrowButton = (Button)layoutinflater.findViewById(com.caldroid.R.id.calendar_left_arrow);
//        rightArrowButton = (Button)layoutinflater.findViewById(com.caldroid.R.id.calendar_right_arrow);
//        leftArrowButton.setOnClickListener(new android.view.View.OnClickListener() {
//
//            final CaldroidFragment this$0;
//
//            public void onClick(View view)
//            {
//                prevMonth();
//            }
//
//
//            {
//                this$0 = CaldroidFragment.this;
//                super();
//            }
//        });
//        rightArrowButton.setOnClickListener(new android.view.View.OnClickListener() {
//
//            final CaldroidFragment this$0;
//
//            public void onClick(View view)
//            {
//                nextMonth();
//            }
//
//
//            {
//                this$0 = CaldroidFragment.this;
//                super();
//            }
//        });
//        setShowNavigationArrows(showNavigationArrows);
//        weekdayGridView = (GridView)layoutinflater.findViewById(com.caldroid.R.id.weekday_gridview);
//        viewgroup = getNewWeekdayAdapter();
//        weekdayGridView.setAdapter(viewgroup);
//        setupDateGridPages(layoutinflater);
//        refreshView();
//        return layoutinflater;
//    }
//
//    public void onDestroyView()
//    {
//        if (getDialog() != null && getRetainInstance())
//        {
//            getDialog().setDismissMessage(null);
//        }
//        super.onDestroyView();
//    }
//
//    public void onDetach()
//    {
//        super.onDetach();
//        try
//        {
//            Field field = android/support/v4/app/Fragment.getDeclaredField("mChildFragmentManager");
//            field.setAccessible(true);
//            field.set(this, null);
//            return;
//        }
//        catch (NoSuchFieldException nosuchfieldexception)
//        {
//            throw new RuntimeException(nosuchfieldexception);
//        }
//        catch (IllegalAccessException illegalaccessexception)
//        {
//            throw new RuntimeException(illegalaccessexception);
//        }
//    }
//
//    public void onViewCreated(View view, Bundle bundle)
//    {
//        super.onViewCreated(view, bundle);
//        if (caldroidListener != null)
//        {
//            caldroidListener.onCaldroidViewCreated();
//        }
//    }
//
//    public void prevMonth()
//    {
//        dateViewPager.setCurrentItem(pageChangeListener.getCurrentPage() - 1);
//    }
//
//    protected void refreshMonthTitleTextView()
//    {
//        firstMonthTime.year = year;
//        firstMonthTime.month = month - 1;
//        firstMonthTime.monthDay = 1;
//        long l = firstMonthTime.toMillis(true);
//        monthYearStringBuilder.setLength(0);
//        String s = DateUtils.formatDateRange(getActivity(), monthYearFormatter, l, l, 52).toString();
//        s = (new StringBuilder()).append(s.split(" ")[0].substring(0, 3)).append(" ").append(s.split(" ")[1]).toString();
//        monthTitleTextView.setText(s.toUpperCase(Locale.getDefault()));
//    }
//
//    public void refreshView()
//    {
//        if (month != -1 && year != -1)
//        {
//            refreshMonthTitleTextView();
//            Iterator iterator = datePagerAdapters.iterator();
//            while (iterator.hasNext())
//            {
//                CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter)iterator.next();
//                caldroidgridadapter.setCaldroidData(getCaldroidData());
//                caldroidgridadapter.setExtraData(extraData);
//                caldroidgridadapter.updateToday();
//                caldroidgridadapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    public void restoreDialogStatesFromKey(FragmentManager fragmentmanager, Bundle bundle, String s, String s1)
//    {
//        restoreStatesFromKey(bundle, s);
//        bundle = (CaldroidFragment)fragmentmanager.findFragmentByTag(s1);
//        if (bundle != null)
//        {
//            bundle.dismiss();
//            show(fragmentmanager, s1);
//        }
//    }
//
//    public void restoreStatesFromKey(Bundle bundle, String s)
//    {
//        if (bundle != null && bundle.containsKey(s))
//        {
//            setArguments(bundle.getBundle(s));
//        }
//    }
//
//    protected void retrieveInitialArgs()
//    {
//        Bundle bundle = getArguments();
//        if (bundle != null)
//        {
//            month = bundle.getInt("month", -1);
//            year = bundle.getInt("year", -1);
//            dialogTitle = bundle.getString("dialogTitle");
//            Object obj = getDialog();
//            if (obj != null)
//            {
//                if (dialogTitle != null)
//                {
//                    ((Dialog) (obj)).setTitle(dialogTitle);
//                } else
//                {
//                    ((Dialog) (obj)).requestWindowFeature(1);
//                }
//            }
//            startDayOfWeek = bundle.getInt("startDayOfWeek", 1);
//            if (startDayOfWeek > 7)
//            {
//                startDayOfWeek = startDayOfWeek % 7;
//            }
//            showNavigationArrows = bundle.getBoolean("showNavigationArrows", true);
//            enableSwipe = bundle.getBoolean("enableSwipe", true);
//            sixWeeksInCalendar = bundle.getBoolean("sixWeeksInCalendar", true);
//            if (getResources().getConfiguration().orientation == 1)
//            {
//                squareTextViewCell = bundle.getBoolean("squareTextViewCell", true);
//            } else
//            {
//                squareTextViewCell = bundle.getBoolean("squareTextViewCell", false);
//            }
//            enableClickOnDisabledDates = bundle.getBoolean("enableClickOnDisabledDates", false);
//            obj = bundle.getStringArrayList("disableDates");
//            if (obj != null && ((ArrayList) (obj)).size() > 0)
//            {
//                disableDates.clear();
//                DateTime datetime1;
//                for (obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext(); disableDates.add(datetime1))
//                {
//                    datetime1 = CalendarHelper.getDateTimeFromString((String)((Iterator) (obj)).next(), "yyyy-MM-dd");
//                }
//
//            }
//            obj = bundle.getStringArrayList("selectedDates");
//            if (obj != null && ((ArrayList) (obj)).size() > 0)
//            {
//                selectedDates.clear();
//                DateTime datetime2;
//                for (obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext(); selectedDates.add(datetime2))
//                {
//                    datetime2 = CalendarHelper.getDateTimeFromString((String)((Iterator) (obj)).next(), "yyyy-MM-dd");
//                }
//
//            }
//            obj = bundle.getString("minDate");
//            if (obj != null)
//            {
//                minDateTime = CalendarHelper.getDateTimeFromString(((String) (obj)), null);
//            }
//            obj = bundle.getString("maxDate");
//            if (obj != null)
//            {
//                maxDateTime = CalendarHelper.getDateTimeFromString(((String) (obj)), null);
//            }
//            themeResource = bundle.getInt("themeResource", com.caldroid.R.style.CaldroidDefault);
//        }
//        if (month == -1 || year == -1)
//        {
//            DateTime datetime = DateTime.today(TimeZone.getDefault());
//            month = datetime.getMonth().intValue();
//            year = datetime.getYear().intValue();
//        }
//    }
//
//    public void saveStatesToKey(Bundle bundle, String s)
//    {
//        bundle.putBundle(s, getSavedStates());
//    }
//
//    public void setBackgroundResourceForDate(int i, Date date)
//    {
//        date = CalendarHelper.convertDateToDateTime(date);
//        backgroundForDateTimeMap.put(date, Integer.valueOf(i));
//    }
//
//    public void setBackgroundResourceForDateTime(int i, DateTime datetime)
//    {
//        backgroundForDateTimeMap.put(datetime, Integer.valueOf(i));
//    }
//
//    public void setBackgroundResourceForDateTimes(HashMap hashmap)
//    {
//        backgroundForDateTimeMap.putAll(hashmap);
//    }
//
//    public void setBackgroundResourceForDates(HashMap hashmap)
//    {
//        if (hashmap != null && hashmap.size() != 0)
//        {
//            backgroundForDateTimeMap.clear();
//            Iterator iterator = hashmap.keySet().iterator();
//            while (iterator.hasNext())
//            {
//                Object obj = (Date)iterator.next();
//                Integer integer = (Integer)hashmap.get(obj);
//                obj = CalendarHelper.convertDateToDateTime(((Date) (obj)));
//                backgroundForDateTimeMap.put(obj, integer);
//            }
//        }
//    }
//
//    public void setCaldroidListener(CaldroidListener caldroidlistener)
//    {
//        caldroidListener = caldroidlistener;
//    }
//
//    public void setCalendarDate(Date date)
//    {
//        setCalendarDateTime(CalendarHelper.convertDateToDateTime(date));
//    }
//
//    public void setCalendarDateTime(DateTime datetime)
//    {
//        month = datetime.getMonth().intValue();
//        year = datetime.getYear().intValue();
//        if (caldroidListener != null)
//        {
//            caldroidListener.onChangeMonth(month, year);
//        }
//        refreshView();
//    }
//
//    public void setDisableDates(ArrayList arraylist)
//    {
//        if (arraylist != null && arraylist.size() != 0)
//        {
//            disableDates.clear();
//            arraylist = arraylist.iterator();
//            while (arraylist.hasNext())
//            {
//                DateTime datetime = CalendarHelper.convertDateToDateTime((Date)arraylist.next());
//                disableDates.add(datetime);
//            }
//        }
//    }
//
//    public void setDisableDatesFromString(ArrayList arraylist)
//    {
//        setDisableDatesFromString(arraylist, null);
//    }
//
//    public void setDisableDatesFromString(ArrayList arraylist, String s)
//    {
//        if (arraylist != null)
//        {
//            disableDates.clear();
//            arraylist = arraylist.iterator();
//            while (arraylist.hasNext())
//            {
//                DateTime datetime = CalendarHelper.getDateTimeFromString((String)arraylist.next(), s);
//                disableDates.add(datetime);
//            }
//        }
//    }
//
//    public void setEnableSwipe(boolean flag)
//    {
//        enableSwipe = flag;
//        dateViewPager.setEnabled(flag);
//    }
//
//    public void setExtraData(HashMap hashmap)
//    {
//        extraData = hashmap;
//    }
//
//    public void setMaxDate(Date date)
//    {
//        if (date == null)
//        {
//            maxDateTime = null;
//            return;
//        } else
//        {
//            maxDateTime = CalendarHelper.convertDateToDateTime(date);
//            return;
//        }
//    }
//
//    public void setMaxDateFromString(String s, String s1)
//    {
//        if (s == null)
//        {
//            setMaxDate(null);
//            return;
//        } else
//        {
//            maxDateTime = CalendarHelper.getDateTimeFromString(s, s1);
//            return;
//        }
//    }
//
//    public void setMinDate(Date date)
//    {
//        if (date == null)
//        {
//            minDateTime = null;
//            return;
//        } else
//        {
//            minDateTime = CalendarHelper.convertDateToDateTime(date);
//            return;
//        }
//    }
//
//    public void setMinDateFromString(String s, String s1)
//    {
//        if (s == null)
//        {
//            setMinDate(null);
//            return;
//        } else
//        {
//            minDateTime = CalendarHelper.getDateTimeFromString(s, s1);
//            return;
//        }
//    }
//
//    public void setMonthTitleTextView(TextView textview)
//    {
//        monthTitleTextView = textview;
//    }
//
//    public void setSelectedDateStrings(String s, String s1, String s2)
//        throws ParseException
//    {
//        setSelectedDates(CalendarHelper.getDateFromString(s, s2), CalendarHelper.getDateFromString(s1, s2));
//    }
//
//    public void setSelectedDates(Date date, Date date1)
//    {
//        if (date == null || date1 == null || date.after(date1))
//        {
//            return;
//        }
//        selectedDates.clear();
//        date = CalendarHelper.convertDateToDateTime(date);
//        for (date1 = CalendarHelper.convertDateToDateTime(date1); date.lt(date1); date = date.plusDays(Integer.valueOf(1)))
//        {
//            selectedDates.add(date);
//        }
//
//        selectedDates.add(date1);
//    }
//
//    public void setShowNavigationArrows(boolean flag)
//    {
//        showNavigationArrows = flag;
//        if (flag)
//        {
//            leftArrowButton.setVisibility(0);
//            rightArrowButton.setVisibility(0);
//            return;
//        } else
//        {
//            leftArrowButton.setVisibility(4);
//            rightArrowButton.setVisibility(4);
//            return;
//        }
//    }
//
//    public void setSixWeeksInCalendar(boolean flag)
//    {
//        sixWeeksInCalendar = flag;
//        dateViewPager.setSixWeeksInCalendar(flag);
//    }
//
//    public void setTextColorForDate(int i, Date date)
//    {
//        date = CalendarHelper.convertDateToDateTime(date);
//        textColorForDateTimeMap.put(date, Integer.valueOf(i));
//    }
//
//    public void setTextColorForDateTime(int i, DateTime datetime)
//    {
//        textColorForDateTimeMap.put(datetime, Integer.valueOf(i));
//    }
//
//    public void setTextColorForDateTimes(HashMap hashmap)
//    {
//        textColorForDateTimeMap.putAll(hashmap);
//    }
//
//    public void setTextColorForDates(HashMap hashmap)
//    {
//        if (hashmap != null && hashmap.size() != 0)
//        {
//            textColorForDateTimeMap.clear();
//            Iterator iterator = hashmap.keySet().iterator();
//            while (iterator.hasNext())
//            {
//                Object obj = (Date)iterator.next();
//                Integer integer = (Integer)hashmap.get(obj);
//                obj = CalendarHelper.convertDateToDateTime(((Date) (obj)));
//                textColorForDateTimeMap.put(obj, integer);
//            }
//        }
//    }
//
//    public void setThemeResource(int i)
//    {
//        themeResource = i;
//    }
//
//    static
//    {
//        SUNDAY = 1;
//        MONDAY = 2;
//        TUESDAY = 3;
//        WEDNESDAY = 4;
//        THURSDAY = 5;
//        FRIDAY = 6;
//        SATURDAY = 7;
//    }
//
//
//
///*
//    static int access$002(CaldroidFragment caldroidfragment, int i)
//    {
//        caldroidfragment.todayColorPos = i;
//        return i;
//    }
//
//*/
//
//
//
///*
//    static boolean access$102(CaldroidFragment caldroidfragment, boolean flag)
//    {
//        caldroidfragment.isNext = flag;
//        return flag;
//    }
//
//*/
//
//
//
///*
//    static boolean access$202(CaldroidFragment caldroidfragment, boolean flag)
//    {
//        caldroidfragment.isPrev = flag;
//        return flag;
//    }
//
//*/
//
//}
