package com.ammvision.sudoku;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class MyActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int gameType = 0;
    CellInfo[][] data = null;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1, gameType);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            //restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.solve_it) {
            PlaceholderFragment.Instance().SolveSudoku();
            return true;
        }
        else if(id == R.id.new_game) {
            if(gameType == 0)gameType = 1;
            PlaceholderFragment.Instance().NewGame(gameType);
        }
        else if(id == R.id.rotate_board) {
            PlaceholderFragment.Instance().RotateBoard();
        }
        else if(id == R.id.action_settings) {

            Intent intent = new Intent(this, Prefs.class);
            startActivity(intent);
        }
        else if(id == R.id.action_validate)
        {
            PlaceholderFragment.Instance().InvokeValidation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean value = sp.getBoolean("show_timer", true);
        if(!value)
            PlaceholderFragment.Instance().HideTimer();
        else
            PlaceholderFragment.Instance().ShowTimer();

        value = sp.getBoolean("instant_validation", true);
        if(!value)
            PlaceholderFragment.Instance().StopValidation();
        else
            PlaceholderFragment.Instance().StartValidation();

        value = sp.getBoolean("highlight_cells", true);
        if(!value)
            PlaceholderFragment.Instance().NonHighlightCells();
        else
            PlaceholderFragment.Instance().HighlightCells();

        value = sp.getBoolean("highlight_numbers", true);
        if(!value)
            PlaceholderFragment.Instance().NonHighlightNumbers();
        else
            PlaceholderFragment.Instance().HighlightNumbers();

        String val = sp.getString("listPref", "1");
        PlaceholderFragment.Instance().fillBoardPreference = Integer.parseInt(val);

        Bundle b = getIntent().getExtras();
        if(b != null ) {
            gameType = b.getInt("gameType");
            if(gameType > 0)
                PlaceholderFragment.Instance().NewGame(gameType);
            else
                gameType = PlaceholderFragment.Instance().LoadLastGame(savedInstanceState);

            mNavigationDrawerFragment.onlySelectItem(gameType);

            getIntent().removeExtra("gameType");
        }
    }

    public void NewGame(int type)
    {
        gameType = type;
        PlaceholderFragment.Instance().NewGame(gameType);
    }

    @Override
    public void onBackPressed()
    {
        PlaceholderFragment.Instance().PauseTimer();
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Bundle bundle = new Bundle();
        CellInfo[][] data = PlaceholderFragment.Instance().GetCurrentState();
        if(data != null)
        {
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++)
                {
                    bundle.putParcelable(""+y+x, data[y][x]);
                    //bundle
                }

            bundle.putBoolean("IsValid", PlaceholderFragment.Instance().IsValid);
            bundle.putString("timeString", PlaceholderFragment.Instance().timerString);
            bundle.putInt("gameType", PlaceholderFragment.Instance().gameType);

            savedInstanceState.putBundle("saved", bundle);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static PlaceholderFragment fragment;
        private CellInfo[][] celldata;
        private Deque<CellInfo> history;
        private Deque<CellInfo> redoHistory;
        private boolean sudokuGenerated;
        private Sudoku s;
        private CustomTextView selectedCell;
        private int selectedNumber;
        private TextView timeSpent;
        private TextView validityStatus;
        private Thread timerThread;
        private boolean IsValid = true;
        private boolean isInstantValidation = true;
        private boolean isHightlightCells = true;
        private boolean isHightlightNumbers = true;
        private int gameType = 0;
        private String timerString = null;
        private int fillBoardPreference = 1;

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                timeSpent.setText(msg.obj.toString());
                super.handleMessage(msg);
            }
        };

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int gameType) {
            if(fragment == null) {
                fragment = new PlaceholderFragment();
                fragment.gameType = gameType;
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
            }
            return fragment;
        }

        public static PlaceholderFragment Instance()
        {
            return fragment;
        }

        public PlaceholderFragment() {
            if(fragment == null) {
                celldata = new CellInfo[9][9];
                history = new LinkedList<CellInfo>();
                redoHistory = new LinkedList<CellInfo>();
                sudokuGenerated = false;
            }
        }

        public CellInfo[][] GetCurrentState()
        {
            return celldata;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Point size = GetSize();

            View rootView = null;
            if(size.x < size.y) {
                rootView = inflater.inflate(R.layout.fragment_my, container, false);
            }
            else
                rootView = inflater.inflate(R.layout.fragment_my_horizontal, container, false);

            return rootView;
        }

        public void NewGame(int type)
        {
            celldata = new CellInfo[9][9];
            history = new LinkedList<CellInfo>();
            redoHistory = new LinkedList<CellInfo>();
            sudokuGenerated = false;
            selectedCell = null;
            selectedNumber = -1;
            if(timerThread != null && timerThread.isAlive())
                timerThread.interrupt();

            IsValid = true;
            gameType = type;

            GenerateSudoku(type);

            timerString = null;
            StartTimer();

            UpdateValidityStatus(false);
        }

        public int LoadLastGame(Bundle savedInstanceState)
        {
            if(savedInstanceState != null) {
                Bundle bundle = savedInstanceState.getBundle("saved");
                if(bundle != null){
                    celldata = new CellInfo[9][9];
                    history = new LinkedList<CellInfo>();
                    redoHistory = new LinkedList<CellInfo>();
                    for(int y = 0; y < 9; y++)
                        for(int x = 0; x < 9; x++)
                        {
                            celldata[y][x] = bundle.getParcelable(""+y+x);
                        }

                    IsValid = bundle.getBoolean("IsValid");
                    timerString = bundle.getString("timeString");
                    gameType = bundle.getInt("gameType");

                    selectedCell = null;
                    selectedNumber = -1;

                    timerString = null;
                    IsValid = true;

                    StartTimer();

                    sudokuGenerated = true;
                    GenerateSudoku(gameType);
                    UpdateValidityStatus(false);
                }
            }
            return gameType;
        }

        public void RotateBoard()
        {
            CellInfo[][] board = new CellInfo[9][9];
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {
                    int X = y;
                    int Y = 8 - x;

                    board[y][x] = celldata[Y][X].Clone();
                    board[y][x].X = x;
                    board[y][x].Y = y;
                }

            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {
                    celldata[y][x] = board[y][x].Clone();
                }

            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView view = (CustomTextView) getView().findViewById(id);

                    UpdateUI(view, x, y);
                }

            Deque<CellInfo> tempHist = new LinkedList<CellInfo>();
            Iterator<CellInfo> itr = history.iterator();
            while(itr.hasNext()) {
                CellInfo info = itr.next().Clone();
                int y = info.Y;
                info.Y = info.X;
                info.X = 8 - y;
                tempHist.addLast(info);
            }

            history.clear();
            itr = tempHist.iterator();
            while(itr.hasNext()) {
                CellInfo info = itr.next().Clone();
                history.addLast(info);
            }

            tempHist = new LinkedList<CellInfo>();
            itr = redoHistory.iterator();
            while(itr.hasNext()) {
                CellInfo info = itr.next().Clone();
                int y = info.Y;
                info.Y = info.X;
                info.X = 8 - y;
                tempHist.addLast(info);
            }

            redoHistory.clear();
            itr = tempHist.iterator();
            while(itr.hasNext()) {
                CellInfo info = itr.next().Clone();
                redoHistory.addLast(info);
            }
        }

        private Point GetSize()
        {
            Point size = new Point();
            Display display = getActivity().getWindowManager().getDefaultDisplay();

            if (Build.VERSION.SDK_INT >= 17){
                //new pleasant way to get real metrics
                //DisplayMetrics realMetrics = new DisplayMetrics();
                //display.getRealMetrics(realMetrics);
                display.getSize(size);

                //size.x = realMetrics.widthPixels;
                //size.y = realMetrics.heightPixels;

            } else if (Build.VERSION.SDK_INT >= 14) {
                //reflection for this weird in-between time
                try {
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    size.x = (Integer) mGetRawW.invoke(display);
                    size.y = (Integer) mGetRawH.invoke(display);
                } catch (Exception e) {
                    //this may not be 100% accurate, but it's all we've got
                    size.x = display.getWidth();
                    size.y = display.getHeight();
                }

            } else {
                //This should be close, as lower API devices should not have window navigation bars
                size.x = display.getWidth();
                size.y = display.getHeight();
            }

            return size;
        }

        @Override
        public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);

            Point size = GetSize();

            timeSpent = (TextView)getView().findViewById(R.id.timeSpent);
            validityStatus = (TextView)getView().findViewById(R.id.validation);

            int cellSize = (size.x / 10);
            int cellSize1 = size.x / 8;

            if(size.x > size.y) {
                cellSize = size.y / 11;
                cellSize1 = size.y / 7;
            }

            if(s != null)
                SetData(s.GetData());
            //if(gameType == 0) gameType = 1;

            //GenerateSudoku(gameType);

            SetHeightWidth(cellSize);

            SetDimensionToNumbers(cellSize1);

            StartTimer();

            RequestAds();
        }

        private void RequestAds()
        {
            AdView mAdView = (AdView) getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        private void StartTimer()
        {
            if(timerThread != null && timerThread.isAlive()) return;

            timerThread = new Thread()
            {
                @Override
                public void run()
                {
                    int hour = 0; int min = 0; int sec = 0;

                    if(timerString != null) {
                        String[]str = timerString.split(":");
                        hour = Integer.parseInt(str[0]);
                        min = Integer.parseInt(str[1]);
                        sec = Integer.parseInt(str[2]);
                    }

                    while (true)
                    {
                        timerString = String.format("%02d:%02d:%02d", hour, min,sec);

                        sec++;
                        if(sec == 60) {
                            min++;
                            sec = 0;
                        }

                        if(min == 60) {
                            hour++;
                            min = 0;
                        }

                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.arg1 = 0;
                        msg.obj = timerString;
                        handler.sendMessage(msg);

                        try {
                            sleep(950);
                        }
                        catch (InterruptedException ex)
                        {
                            break;
                        }
                    }
                }
            };
            timerThread.start();
        }

        private void PauseTimer()
        {
            timerThread.interrupt();
        }

        private void GenerateSudoku(int type)
        {
            s = new Sudoku();
            try {
                int[][] d = GetData();
                s.SetData(d);

                boolean result = true;
                if(!sudokuGenerated)
                {
                    result = s.Generate(type);
                    if (result) {
                        d = s.GetData();
                        SetData(d);
                    }
                }
                else
                    SetData(d);
            }
            catch (Exception ex)
            {
            }

            sudokuGenerated = true;
        }

        public void SolveSudoku()
        {
            if(s != null) {
                boolean solved = s.Solve();
                if(solved) {
                    int[][] d = s.GetData();

                    for(int y = 0; y < 9; y++)
                        for(int x = 0; x < 9; x++) {
                            celldata[y][x].CurrentValue = d[y][x];
                            celldata[y][x].IsDisabled = true;
                        }

                    SetData(null);

                    timerThread.interrupt();
                }
            }
        }

        private int[][] GetData()
        {
            int[][] d = new int[9][9];
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++)
                    if(celldata[y][x] != null)
                        d[y][x] = celldata[y][x].CurrentValue;

            return d;
        }

        private void SetData(int[][] d)
        {
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView view = (CustomTextView) getView().findViewById(id);

                    if(celldata[y][x] == null) {
                        celldata[y][x] = new CellInfo();
                        view.X = x;
                        view.Y = y;
                        view.CurrentValue = d[y][x];
                        view.IsError = false;
                        view.IsDisabled = true;
                        view.setBackgroundResource(R.drawable.normal_cell_style);
                        view.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    CustomTextView view = (CustomTextView) v;
                                    SelectDependentCellOnTouch(view);
                                }
                                return true;
                            }

                        });
                        view.setTextColor(getResources().getColor(R.color.black));

                        if(d[y][x] == 0)
                        {
                            view.IsDisabled = false;
                            view.setBackgroundResource(R.drawable.normal_cell_style);
                            view.setText("");
                            UpdateActualStore(view);
                            continue;
                        }

                        view.setBackgroundResource(R.drawable.normal_cell_style);
                        view.setText(""+d[y][x]);

                        if(!view.UserEntered)
                            view.setBackgroundResource(R.drawable.disable_cell_style);

                        UpdateActualStore(view);
                    }
                    else
                    {
                        UpdateUI(view, x, y);
                    }
                }
        }

        private void SelectDependentCellOnTouch(CustomTextView cv)
        {
            if(cv == null) return;

            if(cv.IsSelected)
            {
                if(fillBoardPreference == 2 && !cv.IsDisabled && selectedNumber >= 0) {

                    AddToHistory(cv.Y, cv.X);
                    UpdateSelectedCell(Integer.toString(selectedNumber));

                    if(isInstantValidation) {
                        InvokeValidation(cv);
                        UpdateValidityStatus(true);
                    }
                }

                return;
            }

            if(isHightlightNumbers && !cv.IsError)
                cv.setTextColor(getResources().getColor(R.color.red));

            selectedCell = cv;

            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView view = (CustomTextView)getView().findViewById(id);

                    view.IsSelected = false;
                    celldata[view.Y][view.X].IsSelected = false;

                    if(view.IsError) continue;

                    if(view.IsDisabled) {
                        view.setBackgroundResource(R.drawable.disable_cell_style);
                    }
                    else if(isHightlightCells && (view.X == cv.X || view.Y == cv.Y || (x/3 == cv.X/3 && y/3 == cv.Y/3))) {
                        view.setBackgroundResource(R.drawable.selected_dependent_style);
                    }
                    else
                        view.setBackgroundResource(R.drawable.normal_cell_style);

                    if(isHightlightNumbers && cv.CurrentValue == view.CurrentValue)
                        view.setTextColor(getResources().getColor(R.color.red));
                    else
                        view.setTextColor(getResources().getColor(R.color.black));

                    //UpdateActualStore(view);
                }

            if(!cv.IsError) {
                if(!cv.IsDisabled)
                    cv.setBackgroundResource(R.drawable.selected_style);
                celldata[cv.Y][cv.X].IsSelected = true;
                //UpdateActualStore(cv);
            }
            cv.IsSelected = true;
        }

        private void SetHeightWidth(int cellSize)
        {
            float textSize = (float)((cellSize/2.0) - (cellSize/10.0));
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView view = (CustomTextView)getView().findViewById(id);

                    view.setMinimumWidth(cellSize);
                    view.setMinimumHeight(cellSize);
                    view.setMaxWidth(cellSize);
                    view.setMaxHeight(cellSize);
                    //view.setTextSize(textSize);
                }

            ImageButton view = (ImageButton)getView().findViewById(R.id.undo);
            view.setMinimumWidth(cellSize);
            view.setMinimumHeight(cellSize);
            view.setMaxWidth(cellSize);
            view.setMaxHeight(cellSize);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Undo();
                }
            });

            view = (ImageButton)getView().findViewById(R.id.redo);
            view.setMinimumWidth(cellSize);
            view.setMinimumHeight(cellSize);
            view.setMaxWidth(cellSize);
            view.setMaxHeight(cellSize);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Redo();
                }
            });
        }

        private void Undo()
        {
            if(history.size() == 0)
            {
                Toast.makeText(getActivity(), "Nothing to Undo!", Toast.LENGTH_LONG).show();
                return;
            }

            CellInfo info = history.removeLast();
            if(info == null) return;

            String idStr = "id/" + "Row" + info.Y + "Col" + info.X;
            int id = getResourceId(idStr, null, getString(R.string.package_name));
            CustomTextView view = (CustomTextView) getView().findViewById(id);

            redoHistory.addLast(celldata[info.Y][info.X].Clone());

            view.CurrentValue = info.CurrentValue;
            view.IsError = info.IsError;
            view.setBackgroundResource(R.drawable.normal_cell_style);

            if(info.CurrentValue == 0)
                view.setText("");
            else
                view.setText("" + info.CurrentValue);

            if(view.IsError) {
                view.setBackgroundResource(R.drawable.number_exists_error_style);
                UpdateValidityStatus(false);
            }
            else {
                UpdateValidityStatus(false);
            }

            UpdateActualStore(view);
        }

        private void Redo()
        {
            if(redoHistory.size() == 0)
            {
                Toast.makeText(getActivity(), "Nothing to Redo!", Toast.LENGTH_SHORT).show();
                return;
            }

            CellInfo info = redoHistory.removeLast();
            if(info == null) return;

            String idStr = "id/" + "Row" + info.Y + "Col" + info.X;
            int id = getResourceId(idStr, null, getString(R.string.package_name));
            CustomTextView view = (CustomTextView) getView().findViewById(id);

            history.addLast(celldata[info.Y][info.X].Clone());

            view.CurrentValue = info.CurrentValue;
            view.IsError = info.IsError;
            view.setBackgroundResource(R.drawable.normal_cell_style);

            if(info.CurrentValue == 0)
                view.setText("");
            else
                view.setText("" + info.CurrentValue);

            if(view.IsError) {
                view.setBackgroundResource(R.drawable.number_exists_error_style);
                UpdateValidityStatus(false);
            }
            else
                UpdateValidityStatus(false);

            UpdateActualStore(view);
        }

        private void SetDimensionToNumbers(int cellSize)
        {
            for(int x = 1; x <= 9; x++)
            {
                String idStr = "id/" + "Num" + x;
                int id = getResourceId(idStr, null, getString(R.string.package_name));
                TextView view = (TextView)getView().findViewById(id);

                view.setMinimumWidth(cellSize);
                view.setMinimumHeight(cellSize);
                view.setMaxWidth(cellSize);
                view.setMaxHeight(cellSize);
                //view.setTextSize(cellSize/2 - cellSize/10);

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            if(fillBoardPreference == 1) {
                                v.setBackgroundResource(R.drawable.selected_number_style);
                            }

                            SelectNumber(v);

                            if(fillBoardPreference == 1) {
                                v.setBackgroundResource(R.drawable.button_style);
                            }
                        }
                        return true;
                    }

                });
            }

            ImageButton view = (ImageButton)getView().findViewById(R.id.Erase);
            view.setMinimumWidth(cellSize);
            view.setMinimumHeight(cellSize);
            view.setMaxWidth(cellSize);
            view.setMaxHeight(cellSize);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(fillBoardPreference == 1) {
                        v.setBackgroundResource(R.drawable.selected_number_style);
                    }

                    SelectEraser(v);

                    if(fillBoardPreference == 1) {
                        v.setBackgroundResource(R.drawable.button_style);
                    }
                }

            });
        }

        private void SelectNumber(View v) {

            TextView tv = (TextView) v;

            if(fillBoardPreference == 2) {
                if(selectedNumber == Integer.parseInt(tv.getText().toString()))
                {
                    tv.setBackgroundResource(R.drawable.button_style);
                    tv.setTextColor(getResources().getColor(R.color.black));
                    selectedNumber = -1;
                    return;
                }
                int eraseId = getResourceId("id/Erase", null, getString(R.string.package_name));
                ImageButton erase = (ImageButton) getView().findViewById(eraseId);
                erase.setBackgroundResource(R.drawable.button_style);
                for (int x = 1; x <= 9; x++) {
                    String idStr = "id/" + "Num" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    TextView view = (TextView) getView().findViewById(id);
                    view.setTextColor(getResources().getColor(R.color.black));
                    view.setBackgroundResource(R.drawable.button_style);
                }
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.selected_number_style);
            }

            selectedNumber = Integer.parseInt(tv.getText().toString());

            if(selectedCell == null || selectedCell.IsDisabled) return;

            if(fillBoardPreference == 1) {
                AddToHistory(selectedCell.Y, selectedCell.X);
                UpdateSelectedCell(tv.getText().toString());

                if (isInstantValidation) {
                    InvokeValidation(selectedCell);
                    UpdateValidityStatus(true);
                }
            }
        }

        private void SelectEraser(View v)
        {
            if(fillBoardPreference == 2) {
                if(selectedNumber == 0)
                {
                    v.setBackgroundResource(R.drawable.button_style);
                    selectedNumber = -1;
                    return;
                }

                ImageButton eraser = (ImageButton) v;
                eraser.setBackgroundResource(R.drawable.selected_number_style);
                for (int x = 1; x <= 9; x++) {
                    String idStr = "id/" + "Num" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    TextView view = (TextView) getView().findViewById(id);
                    view.setTextColor(getResources().getColor(R.color.black));
                    view.setBackgroundResource(R.drawable.button_style);
                }
            }

            selectedNumber = 0;

            if(selectedCell == null || selectedCell.IsDisabled) return;

            if(fillBoardPreference == 1) {
                AddToHistory(selectedCell.Y, selectedCell.X);
                selectedCell.setText("");
                selectedCell.CurrentValue = 0;
                selectedCell.IsError = false;
                selectedCell.setBackgroundResource(R.drawable.selected_style);
                UpdateActualStore(selectedCell);

                s.SetData(GetData());
                InvokeValidation(selectedCell);
                UpdateValidityStatus(false);
            }
        }

        private void UpdateSelectedCell(String text)
        {
            if(text == "" || Integer.parseInt(text)== 0) {
                selectedCell.CurrentValue = 0;
                selectedCell.setText("");
            }
            else {
                selectedCell.CurrentValue = Integer.parseInt(text);
                selectedCell.setText(text);
            }

            selectedCell.UserEntered = true;
            selectedCell.IsError = false;
            selectedCell.setBackgroundResource(R.drawable.selected_style);

            UpdateActualStore(selectedCell);
        }

        private void UpdateActualStore(CustomTextView view)
        {
            celldata[view.Y][view.X].X = view.X;
            celldata[view.Y][view.X].Y = view.Y;
            celldata[view.Y][view.X].CurrentValue = view.CurrentValue;
            celldata[view.Y][view.X].UserEntered = view.UserEntered;
            celldata[view.Y][view.X].IsDisabled = view.IsDisabled;
            celldata[view.Y][view.X].IsError = view.IsError;
            celldata[view.Y][view.X].TextColor = view.getTextColors();

            s.SetData(GetData());
        }

        private void AddToHistory(int x, int y)
        {
            redoHistory.clear();
            history.addLast(celldata[x][y].Clone());
            if(history.size() > 100)
                history.removeFirst();
        }

        private void AddToRedoHistory(int x, int y)
        {
            redoHistory.addLast(celldata[x][y].Clone());
        }

        private void UpdateUI(CustomTextView view, int x, int y)
        {
            view.CurrentValue = celldata[y][x].CurrentValue;
            view.IsDisabled = celldata[y][x].IsDisabled;
            view.IsError = celldata[y][x].IsError;
            view.UserEntered = celldata[y][x].UserEntered;
            view.setTextColor(celldata[y][x].TextColor);
            view.IsSelected = celldata[y][x].IsSelected;
            view.X = celldata[y][x].X;
            view.Y = celldata[y][x].Y;

            if(celldata[y][x].CurrentValue > 0)
                view.setText("" + celldata[y][x].CurrentValue);
            else
                view.setText("");

            view.setBackgroundResource(R.drawable.normal_cell_style);
            if(view.IsDisabled)
                view.setBackgroundResource(R.drawable.disable_cell_style);
            else if(view.IsError)
                view.setBackgroundResource(R.drawable.number_exists_error_style);
            else
                view.setBackgroundResource(R.drawable.normal_cell_style);

            if(view.IsSelected) {
                if(!view.IsError && !view.IsDisabled)
                    view.setBackgroundResource(R.drawable.selected_style);
            }

            if(celldata[y][x].IsSelected)
                selectedCell = view;

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        CustomTextView view = (CustomTextView) v;
                        SelectDependentCellOnTouch(view);
                    }
                    return true;
                }

            });

            UpdateValidityStatus(false);
        }

        private boolean InvokeValidation()
        {
            boolean isValid = true;
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {
                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView cv = (CustomTextView)getView().findViewById(id);
                    if(cv.IsDisabled || cv.CurrentValue == 0) continue;
                    if(!InvokeValidation(cv)) {
                        isValid = false;
                    }
                }

            UpdateValidityStatus(true);

            return isValid;
        }

        private boolean InvokeValidation(CustomTextView view) {

            if(view == null) return true;

            boolean isValid = true;
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    if(x == view.X && y == view.Y) continue;

                    String idStr = "id/" + "Row" + y + "Col" + x;
                    int id = getResourceId(idStr, null, getString(R.string.package_name));
                    CustomTextView cv = (CustomTextView)getView().findViewById(id);

                    if(isHightlightNumbers && cv.CurrentValue > 0 && cv.CurrentValue == view.CurrentValue)
                        cv.setTextColor(getResources().getColor(R.color.red));
                    else
                        cv.setTextColor(getResources().getColor(R.color.black));

                    if(view.X == x || view.Y == y || (x/3 == view.X/3 && y/3 == view.Y/3)) {

                        //cv.IsError = cv.IsError ? true : false;
                        if(cv.CurrentValue > 0) {

                            if(cv.CurrentValue == view.CurrentValue) {
                                if (!cv.IsDisabled) {
                                    cv.setBackgroundResource(R.drawable.number_exists_error_style);
                                    cv.IsError = true;
                                }

                                view.setBackgroundResource(R.drawable.number_exists_error_style);
                                view.IsError = true;
                                isValid = false;
                            }
                            else if(cv.IsError && !celldata[cv.Y][cv.X].IsSelected) {
                                cv.IsError = false;
                                if(InvokeValidation(cv)) {
                                    cv.setBackgroundResource(R.drawable.selected_dependent_style);
                                    cv.IsError = false;
                                    UpdateActualStore(cv);
                                }
                                else{
                                    isValid = false;
                                    cv.IsError = true;
                                    UpdateActualStore(cv);
                                }
                            }
                        }
                    }

                    UpdateActualStore(cv);
                }

            UpdateActualStore(view);

            return isValid;
        }

        private boolean CheckForPuzzleCompletion()
        {
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    if(celldata[y][x].CurrentValue == 0)
                        return false;
                }

            return true;
        }

        private void UpdateValidityStatus( boolean checkForCompletion)
        {
            boolean isValid = CheckValidityStatus();

            if(!isValid) {
                IsValid = false;
                validityStatus.setText("INVALID");
                validityStatus.setTextColor(getResources().getColor(R.color.red));
            }
            else {
                IsValid = true;
                validityStatus.setText("VALID");
                validityStatus.setTextColor(getResources().getColor(R.color.green));
                if(checkForCompletion) {
                    if (CheckForPuzzleCompletion()) {
                        PauseTimer();
                        timerString = null;
                        validityStatus.setText("DONE!!");
                        ShowCompletedMessage("Bravo!!", "Congratulation. You solved it!!");
                    }
                }
            }
        }

        private boolean CheckValidityStatus()
        {
            for(int y = 0; y < 9; y++)
                for(int x = 0; x < 9; x++) {

                    if(celldata[y][x].IsError)
                        return false;
                }

            return true;

        }

        public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
        {
            try {
                return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MyActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private void ShowCompletedMessage(String caption, String msg)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(caption)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NewGame(gameType);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        private void CheckForSaveGame()
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Save ??")
                    .setMessage("Do you want to save the game?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        public void HideTimer()
        {
            timeSpent.setVisibility(View.INVISIBLE);
        }

        public void ShowTimer()
        {
            timeSpent.setVisibility(View.VISIBLE);
        }

        public void StopValidation()
        {
            isInstantValidation = false;
        }

        public void StartValidation()
        {
            isInstantValidation = true;
            //InvokeValidation(selectedCell);
        }

        public void NonHighlightCells()
        {
            isHightlightCells = false;
            SelectDependentCellOnTouch(selectedCell);
        }

        public void HighlightCells()
        {
            isHightlightCells = true;

            SelectDependentCellOnTouch(selectedCell);
        }

        public void NonHighlightNumbers()
        {
            isHightlightNumbers = false;
            InvokeValidation(selectedCell);
        }

        public void HighlightNumbers()
        {
            isHightlightNumbers = true;
            InvokeValidation(selectedCell);
        }
    }
}
