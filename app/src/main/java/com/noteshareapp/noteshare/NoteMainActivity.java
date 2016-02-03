package com.noteshareapp.noteshare;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lassana.recorder.AudioRecorderBuilder;
import com.noteshareapp.db.Note;
import com.noteshareapp.db.NoteElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class NoteMainActivity extends DrawerActivity implements OnClickListener {
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_CAMERA = 2;
    public static String noteIdForDetails;
    static String backgroundColor = "#FFFFFF";
    final Context context = this;
    public ImageButton imageButtonHamburg, imageButtoncalander,
            imageButtonsquence, imageButtoncheckbox;
    public ImageButton imageButtonTextMode, imageButtonImageMode,
            imageButtonPaintMode, imageButtonAudioMode, imageButtonShareMode,
            imageButtonDeleteMode, imageButtonMoreMode;
    public SpannableString spanUpdted, spanold;
    public int typefacae = Typeface.NORMAL;
    public TextView progressRecordtext;
    public RelativeLayout layoutHeader;
    public int currentAudioIndex = 0;
    public LinearLayout noteElements;
    public RelativeLayout noteScribbleElements;
    public ImageButton deleteCheckbox, deleteAudio;
    public LinearLayout layOutDrawingView, textNoteControls, bottommenue,
            layout_note_more_Info, layout_audio_notechooser, audioElement;
    public TextView textViewAdd, textViewDuration;
    public ListView listviewNotes;
    public DrawingView drawView;
    public Dialog dialogColor;
    public Dialog move;
    public Dialog brushDialog1;
    public int currentFontSize;
    public String currentFontTypeface;
    public int currentFontColor;
    public LinearLayout drawingControls;
    public RelativeLayout LayoutTextWritingView;
    public HorizontalScrollView horizontal_scroll_editor;
    public ScrollView scrollView;
    public ImageButton imageButtondrawback, imageButtondrawnew,
            imageButtonbrushdraw, imageButtondrawcolors,
            imageButtonhighlightdraw, imageButtondrawerase,
            imageButtondrawMore;
    public ImageButton audioButtondrawback, audioButtondrawnew;
    public boolean isMoreShown = false;
    public boolean isTextmodeSelected = false;
    public boolean isDeleteModeSelected = false;
    public String[] fonts_sizeName, fonts_Name_Display, arrStrings;
    public String[] fontSizes;
    public String[] editortext = new String[1];
    public List<View> textelementid = new ArrayList<View>();
    public String[] noteTitle = new String[1];
    public String[] noteCheckText = new String[1];
    MediaPlayer mediaPlayer;// = new MediaPlayer();
    EditText textViewheaderTitle;
    List<ImageView> listImage = new ArrayList<ImageView>();
    //Color picker
    int color_selected = -16777216;
    EditText edittextEditer, txtViewer;
    Button btnAddText;
    ImageButton buttonPlay;
    ImageButton buttonStop;
    ImageButton buttonRecord, buttonPause, buttonRecordPause;
    public long noteElementId;
    ProgressBar progressRecord;
    SeekBar progressRecord1;
    public NoteFunctions noteFunctions = new NoteFunctions();
    // /Drawing Controls
    RelativeLayout LayoutAudioRecording;
    ImageButton bold = null, italic = null, underline = null, h1 = null, h2 = null, h3 = null, h4 = null, h5 = null, h6 = null, align_left = null, align_center = null, align_right = null, redo = null, undo = null;
    ImageButton orderedList = null, unorderedList = null;
    View viewText;
    List<RichEditor> allRe = new ArrayList<RichEditor>();
    List<View> allCheckboxText = new ArrayList<View>();
    List<View> allDelete = new ArrayList<View>();
    boolean isErase;
    boolean isPaintMode = false;
    boolean isUnderLine = false, isBold = false, isItalic = false;
    boolean isRecordingAudio = false, recordingPlay = false;
    View contentView;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentDateStr = formatter.format(new Date());
    RelativeLayout background_bg;
    Uri mMediaUri;
    Dialog scribbleDialog;
    SeekBar sizeEraserSeekBar;
    TextView tvEraserSize;
    View highlightview, brushview, eraserview;
    ImageButton buttonHighlight, buttonBrush, buttonEraser;
    GradientDrawable brushshape;
    // 8b241b selected bg
    int lastBrushSize = 3, lastHighlightSize = 3, lastEraserSize = 3,
            highlightViewSize = (lastHighlightSize * 4 + 20),
            brushViewSize = (lastBrushSize * 4 + 20),
            eraserViewSize = (lastEraserSize * 4 + 20),
            lastBrushColor = 0, count = 0;
    int firstHighlightColor, lastHighlightColor = Color.parseColor("#77FF5B1E");
    int orderNumber = 1;
    private float smallBrush, mediumBrush, largeBrush;
    private AudioRecorder mAudioRecorder;
    private String audioName;
    private String mActiveRecordFileName;
    private ImageButton currPaint;
    private ImageView imageView53;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;


    //audio stopwatch
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    public TextView audio_text;
    //multiple delete
    List<String> multipleDeleteArray = new ArrayList<String>();
    List<View> multipleDeleteParentArray = new ArrayList<View>();
    //note element order and spacing
    boolean scribbleAdded = false;
    public static String background;
    boolean outsideNote = false;
    boolean out = false;

    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        StringBuffer sb = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        sb.append(String.format("%02d", hours)).append(":")
                .append(String.format("%02d", minutes)).append(":")
                .append(String.format("%02d", seconds));

        System.out.println("time is:" + sb.toString());

        return sb.toString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Intent intent = this.getIntent();
        noteIdForDetails = intent.getStringExtra("NoteId");
        out = intent.getBooleanExtra("Outside", false);

        Log.v("select", "onCreate Note Id" + noteIdForDetails);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        contentView = inflater
                .inflate(R.layout.note_activity_main, null, false);
        mDrawerLayout.addView(contentView, 0);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initlizeUIElement(contentView);
        try {
            fetchNoteElementsFromDb();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (out) {
            screenshot();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initlizeUIElement(contentView);
        try {
            fetchNoteElementsFromDb();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void initlizeUIElement(View contentview) {

        scrollView = (ScrollView) contentview.findViewById(R.id.scrollView);

        noteElements = (LinearLayout) findViewById(R.id.noteElements);


	/* Default Initlization */
        currentFontSize = 8;
        currentFontColor = Color.BLACK;

        bottommenue = (LinearLayout) findViewById(R.id.bottommenue);

        background_bg = (RelativeLayout) contentview
                .findViewById(R.id.background_bg);
        layoutHeader = (RelativeLayout) contentview
                .findViewById(R.id.mainHeadermenue);
        imageButtoncalander = (ImageButton) layoutHeader
                .findViewById(R.id.imageButtoncalander);
        imageButtoncheckbox = (ImageButton) layoutHeader
                .findViewById(R.id.imageButtoncheckbox);

        textViewheaderTitle = (EditText) layoutHeader
                .findViewById(R.id.textViewheaderTitle);
        textViewheaderTitle.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        imageButtonHamburg = (ImageButton) layoutHeader
                .findViewById(R.id.imageButtonHamburg);
        imageButtonsquence = (ImageButton) layoutHeader
                .findViewById(R.id.imageButtonsquence);

        imageButtonHamburg.setImageResource(R.drawable.back_icon_1);
        imageButtoncalander.setImageResource(R.drawable.done_icon);

        horizontal_scroll_editor = (HorizontalScrollView) contentview.findViewById(R.id.horizontal_scroll_editor);
        horizontal_scroll_editor.setVisibility(View.GONE);

        // audio controls
        //audioRecording(contentView);
        initlizesAudioNoteControls(contentview);

        // MoreInfo View

        initlizesMoreInfoView(contentview);

        // scribble controls
        initlizesScibbleNoteControles(contentview);

        // text note controls

        initlizesTextNoteControls(contentview);

        // Main controls

        imageButtonAudioMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonAudioMode);
        imageButtonImageMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonImageMode);
        imageButtonPaintMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonPaintMode);
        imageButtonShareMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonShareMode);
        imageButtonTextMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonTextMode);
        imageButtonMoreMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonMoreMode);
        imageButtonDeleteMode = (ImageButton) contentview
                .findViewById(R.id.imageButtonDeleteMode);

        final String[] updatedText = new String[1];
        if (noteIdForDetails == null) {
            List<Note> notes = Note.findWithQuery(Note.class, "Select id from NOTE");
            String num = null;
            if (notes.size() > 0)
                num = String.valueOf(((notes.get(notes.size() - 1).getId()) + 1L));
            else
                num = "1";
            textViewheaderTitle.setText("Note " + num + "");
            updatedText[0] = "Note " + num;
            noteTitle[0] = "Note " + num;
        }

        final boolean[] initialNameSet = {false};

        textViewheaderTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                drawingControls.setVisibility(View.GONE);
                layout_note_more_Info.setVisibility(View.GONE);
                //isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                horizontal_scroll_editor.setVisibility(View.GONE);

                bottommenue.setVisibility(View.GONE);
                imageButtonHamburg.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.VISIBLE);
                imageButtonsquence.setVisibility(View.GONE);
                imageButtoncheckbox.setVisibility(View.GONE);
            }
        });

        textViewheaderTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (noteIdForDetails == null && initialNameSet[0]) {
                    makeNote();
                }

                updatedText[0] = s.toString();

                if (!noteTitle[0].equals(updatedText[0]) && initialNameSet[0]) {
                    Note note = Note.findById(Note.class, Long.parseLong(noteIdForDetails));
                    note.title = updatedText[0];
                    //note.modificationtime = currentDateStr;
                    note.save();
                    modifyNoteTime();
                    noteTitle[0] = updatedText[0];
                }

                initialNameSet[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        /** Layout Audio Recording **/

        addlistners();
        addScribbleControlListners();
        addTextNoteControlsListners();
        addAudioNoteListners();

        updateHeaderControls(R.id.imageButtonHamburg);
        imageButtonsquence.setVisibility(View.VISIBLE);
        //imageButtoncalander.setVisibility(View.VISIBLE);

        fonts_sizeName = getResources().getStringArray(R.array.Font_Size_px);
        fontSizes = getResources().getStringArray(R.array.Font_Size);
        fonts_Name_Display = getResources().getStringArray(
                R.array.Font_Name_Display);

    }

    void initlizesAudioNoteControls(View contentview) {
        layout_audio_notechooser = (LinearLayout) contentview
                .findViewById(R.id.audioControls);
        layout_audio_notechooser.setVisibility(View.GONE);
        audioButtondrawback = (ImageButton) layout_audio_notechooser
                .findViewById(R.id.imageButtondrawback);
        audioButtondrawnew = (ImageButton) layout_audio_notechooser
                .findViewById(R.id.imageButtondrawnew);
        updateAudioNoteUI(R.id.imageButtondrawback);
        audioButtondrawback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonUI(-1);
            }
        });
    }

    void updateAudioNoteUI(int elementId) {

        audioButtondrawback.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        audioButtondrawnew.setBackgroundColor(getResources().getColor(
                R.color.header_bg));

        switch (elementId) {
            case R.id.imageButtondrawback:
                audioButtondrawback.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtondrawnew:
                audioButtondrawnew.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;

            default:
                break;
        }

    }

    void addAudioNoteListners() {

        audioButtondrawback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                updateAudioNoteUI(arg0.getId());
                layout_audio_notechooser.setVisibility(View.GONE);
            }
        });

        audioButtondrawnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                LayoutAudioRecording.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.GONE);
                buttonRecordPause.setVisibility(View.GONE);
                buttonPause.setVisibility(View.GONE);

                buttonRecord.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                progressRecord1.setVisibility(View.GONE);
                textViewDuration.setVisibility(View.GONE);
                buttonRecord.setEnabled(true);
                buttonStop.setEnabled(true);

                initlizeAudiorecoder();

                updateAudioNoteUI(arg0.getId());
            }
        });
    }

    void initlizeAudiorecoder() {
        //SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
        String timestamp = String.valueOf(System.currentTimeMillis());
        FileNameGenerator fileNameGenerator = new FileNameGenerator();
        audioName = fileNameGenerator.getFileName("AUDIO");

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NoteShare/NoteShare Audio/" + audioName;

        myAudioRecorder = new MediaRecorder();

        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        myAudioRecorder.setAudioSamplingRate(44100);
        myAudioRecorder.setAudioEncodingBitRate(320000);
        myAudioRecorder.setAudioChannels(2);

        myAudioRecorder.setOutputFile(outputFile);
    }

    void initlizesTextNoteControls(View contentview) {

        textNoteControls = (LinearLayout) contentview.findViewById(R.id.textNoteControls);
        textNoteControls.setVisibility(View.GONE);

        // bold italic underline ol ul h1 h2 h3 h4 h5 h6 align_left align_center align_right redo undo
        bold = (ImageButton) findViewById(R.id.action_bold);
        italic = (ImageButton) findViewById(R.id.action_italic);
        underline = (ImageButton) findViewById(R.id.action_underline);
        orderedList = (ImageButton) findViewById(R.id.action_ordered);
        unorderedList = (ImageButton) findViewById(R.id.action_unordered);
        h1 = (ImageButton) findViewById(R.id.action_heading1);
        h2 = (ImageButton) findViewById(R.id.action_heading2);
        h3 = (ImageButton) findViewById(R.id.action_heading3);
        h4 = (ImageButton) findViewById(R.id.action_heading4);
        h5 = (ImageButton) findViewById(R.id.action_heading5);
        h6 = (ImageButton) findViewById(R.id.action_heading6);
        align_left = (ImageButton) findViewById(R.id.action_align_left);
        align_center = (ImageButton) findViewById(R.id.action_align_center);
        align_right = (ImageButton) findViewById(R.id.action_align_right);
        redo = (ImageButton) findViewById(R.id.action_redo);
        undo = (ImageButton) findViewById(R.id.action_undo);

    }

    void initlizesScibbleNoteControles(View contentview) {

        layOutDrawingView = (LinearLayout) contentview
                .findViewById(R.id.layOutDrawingView);
        drawView = (DrawingView) layOutDrawingView
                .findViewById(R.id.viewScibble);

        layOutDrawingView.setVisibility(View.GONE);

        drawingControls = (LinearLayout) contentview
                .findViewById(R.id.drawingControls);
        drawingControls.setVisibility(View.GONE);

        imageButtondrawback = (ImageButton) drawingControls
                .findViewById(R.id.imageButtondrawback);
        imageButtondrawnew = (ImageButton) drawingControls
                .findViewById(R.id.imageButtondrawnew);
        imageButtonbrushdraw = (ImageButton) drawingControls
                .findViewById(R.id.imageButtonbrushdraw);
        /*imageButtondrawcolors = (ImageButton) drawingControls
                .findViewById(R.id.imageButtondrawcolors);*/
        imageButtonhighlightdraw = (ImageButton) drawingControls
                .findViewById(R.id.imageButtonhighlightdraw);
        imageButtondrawerase = (ImageButton) drawingControls
                .findViewById(R.id.imageButtondrawerase);
        /*imageButtondrawMore = (ImageButton) drawingControls
                .findViewById(R.id.imageButtondrawMore);*/

    }

    void addTextNoteControlsListners() {

        // redo
        redo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).redo();
            }
        });

        // undo
        undo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).undo();
            }
        });

        // bold
        bold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setBold();
            }
        });

        // italic
        italic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setItalic();
            }
        });

        // underline
        underline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setUnderline();
            }
        });

        // ordered list
        orderedList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setOrderedList();
            }
        });

        // unordered list
        unorderedList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setUnorderedList();
            }
        });

        // h1
        h1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(1);
            }
        });

        // h2
        h2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(2);
            }
        });

        // h3
        h3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(3);
            }
        });

        // h4
        h4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(4);
            }
        });

        // h5
        h5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(5);
            }
        });

        // h6
        h6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setHeading(6);
            }
        });

        // align left
        align_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setAlignLeft();
            }
        });

        // align center
        align_center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setAlignCenter();
            }
        });

        // align right
        align_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allRe.get(Integer.parseInt(v.getTag().toString())).setAlignRight();
            }
        });

    }

    void addScribbleControlListners() {
        imageButtondrawback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScribbleControlListners(v.getId());

                /*if (drawView.getUserDrawn() == true) {
                    //SaveDrawingDialog();
                    isPaintMode = false;
                    imageButtonHamburg.setVisibility(View.VISIBLE);
                    imageButtoncalander.setVisibility(View.GONE);
                    imageButtonsquence.setVisibility(View.VISIBLE);
                    imageButtoncheckbox.setVisibility(View.VISIBLE);
                    bottommenue.setVisibility(View.VISIBLE);
                } else {*/

                drawingControls.setVisibility(View.GONE);
                layOutDrawingView.setVisibility(View.GONE);

                isPaintMode = false;
                imageButtonHamburg.setVisibility(View.VISIBLE);
                imageButtoncalander.setVisibility(View.GONE);
                imageButtonsquence.setVisibility(View.VISIBLE);
                imageButtoncheckbox.setVisibility(View.VISIBLE);
                bottommenue.setVisibility(View.VISIBLE);
                updateButtonUI(-1);

                drawView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int x = (int) event.getX();
                        int y = (int) event.getX();


                        Log.e("jay x", String.valueOf(x));
                        Log.e("jay y", String.valueOf(y));

                        return false;
                    }
                });

                //}

            }
        });
        imageButtondrawnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDrawingDialog();

            }
        });
        imageButtonbrushdraw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScribbleControlListners(v.getId());
                showScribbleDialog("brush");
                openBrush();
                updateScribbleButtonColor("brush");
            }
        });
        imageButtonhighlightdraw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScribbleControlListners(v.getId());
                showScribbleDialog("highlight");
                openHighlight();
                updateScribbleButtonColor("highlight");
            }
        });
        imageButtondrawerase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScribbleControlListners(v.getId());
                showScribbleDialog("eraser");
                openEraser();
                updateScribbleButtonColor("eraser");
            }
        });
    }

    /*void updateTextNoteControlListners(int elementId) {

        bold.setBackgroundColor(getResources().getColor(R.color.header_bg));
        italic.setBackgroundColor(getResources().getColor(R.color.header_bg));
        underline.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h1.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h2.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h3.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h4.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h5.setBackgroundColor(getResources().getColor(R.color.header_bg));
        h6.setBackgroundColor(getResources().getColor(R.color.header_bg));
        align_left.setBackgroundColor(getResources().getColor(R.color.header_bg));
        align_center.setBackgroundColor(getResources().getColor(R.color.header_bg));
        align_right.setBackgroundColor(getResources().getColor(R.color.header_bg));
        redo.setBackgroundColor(getResources().getColor(R.color.header_bg));
        undo.setBackgroundColor(getResources().getColor(R.color.header_bg));

        switch (elementId) {
            case R.id.action_bold:
                bold.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_italic:
                italic.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_underline:
                underline.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading1:
                h1.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading2:
                h2.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading3:
                h3.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading4:
                h4.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading5:
                h5.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_heading6:
                h6.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_align_left:
                align_left.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_align_center:
                align_center.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_align_right:
                align_right.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_redo:
                redo.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            case R.id.action_undo:
                undo.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                break;
            default:
                break;
        }

    }*/

    void updateScribbleControlListners(int elementId) {

        imageButtondrawback.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtondrawnew.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonbrushdraw.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        /*imageButtondrawcolors.setBackgroundColor(getResources().getColor(
                R.color.header_bg));*/
        imageButtonhighlightdraw.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtondrawerase.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        /*imageButtondrawMore.setBackgroundColor(getResources().getColor(
                R.color.header_bg));*/

        switch (elementId) {
            case R.id.imageButtondrawback:
                imageButtondrawback.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtondrawnew:
                imageButtondrawnew.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtondrawdraw:
                imageButtonbrushdraw.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtondrawcolors:
                imageButtondrawcolors.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtonhighlightdraw:
                imageButtonhighlightdraw.setBackgroundColor(getResources()
                        .getColor(R.color.A8b241b));
                break;
            case R.id.imageButtondrawerase:
                imageButtondrawerase.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtondrawMore:
                imageButtondrawMore.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;

            default:
                break;
        }

    }

    void updateHeaderControls(int itemId) {
        imageButtonHamburg.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtoncalander.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonsquence.setBackgroundColor(getResources().getColor(
                R.color.header_bg));

        switch (itemId) {
            case R.id.imageButtonHamburg:
                imageButtonHamburg.setBackgroundColor(getResources().getColor(
                        R.color.header_bg));
                break;
            case R.id.imageButtoncalander:
                imageButtoncalander.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;
            case R.id.imageButtonsquence:
                imageButtonsquence.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
                break;

            default:
                break;
        }

    }

    /*************
     * moreinfo control Here
     ************/
    Button buttonLock;

    void initlizesMoreInfoView(View contentView) {
        layout_note_more_Info = (LinearLayout) contentView
                .findViewById(R.id.layout_note_more_Info);
        layout_note_more_Info.setVisibility(View.GONE);

	/*Button buttonLock = (Button) layout_note_more_Info
    .findViewById(R.id.buttonLock);
	Button buttonDelete = (Button) layout_note_more_Info
	.findViewById(R.id.buttonDelete);*/
        buttonLock = (Button) layout_note_more_Info.findViewById(R.id.buttonLock);
        Button buttonTimebomb = (Button) layout_note_more_Info.findViewById(R.id.buttonTimebomb);
        Button buttonRemind = (Button) layout_note_more_Info.findViewById(R.id.buttonRemind);
        Button buttonMove = (Button) layout_note_more_Info.findViewById(R.id.buttonMove);
        Button buttonDelete = (Button) layout_note_more_Info.findViewById(R.id.buttonDelete);
        Button buttonShare = (Button) layout_note_more_Info.findViewById(R.id.buttonShare);
    }

    public void footerMenuGone() {
        isMoreShown = false;
        layout_note_more_Info.setVisibility(View.GONE);
        imageButtonMoreMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
    }

    public void share(View v) {
        noteFunctions.share(this, noteIdForDetails, outsideNote);
        footerMenuGone();
    }

    public void move(View v) {
        if (noteIdForDetails == null)
            makeNote();
        noteFunctions.showMoveAlert(this, noteIdForDetails);
        footerMenuGone();
    }

    public void timebomb(View v) {
        if (noteIdForDetails == null)
            makeNote();
        noteFunctions.showDate(this, noteIdForDetails, "SET TIMEBOMB", "timebomb");
        footerMenuGone();
    }

    public void remindClick(View v) {
        if (noteIdForDetails == null)
            makeNote();
        noteFunctions.showDate(this, noteIdForDetails, "SET REMAINDER", "reminder");
        footerMenuGone();
    }

    public void passcode(View v) {
        if (noteIdForDetails == null)
            makeNote();
        noteFunctions.setPasscode(this, noteIdForDetails);
        updateButtonUI(R.id.imageButtonMoreMode);

        footerMenuGone();
    }

    public void delete(View v) {
        if (noteIdForDetails == null)
            makeNote();

        noteFunctions.showDeleteAlert(this, noteIdForDetails, true);
        footerMenuGone();
        //context.startActivity(new Intent(context, MainActivity.class));

    }

    void addlistners() {

        //imageButtoncalander.setVisibility(View.VISIBLE); //changed by Jay

        imageButtoncalander.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_note_more_Info.setVisibility(View.GONE);
                imageButtonsquence.setVisibility(View.VISIBLE);
                isMoreShown = false;
                // Save click
                updateHeaderControls(v.getId());

                if (textelementid.size() > 0) {
                    textelementid.get(0).clearFocus();
                }
                if (allCheckboxText.size() > 0) {
                    allCheckboxText.get(0).clearFocus();
                }

                textNoteControls.setVisibility(View.GONE);
                horizontal_scroll_editor.setVisibility(View.GONE);
                isTextmodeSelected = false;

                textViewheaderTitle.clearFocus();

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }

                if (isPaintMode) {
                    //SaveDrawingDialog();
                    saveScribble();
                    isPaintMode = false;
                } else {
                    drawView.setVisibility(View.GONE);
                    drawingControls.setVisibility(View.GONE);
                }

                if (isDeleteModeSelected) {

                    Log.e("jay size", String.valueOf(multipleDeleteArray.size()));
                    imageButtonDeleteMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
                    for (int i = 0; i < allDelete.size(); i++) {
                        try {
                            allDelete.get(i).setVisibility(View.GONE);
                        } catch (NullPointerException npe) {
                            Log.e("jay ", Log.getStackTraceString(npe));
                        }
                    }

                    for (int i = 0; i < multipleDeleteArray.size(); i++) {
                        Log.e("jay iiiiii", String.valueOf(i));
                        deleteElements(multipleDeleteArray.get(i));
                        multipleDeleteParentArray.get(i).setVisibility(View.GONE);
                    }

                    //multipleDeleteFinal();
                    multipleDeleteArray.clear();
                    isDeleteModeSelected = false;
                    onResume();
                }

                //drawView.startNew();
                /*drawView.setVisibility(View.GONE);
                drawingControls.setVisibility(View.GONE);*/
                imageButtonDeleteMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
                imageButtonAudioMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
                imageButtonbrushdraw.setBackgroundColor(getResources().getColor(R.color.header_bg));
                imageButtonTextMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
                imageButtonHamburg.setVisibility(View.VISIBLE);
                imageButtoncalander.setVisibility(View.GONE);
                imageButtonsquence.setVisibility(View.VISIBLE);
                imageButtoncheckbox.setVisibility(View.VISIBLE);
                bottommenue.setVisibility(View.VISIBLE);
            }
        });

        imageButtonHamburg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // openSlideMenu();
                if (isRecordingAudio) {
                    Toast.makeText(getApplicationContext(), "Audio recording is going on!", Toast.LENGTH_LONG).show();
                } else {
                    isTextmodeSelected = false;
                    layout_note_more_Info.setVisibility(View.GONE);
                    imageButtonsquence.setVisibility(View.VISIBLE);
                    layout_audio_notechooser.setVisibility(View.GONE);
                    imageButtoncalander.setVisibility(View.GONE);
                    isMoreShown = false;
                    finish();
                }
            }
        });

        imageButtoncheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCheckBox();
            }
        });

        imageButtonsquence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTextmodeSelected == true) {
                    LayoutTextWritingView.setVisibility(View.VISIBLE);
                    imageButtoncalander.setVisibility(View.VISIBLE);
                }

                layout_note_more_Info.setVisibility(View.GONE);
                layout_audio_notechooser.setVisibility(View.GONE);
                isMoreShown = false;

                // Color ICON
                //updateHeaderControls(v.getId());

                // Show IN TEXT NOTE
                showTextNoteDialog();

            }
        });

        imageButtonDeleteMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton();

                drawingControls.setVisibility(View.GONE);
                layout_note_more_Info.setVisibility(View.GONE);
                //isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                horizontal_scroll_editor.setVisibility(View.GONE);

                bottommenue.setVisibility(View.GONE);
                imageButtonHamburg.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.VISIBLE);
                imageButtonsquence.setVisibility(View.GONE);
                imageButtoncheckbox.setVisibility(View.GONE);


            }
        });

        imageButtonAudioMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingControls.setVisibility(View.GONE);
                //updateButtonUI(R.id.imageButtonAudioMode);

                if (isRecordingAudio) {
                    Toast.makeText(getApplication(), "Already recording previous audio!", Toast.LENGTH_LONG).show();
                } else {

                    audioElement = (LinearLayout) findViewById(R.id.audioRecording);
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View viewAudio = inflater.inflate(R.layout.note_audio_recording, null, false);
                    LinearLayout note_audio = (LinearLayout) viewAudio.findViewById(R.id.note_audio_recording);
                    final ImageView audio_play = (ImageView) viewAudio.findViewById(R.id.audio_play);
                    audio_text = (TextView) viewAudio.findViewById(R.id.audio_text);
                    audio_text.setTypeface(RegularFunctions.getAgendaMediumFont(NoteMainActivity.this));

                    audio_play.setImageResource(R.drawable.ic_audio_record);
                    //final ImageButton audioDelete = (ImageButton) viewAudio.findViewById(R.id.deleteAudio);

                    final ImageView audio_stop = (ImageView) viewAudio.findViewById(R.id.audio_stop);

                    //allDelete.add(audioDelete);
                    NoteElement ne = null;
                    //long noteElementId = 0;
                    isRecordingAudio = true;
                    startTime = 0L;
                    timeSwap = 0L;
                    finalTime = 0L;

                    final TextView record_text = (TextView) viewAudio.findViewById(R.id.record_text);

                    FileNameGenerator fileNameGenerator = new FileNameGenerator();
                    audioName = fileNameGenerator.getFileName("AUDIO");
                    final com.github.lassana.recorder.AudioRecorder recorder = AudioRecorderBuilder.with(context)
                            .fileName(getNextFileName(audioName))
                            .config(com.github.lassana.recorder.AudioRecorder.MediaRecorderConfig.DEFAULT)
                            .loggable()
                            .build();

                    if (noteIdForDetails == null) {
                        makeNote();
                    }
                    if (noteIdForDetails != null) {
                        ne = new NoteElement(Long.parseLong(noteIdForDetails), getNoteElementOrderNumber(), "yes", "audio", audioName, "false", "");
                        ne.save();
                        noteElementId = ne.getId();
                        modifyNoteTime();
                    }
                    recorder.start(new com.github.lassana.recorder.AudioRecorder.OnStartListener() {
                        @Override
                        public void onStarted() {
                            // started
                            startTime = SystemClock.uptimeMillis();
                            myHandler.postDelayed(updateTimerMethod, 0);
                            Toast.makeText(NoteMainActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
                            audio_play.setImageResource(R.drawable.ic_audio_pause);
                            audio_play.startAnimation(AnimationUtils.loadAnimation(NoteMainActivity.this, R.anim.animation_pulse));
                            recordingPlay = true;
                        }

                        @Override
                        public void onException(Exception e) {
                            // error
                        }
                    });

                    audio_play.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (recordingPlay) {
                                //already playing and now pause
                                recorder.pause(new com.github.lassana.recorder.AudioRecorder.OnPauseListener() {
                                    @Override
                                    public void onPaused(String activeRecordFileName) {
                                        // paused
                                        timeSwap += timeInMillies;
                                        myHandler.removeCallbacks(updateTimerMethod);
                                        Toast.makeText(NoteMainActivity.this, "Paused", Toast.LENGTH_SHORT).show();
                                        audio_play.setImageResource(R.drawable.ic_audio_record);
                                        audio_play.clearAnimation();
                                        recordingPlay = false;
                                    }

                                    @Override
                                    public void onException(Exception e) {
                                        // error
                                    }
                                });
                            } else {
                                //pause and now start playing it again
                                recorder.start(new com.github.lassana.recorder.AudioRecorder.OnStartListener() {
                                    @Override
                                    public void onStarted() {
                                        // started
                                        startTime = SystemClock.uptimeMillis();
                                        myHandler.postDelayed(updateTimerMethod, 0);
                                        Toast.makeText(NoteMainActivity.this, "Play again", Toast.LENGTH_SHORT).show();
                                        audio_play.setImageResource(R.drawable.ic_audio_pause);
                                        audio_play.startAnimation(AnimationUtils.loadAnimation(NoteMainActivity.this, R.anim.animation_pulse));
                                        recordingPlay = true;
                                    }

                                    @Override
                                    public void onException(Exception e) {
                                        // error
                                    }
                                });
                            }

                        }
                    });

                    audio_stop.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //if user clicks on stop and recording is still playing
                            if (recordingPlay) {
                                recorder.pause(new com.github.lassana.recorder.AudioRecorder.OnPauseListener() {
                                    @Override
                                    public void onPaused(String activeRecordFileName) {
                                        // paused
                                        timeSwap += timeInMillies;
                                        myHandler.removeCallbacks(updateTimerMethod);
                                        audio_play.setImageResource(R.drawable.ic_audio_record);
                                        audio_play.clearAnimation();
                                        recordingPlay = false;

                                        saveRecording();
                                        //Toast.makeText(getApplicationContext(),"Paused and Stop",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onException(Exception e) {
                                        // error
                                    }
                                });
                            }

                            if (!recordingPlay) {
                                saveRecording();
                            }

                        }
                    });

                    audioElement.addView(note_audio);
                }
            }
        });
        imageButtonImageMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("image mode");
                //updateButtonUI(R.id.imageButtonImageMode);
                // listviewNotes.setScrollContainer(true);
                // startActivity(new
                // Intent(getApplicationContext(),ImageChooserActivity.class));
                isTextmodeSelected = false;
                drawingControls.setVisibility(View.GONE);
                layOutDrawingView.setVisibility(View.GONE);
                //showImageChooserAlertWith("", NoteMainActivity.this);
                //ToDo Image
                Intent intent = new Intent(NoteMainActivity.this, CameraActivity.class);
                if (noteIdForDetails == null)
                    intent.putExtra("isNoteIdNull", true);
                else
                    intent.putExtra("isNoteIdNull", false);

                intent.putExtra("noteid", noteIdForDetails);
                //intent.putExtra("check", 0);
                startActivity(intent);
                imageButtonsquence.setVisibility(View.VISIBLE);
                layout_note_more_Info.setVisibility(View.GONE);
                isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.GONE);
                imageButtonAudioMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
            }
        });
        imageButtonPaintMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollView.setVisibility(View.INVISIBLE);

                isPaintMode = true;
                //updateButtonUI(R.id.imageButtonPaintMode);
                layOutDrawingView.setVisibility(View.VISIBLE);
                drawingControls.setVisibility(View.VISIBLE);

                isTextmodeSelected = false;
                //updateScribbleControlListners(R.id.imageButtondrawback);
                imageButtonsquence.setVisibility(View.GONE);
                imageButtoncheckbox.setVisibility(View.GONE);
                layout_note_more_Info.setVisibility(View.GONE);
                isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                imageButtonHamburg.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.VISIBLE);
                drawView.setVisibility(View.VISIBLE);
                //drawView.setDrawColor(getResources().getColor(R.color.black));
                drawView.setDrawColor(color_selected);
                drawView.setBrushSize(16);
            }
        });
        imageButtonShareMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingControls.setVisibility(View.GONE);
                //updateButtonUI(R.id.imageButtonShareMode);
                System.out.println("share mode");
                layOutDrawingView.setVisibility(View.GONE);
                // listviewNotes.setScrollContainer(true);
                imageButtonsquence.setVisibility(View.VISIBLE);
                layout_note_more_Info.setVisibility(View.GONE);
                isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
            }
        });
        imageButtonTextMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateButtonUI(R.id.imageButtonTextMode);
                System.out.println("text mode");
                //layOutDrawingView.setVisibility(View.GONE);
                // startActivity(new
                // Intent(getApplicationContext(),TextChooserActivity.class));

                // listviewNotes.setScrollContainer(true);
                //LayoutTextWritingView.setVisibility(View.VISIBLE);
                //isTextmodeSelected = true;

                drawingControls.setVisibility(View.GONE);
                //textNoteControls.setVisibility(View.VISIBLE);
                //imageButtonsquence.setVisibility(View.VISIBLE);
                layout_note_more_Info.setVisibility(View.GONE);
                isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                horizontal_scroll_editor.setVisibility(View.VISIBLE);

                imageButtonHamburg.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.VISIBLE);
                imageButtonsquence.setVisibility(View.GONE);
                imageButtoncheckbox.setVisibility(View.GONE);

                noteElements = (LinearLayout) findViewById(R.id.noteElements);
                LayoutInflater inflator = getLayoutInflater();
                viewText = inflator.inflate(R.layout.note_text, null, false);
                final RelativeLayout textView = (RelativeLayout) viewText.findViewById(R.id.textView);
                final RichEditor editor = (RichEditor) viewText.findViewById(R.id.editor);
                final CheckBox deleteText = (CheckBox) viewText.findViewById(R.id.deleteText);
                editor.setMinimumHeight(20);
                editor.setEditorHeight(20);
                editor.setBackgroundColor(0);
                noteElements.addView(textView);

                allDelete.add(deleteText);
                allRe.add(editor);
                editor.setTag(allRe.size() - 1);
                setFeatureTag(String.valueOf(allRe.size() - 1));

                final boolean[] ne_added = {false};
                final long[] thisnoteid = new long[1];
                final int[] height = {editor.getHeight()};
                //editor.setHtml(s);

                //editor.requestFocus();

                textelementid.clear();

                if (textelementid.size() == 0)
                    textelementid.add(editor);


                deleteText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        multipleDelete(buttonView, textView);
                    }
                });

                editor.setPlaceholder("Tap and start typing...");
                //editor.focusEditor();
                //editor.requestFocus();
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);

                /*int[] coords = new int[2];
                editor.getLocationOnScreen(coords);

                //int xa = coords[0];
                //int ya = coords[1];

                //int xa = editor.getScrollY();
                int ya = editor.getScrollY();
                Log.e("jay scrollY", String.valueOf(ya));

                // Obtain MotionEvent object
                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis() + 100;
                //float x = xa + 5;
                //float y = ya + 10;
                //Log.e("jay x", String.valueOf(x));
                //Log.e("jay y", String.valueOf(y));
                // List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
                int metaState = 0;
                MotionEvent motionEvent = MotionEvent.obtain(
                        downTime,
                        eventTime,
                        MotionEvent.ACTION_DOWN,
                        14.944548F,
                        20.838715F,
                        metaState
                );

                    // Dispatch touch event to view
                editor.dispatchTouchEvent(motionEvent);*/

                /*editor.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.e("jay touch", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                        return false;
                    }
                });*/

                // TODO editor up
                editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        //mEditor.setEditorHeight(400);
                        String id = v.getTag().toString();

                        setFeatureTag(id);
                        drawingControls.setVisibility(View.GONE);
                        layout_note_more_Info.setVisibility(View.GONE);
                        isMoreShown = false;
                        layout_audio_notechooser.setVisibility(View.GONE);
                        horizontal_scroll_editor.setVisibility(View.VISIBLE);

                        imageButtonHamburg.setVisibility(View.GONE);
                        imageButtoncalander.setVisibility(View.VISIBLE);
                        imageButtonsquence.setVisibility(View.GONE);
                        imageButtoncheckbox.setVisibility(View.GONE);

                        if (textelementid.size() > 0)
                            textelementid.remove(0);

                        textelementid.add(v);

                    }
                });

                editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                    @Override
                    public void onTextChange(String s) {

                        String plainText = getPlainText(s);

                        if (noteIdForDetails == null)
                            makeNote();

                        if (!ne_added[0]) {
                            NoteElement ne = new NoteElement(Long.parseLong(noteIdForDetails), getNoteElementOrderNumber(), "yes", "text", s, plainText, "");
                            ne.save();
                            deleteText.setTag(ne.getId());
                            thisnoteid[0] = ne.getId();
                            ne_added[0] = true;
                        }
                        if (height[0] < editor.getHeight()) {
                            height[0] = editor.getHeight();
                            scrollView.setScrollY(scrollView.getScrollY() + pxFromDp(NoteMainActivity.this, 20));
                        }
                        if (height[0] > editor.getHeight()) {
                            height[0] = editor.getHeight();
                            scrollView.setScrollY(scrollView.getScrollY() - pxFromDp(NoteMainActivity.this, 20));
                        }
                        if (ne_added[0]) {
                            NoteElement ne = NoteElement.findById(NoteElement.class, thisnoteid[0]);
                            ne.setContent(s);
                            ne.setContentA(plainText);
                            ne.save();
                            modifyNoteTime();
                        }
                    }
                });

            }
        });


        imageButtonMoreMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonsquence.setVisibility(View.VISIBLE);
                drawingControls.setVisibility(View.GONE);
                System.out.println("more mode");
                layOutDrawingView.setVisibility(View.GONE);
                //updateButtonUI(R.id.imageButtonMoreMode);
                layout_audio_notechooser.setVisibility(View.GONE);

                showShareActionSheet(v);

                /*if (isMoreShown == false) {
                    isMoreShown = true;
                    layout_note_more_Info.setVisibility(View.VISIBLE);
                    imageButtonMoreMode.setBackgroundColor(getResources().getColor(R.color.A8b241b));
                } else {
                    isMoreShown = false;
                    layout_note_more_Info.setVisibility(View.GONE);
                    imageButtonMoreMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
                }*/
                imageButtoncalander.setVisibility(View.GONE);
            }
        });

    }

    private void setFeatureTag(String id) {

        bold.setTag(Integer.parseInt(id));
        italic.setTag(Integer.parseInt(id));
        underline.setTag(Integer.parseInt(id));
        orderedList.setTag(Integer.parseInt(id));
        unorderedList.setTag(Integer.parseInt(id));
        h1.setTag(Integer.parseInt(id));
        h2.setTag(Integer.parseInt(id));
        h3.setTag(Integer.parseInt(id));
        h4.setTag(Integer.parseInt(id));
        h5.setTag(Integer.parseInt(id));
        h6.setTag(Integer.parseInt(id));
        align_left.setTag(Integer.parseInt(id));
        align_center.setTag(Integer.parseInt(id));
        align_right.setTag(Integer.parseInt(id));
        redo.setTag(Integer.parseInt(id));
        undo.setTag(Integer.parseInt(id));
    }

    void updateButtonUI(int id) {

        imageButtonAudioMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonTextMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonShareMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonPaintMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonMoreMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));
        imageButtonImageMode.setBackgroundColor(getResources().getColor(
                R.color.header_bg));

        switch (id) {

            case R.id.imageButtonMoreMode: {

                imageButtonMoreMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
            }
            break;
            case R.id.imageButtonTextMode: {

                imageButtonTextMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));

            }
            break;
            case R.id.imageButtonPaintMode: {
                imageButtonPaintMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));
            }
            break;
            case R.id.imageButtonShareMode: {

                imageButtonShareMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));

            }
            break;
            case R.id.imageButtonAudioMode: {
                imageButtonAudioMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));

            }
            break;
            case R.id.imageButtonImageMode: {
                imageButtonImageMode.setBackgroundColor(getResources().getColor(
                        R.color.A8b241b));

            }
            break;

            default:
                break;
        }

    }

    /*************
     * text control Here
     ************/
    void showTextNoteDialog() {

        final Dialog dialog = new Dialog(NoteMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.note_text_style_chooser);

        final LinearLayout layoutPapers = (LinearLayout) dialog.findViewById(R.id.layoutPapers);

        Button buttonPaper = (Button) dialog.findViewById(R.id.buttonPaper);
        buttonPaper.setTypeface(RegularFunctions.getAgendaBoldFont(this));


        layoutPapers.setVisibility(View.VISIBLE);

        ImageButton paper_bg_6 = (ImageButton) dialog
                .findViewById(R.id.paper_bg_5);
        ImageButton paper_bg_5 = (ImageButton) dialog
                .findViewById(R.id.paper_bg_4);
        ImageButton paper_bg_4 = (ImageButton) dialog
                .findViewById(R.id.paper_bg_3);
        ImageButton paper_bg_2 = (ImageButton) dialog
                .findViewById(R.id.paper_bg_2);
        ImageButton paper_bg_1 = (ImageButton) dialog
                .findViewById(R.id.paper_bg_1);

        paper_bg_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paperButtonSelected(arg0);
                dialog.dismiss();
            }
        });
        paper_bg_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paperButtonSelected(arg0);
                dialog.dismiss();
            }
        });
        paper_bg_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paperButtonSelected(arg0);
                dialog.dismiss();
            }
        });
        paper_bg_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paperButtonSelected(arg0);
                dialog.dismiss();
            }
        });
        paper_bg_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                paperButtonSelected(arg0);
                dialog.dismiss();
            }
        });

        ImageButton color_bg_10 = (ImageButton) dialog
                .findViewById(R.id.color_bg_10);
        ImageButton color_bg_9 = (ImageButton) dialog
                .findViewById(R.id.color_bg_9);
        ImageButton color_bg_8 = (ImageButton) dialog
                .findViewById(R.id.color_bg_8);
        ImageButton color_bg_7 = (ImageButton) dialog
                .findViewById(R.id.color_bg_7);
        ImageButton color_bg_6 = (ImageButton) dialog
                .findViewById(R.id.color_bg_6);
        ImageButton color_bg_5 = (ImageButton) dialog
                .findViewById(R.id.color_bg_5);
        ImageButton color_bg_4 = (ImageButton) dialog
                .findViewById(R.id.color_bg_4);
        ImageButton color_bg_3 = (ImageButton) dialog
                .findViewById(R.id.color_bg_3);
        ImageButton color_bg_2 = (ImageButton) dialog
                .findViewById(R.id.color_bg_2);
        ImageButton color_bg_1 = (ImageButton) dialog
                .findViewById(R.id.color_bg_1);

        color_bg_10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });

        color_bg_8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });
        color_bg_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButtonSelected(v);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // TODO highlight + brush + eraser dialog
    public void showScribbleDialog(String name) {
        scribbleDialog = new Dialog(NoteMainActivity.this);
        scribbleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scribbleDialog.setCanceledOnTouchOutside(true);
        scribbleDialog.setContentView(R.layout.scribble_dialog);

        final RelativeLayout layoutHighlight = (RelativeLayout) scribbleDialog.findViewById(R.id.highlightView);
        final RelativeLayout layoutBrush = (RelativeLayout) scribbleDialog.findViewById(R.id.brushView);
        final RelativeLayout layoutEraser = (RelativeLayout) scribbleDialog.findViewById(R.id.eraserView);

        buttonHighlight = (ImageButton) scribbleDialog.findViewById(R.id.buttonHighlight);
        buttonBrush = (ImageButton) scribbleDialog.findViewById(R.id.buttonBrush);
        buttonEraser = (ImageButton) scribbleDialog.findViewById(R.id.buttonEraser);

        layoutHighlight.setVisibility(View.GONE);
        layoutBrush.setVisibility(View.GONE);
        layoutEraser.setVisibility(View.GONE);


        if (name.equals("highlight"))
            layoutHighlight.setVisibility(View.VISIBLE);
        else if (name.equals("brush"))
            layoutBrush.setVisibility(View.VISIBLE);
        else if (name.equals("eraser"))
            layoutEraser.setVisibility(View.VISIBLE);

        buttonHighlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                layoutHighlight.setVisibility(View.VISIBLE);
                layoutBrush.setVisibility(View.GONE);
                layoutEraser.setVisibility(View.GONE);
                openHighlight();
                updateScribbleButtonColor("highlight");
            }
        });

        buttonBrush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                layoutHighlight.setVisibility(View.GONE);
                layoutBrush.setVisibility(View.VISIBLE);
                layoutEraser.setVisibility(View.GONE);
                openBrush();
                updateScribbleButtonColor("brush");
            }
        });

        buttonEraser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHighlight.setVisibility(View.GONE);
                layoutBrush.setVisibility(View.GONE);
                layoutEraser.setVisibility(View.VISIBLE);
                openEraser();
                updateScribbleButtonColor("eraser");
            }
        });

        // Highlight
        LinearLayout paintHighlight1 = (LinearLayout) layoutHighlight.findViewById(R.id.paint_highlight1);

        ImageButton highlightbutton1 = (ImageButton) paintHighlight1
                .findViewById(R.id.hightlightbutton1);
        ImageButton highlightbutton2 = (ImageButton) paintHighlight1
                .findViewById(R.id.hightlightbutton2);
        ImageButton highlightbutton3 = (ImageButton) paintHighlight1
                .findViewById(R.id.highlightbutton3);
        ImageButton highlightbutton4 = (ImageButton) paintHighlight1
                .findViewById(R.id.highlightbutton4);
        ImageButton highlightbutton5 = (ImageButton) paintHighlight1
                .findViewById(R.id.highlightbutton5);
        ImageButton highlightbutton6 = (ImageButton) paintHighlight1
                .findViewById(R.id.highlightbutton6);
        ImageButton highlightbutton7 = (ImageButton) paintHighlight1
                .findViewById(R.id.highlightbutton7);

        highlightbutton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });
        highlightbutton7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightClicked(v);
            }
        });

        final TextView tvHighlightSize = (TextView) layoutHighlight.findViewById(R.id.tvHighlightSize);
        tvHighlightSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        TextView tvHighSize = (TextView) layoutHighlight.findViewById(R.id.tvHighSize);
        tvHighSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        String hsize = String.valueOf(lastHighlightSize);
        tvHighlightSize.setText(hsize);
        SeekBar hightlight_sizeSeekBar = (SeekBar) layoutHighlight.findViewById(R.id.hightlight_sizeSeekBar);
        hightlight_sizeSeekBar.setMax(10);
        hightlight_sizeSeekBar.setProgress(lastHighlightSize);
        highlightview = layoutHighlight.findViewById(R.id.aview);
        LayerDrawable bgDrawable = (LayerDrawable) highlightview.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_id);
        shape.setColor(lastHighlightColor);
        //aview.setBackgroundColor(lastHighlightColor);
        if (lastHighlightSize == 0)
            tvHighlightSize.setText("0.5");
        else
            tvHighlightSize.setText(hsize);

        hightlight_sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lastHighlightSize = progress;
                //drawView.setBrushSize((int)(progress * 6.299));
                drawView.setDrawColor(lastHighlightColor);

                if (lastHighlightSize == 0) {
                    drawView.setBrushSize(3);
                    tvHighlightSize.setText("0.5");
                    highlightViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                } else {
                    drawView.setBrushSize((int) (lastHighlightSize * 6.299));
                    tvHighlightSize.setText(String.valueOf(lastHighlightSize));
                    highlightViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (progress * 6.299), getResources().getDisplayMetrics());
                }

                //highlightViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int)(progress * 6.299), getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(highlightViewSize, highlightViewSize);
                params.gravity = Gravity.CENTER;
                highlightview.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Brush
        LinearLayout paint_brush1 = (LinearLayout) layoutBrush.findViewById(R.id.paint_brush1);
        LinearLayout paint_brush2 = (LinearLayout) layoutBrush.findViewById(R.id.paint_brush2);

        ImageButton brushButton1 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton1);
        ImageButton brushButton2 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton2);
        ImageButton brushButton3 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton3);
        ImageButton brushButton4 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton4);
        ImageButton brushButton5 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton5);
        ImageButton brushButton6 = (ImageButton) paint_brush1.findViewById(R.id.brushbutton6);
        ImageButton brushButton7 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton7);
        ImageButton brushButton8 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton8);
        ImageButton brushButton9 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton9);
        ImageButton brushButton10 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton10);
        ImageButton brushButton11 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton11);
        ImageButton brushButton12 = (ImageButton) paint_brush2.findViewById(R.id.brushbutton12);

        brushButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        brushButton12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushClicked(v);
            }
        });

        SeekBar sizeSeekBar = (SeekBar) scribbleDialog.findViewById(R.id.sizeSeekBar);
        sizeSeekBar.setMax(10);
        sizeSeekBar.setProgress(lastBrushSize);

        brushview = layoutBrush.findViewById(R.id.view);
        LayerDrawable brushDrawable = (LayerDrawable) brushview.getBackground();
        brushshape = (GradientDrawable) brushDrawable.findDrawableByLayerId(R.id.shape_id);
        brushshape.setColor(lastBrushColor);
        brushview.setBackground(brushshape);

        if (count == 0) {
            drawView.setBrushSize((int) (lastBrushSize * 6.299));
            drawView.setDrawColor(Color.parseColor("#000000"));
            lastBrushColor = Color.BLACK;
            brushshape.setColor(lastBrushColor);
            brushview.setBackground(brushshape);
        }

        final TextView tvBrushSize = (TextView) scribbleDialog.findViewById(R.id.tvBrushSize);
        tvBrushSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        TextView tvPaintSize = (TextView) scribbleDialog.findViewById(R.id.tvPaintSize);
        tvPaintSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        String size = String.valueOf(lastBrushSize);
        tvBrushSize.setText(size);
        drawView.setBrushSize((int) (lastBrushSize * 6.299));

        if (count > 0) {
            brushview.setBackgroundColor(color_selected);
        }

        if (lastBrushSize == 0)
            tvBrushSize.setText("0.5");
        else
            tvBrushSize.setText(size);

        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lastBrushSize = progress;
                if (lastBrushSize == 0) {
                    drawView.setBrushSize(3);
                    tvBrushSize.setText("0.5");
                    brushViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                } else {
                    drawView.setBrushSize((int) (lastBrushSize * 6.299));
                    tvBrushSize.setText(String.valueOf(lastBrushSize));
                    brushViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (progress * 6.299), getResources().getDisplayMetrics());
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(brushViewSize, brushViewSize);
                params.gravity = Gravity.CENTER;
                brushview.setLayoutParams(params);
                //brushshape.setColor(color_selected);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Eraser
        sizeEraserSeekBar = (SeekBar) scribbleDialog.findViewById(R.id.eraser_sizeSeekBar);
        sizeEraserSeekBar.setMax(10);
        sizeEraserSeekBar.setProgress(lastEraserSize);

        final TextView tvEraserSize = (TextView) scribbleDialog.findViewById(R.id.tvEraserSize);
        tvEraserSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        TextView tvSize = (TextView) scribbleDialog.findViewById(R.id.tvSize);
        tvSize.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        String esize = String.valueOf(lastEraserSize);
        if (lastEraserSize == 0) {
            tvEraserSize.setText("0.5");
        } else
            tvEraserSize.setText(esize);
        eraserview = layoutEraser.findViewById(R.id.eraser_view);
        LayerDrawable eraserDrawable = (LayerDrawable) eraserview.getBackground();
        final GradientDrawable erasershape = (GradientDrawable) eraserDrawable.findDrawableByLayerId(R.id.shape_id);
        erasershape.setColor(Color.BLACK);

        sizeEraserSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lastEraserSize = progress;
                if (lastEraserSize == 0) {
                    drawView.setBrushSize(3);
                    tvEraserSize.setText("0.5");
                    eraserViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                } else {
                    drawView.setBrushSize((int) (lastEraserSize * 6.299));
                    tvEraserSize.setText(String.valueOf(lastEraserSize));
                    eraserViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (progress * 6.299), getResources().getDisplayMetrics());
                }
                drawView.setDrawColor(lastBrushColor);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(eraserViewSize, eraserViewSize);
                params.gravity = Gravity.CENTER;
                eraserview.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        scribbleDialog.show();
    }

    // TODO highlighter clicked
    public void highlightClicked(View view) {
        String color = view.getTag().toString();
        lastHighlightColor = Color.parseColor(color);
        drawView.setDrawColor(lastHighlightColor);
        drawView.setBrushSize((int) (lastBrushSize * 6.299));
        brushshape.setColor(lastHighlightColor);
        highlightview.setBackground(brushshape);
    }

    // TODO brush clicked
    public void brushClicked(View view) {
        String color = view.getTag().toString();
        lastBrushColor = Color.parseColor(color);
        drawView.setDrawColor(lastBrushColor);
        drawView.setBrushSize((int) (lastBrushSize * 6.299));
        brushshape.setColor(lastBrushColor);
        brushview.setBackground(brushshape);
        count++;
    }

    // TODO open highlight
    public void openHighlight() {
        drawView.onClickEraser(1);
        drawView.setDrawColor(lastHighlightColor);
        if (lastHighlightColor == 0) {
            drawView.setBrushSize(3);
            highlightViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        } else {
            drawView.setBrushSize((int) (lastHighlightSize * 6.299));
            highlightViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (lastHighlightSize * 6.299), getResources().getDisplayMetrics());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(highlightViewSize, highlightViewSize);
        params.gravity = Gravity.CENTER;
        highlightview.setLayoutParams(params);
        buttonHighlight.setBackgroundColor(getResources().getColor(R.color.eaeaea));
        buttonHighlight.setImageResource(R.drawable.scrbble_highlight_red);
        buttonBrush.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonBrush.setImageResource(R.drawable.scrbble_draw);
        buttonEraser.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonEraser.setImageResource(R.drawable.scrbble_erase);
        firstHighlightColor = lastHighlightColor;
        drawView.setDrawColor(lastHighlightColor);
    }

    // TODO open brush
    public void openBrush() {
        drawView.onClickEraser(1);
        drawView.setDrawColor(lastBrushColor);
        if (lastBrushSize == 0) {
            drawView.setBrushSize(3);
            brushViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        } else {
            drawView.setBrushSize((int) (lastBrushSize * 6.299));
            brushViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (lastBrushSize * 6.299), getResources().getDisplayMetrics());
        }
        brushview.setBackgroundColor(lastBrushColor);
        brushshape.setColor(lastBrushColor);
        brushview.setBackground(brushshape);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(brushViewSize, brushViewSize);
        params.gravity = Gravity.CENTER;
        brushview.setLayoutParams(params);
        buttonHighlight.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonHighlight.setImageResource(R.drawable.scrbble_highlight);
        buttonBrush.setBackgroundColor(getResources().getColor(R.color.eaeaea));
        buttonBrush.setImageResource(R.drawable.scrbble_draw_red);
        buttonEraser.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonEraser.setImageResource(R.drawable.scrbble_erase);
        drawView.setDrawColor(lastBrushColor);
    }

    // TODO open eraser
    public void openEraser() {
        //drawView.setDrawColor(Color.parseColor("#FFFFFF"));
        drawView.setDrawColor(Color.WHITE);
        drawView.onClickEraser(0);
        if (lastEraserSize == 0) {
            drawView.setBrushSize(3);
            eraserViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        } else {
            drawView.setBrushSize((int) (lastEraserSize * 6.299));
            eraserViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (lastEraserSize * 6.299), getResources().getDisplayMetrics());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(eraserViewSize, eraserViewSize);
        params.gravity = Gravity.CENTER;
        eraserview.setLayoutParams(params);
        buttonHighlight.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonHighlight.setImageResource(R.drawable.scrbble_highlight);
        buttonBrush.setBackgroundColor(getResources().getColor(R.color.header_bg));
        buttonBrush.setImageResource(R.drawable.scrbble_draw);
        buttonEraser.setBackgroundColor(getResources().getColor(R.color.eaeaea));
        buttonEraser.setImageResource(R.drawable.scrbble_erase_red);
    }

    // TODO updateScribbleButtonColor
    public void updateScribbleButtonColor(String name) {
        if (name.equals("highlight")) {
            imageButtonhighlightdraw.setBackgroundColor(getResources().getColor(R.color.A8b241b));
            imageButtonbrushdraw.setBackgroundColor(getResources().getColor(R.color.header_bg));
            imageButtondrawerase.setBackgroundColor(getResources().getColor(R.color.header_bg));
        }
        if (name.equals("brush")) {
            imageButtonhighlightdraw.setBackgroundColor(getResources().getColor(R.color.header_bg));
            imageButtonbrushdraw.setBackgroundColor(getResources().getColor(R.color.A8b241b));
            imageButtondrawerase.setBackgroundColor(getResources().getColor(R.color.header_bg));
        }
        if (name.equals("eraser")) {
            imageButtonhighlightdraw.setBackgroundColor(getResources().getColor(R.color.header_bg));
            imageButtonbrushdraw.setBackgroundColor(getResources().getColor(R.color.header_bg));
            imageButtondrawerase.setBackgroundColor(getResources().getColor(R.color.A8b241b));
        }
    }

    void showNewDrawingDialog() {

        final Dialog dialog = new Dialog(NoteMainActivity.this);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("REFRESH PAINTING");
        textViewTitleAlert.setTypeface(RegularFunctions.getAgendaBoldFont(this));
        textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage
                .setText("Are you sure?");
        textViewTitleAlertMessage.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);

        buttonAlertCancel.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        buttonAlertOk.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        buttonAlertCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        buttonAlertOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                drawView.startNew();
                dialog.dismiss();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (isRecordingAudio) {
            Toast.makeText(getApplicationContext(), "Audio recording is going on!", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }

    public void paperButtonSelected(View view) {
        paperBackground(view.getTag().toString());
        String viewTag = view.getTag().toString();

        if (noteIdForDetails == null) {
            makeNote();
        }

        if (noteIdForDetails != null) {
            Note note = Note.findById(Note.class, Long.parseLong(noteIdForDetails));
            note.setColor(viewTag);
            note.setModifytime(currentDateStr);
            note.save();
        }
    }

    public void paperBackground(String background) {
        if (background.equals("paper_bg_1")) {
            /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paper_bg_1111);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
            bitmapDrawable.setTileModeY(Shader.TileMode.REPEAT);
            background_bg.setBackgroundDrawable(bitmapDrawable);*/
            background_bg.setBackgroundResource(R.drawable.paper_bg_1);
        } else if (background.equals("paper_bg_2")) {
            background_bg.setBackgroundResource(R.drawable.paper_bg_2);
        } else if (background.equals("paper_bg_3")) {
            background_bg.setBackgroundResource(R.drawable.paper_bg_3);
        } else if (background.equals("paper_bg_4")) {
            background_bg.setBackgroundResource(R.drawable.paper_bg_4);
        } else if (background.equals("paper_bg_5")) {
            background_bg.setBackgroundResource(R.drawable.paper_bg_5);
        }
    }

    public void colorButtonSelected(View view) {

        currentFontColor = Color.BLACK;
        background_bg.setBackgroundColor(Color.parseColor(view.getTag()
                .toString()));
        backgroundColor = view.getTag().toString();

        if (noteIdForDetails == null) {
            makeNote();
        }

        if (noteIdForDetails != null) {
            Note note = Note.findById(Note.class, Long.parseLong(noteIdForDetails));
            note.setColor(backgroundColor);
            note.save();
            modifyNoteTime();
        }

    }

    public void makeNote() {
        String timestamp = currentDateStr;
        Note note = null;
        try {
            note = new Note(textViewheaderTitle.getText().toString(), "", backgroundColor, "0", 0L, "0", "#FFFFFF", timestamp, timestamp, "0", 0, stringToDate(timestamp), stringToDate(timestamp));
            note.save();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        noteIdForDetails = note.getId().toString();
        noteTitle[0] = note.getTitle();
    }

    public long stringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
    }

    public void modifyNoteTime() {
        String timestamp = currentDateStr;
        try {
            Note n = Note.findById(Note.class, Long.parseLong(noteIdForDetails));
            n.setModifytime(timestamp);
            n.setMtime(stringToDate(timestamp));
            n.save();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    public void deleteElements(String tag) {
        NoteElement ne = NoteElement.findById(NoteElement.class, Long.parseLong(tag));
        ne.delete();
        //onResume();
    }

    public void fetchNoteElementsFromDb() throws FileNotFoundException {

        if (noteIdForDetails != null) {

            noteElements.removeAllViews();
            List<NoteElement> ne = NoteElement.findWithQuery(NoteElement.class, "SELECT * FROM NOTE_ELEMENT WHERE NOTEID = " + Long.parseLong(noteIdForDetails));

            Note note = Note.findById(Note.class, Long.parseLong(noteIdForDetails));


            if (note.islocked == 1)
                buttonLock.setText("Unlock");
            else
                buttonLock.setText("Lock");

            noteTitle[0] = note.title;

            textViewheaderTitle.setText(note.getTitle());

            background = note.getColor();

            if (background.contains("#")) {
                background_bg.setBackgroundColor(Color.parseColor(background));
            } else {
                paperBackground(background);
            }

            for (final NoteElement n : ne) {

                orderNumber = n.getOrderNumber();

                if (n.type.equals("text")) {
                    String s = n.content;
                    noteElements = (LinearLayout) findViewById(R.id.noteElements);
                    LayoutInflater inflator = getLayoutInflater();
                    View viewText = inflator.inflate(R.layout.note_text, null, false);
                    final RelativeLayout textView = (RelativeLayout) viewText.findViewById(R.id.textView);
                    final RichEditor editor = (RichEditor) viewText.findViewById(R.id.editor);
                    final CheckBox deleteText = (CheckBox) viewText.findViewById(R.id.deleteText);
                    deleteText.setTag(n.getId());

                    allDelete.add(deleteText);
                    allRe.add(editor);
                    editor.setTag(allRe.size() - 1);
                    editor.setHtml(s);
                    editor.setBackgroundColor(0);

                    final int[] height = {editor.getHeight()};

                    editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            String id = v.getTag().toString();
                            setFeatureTag(id);
                            drawingControls.setVisibility(View.GONE);
                            layout_note_more_Info.setVisibility(View.GONE);
                            isMoreShown = false;
                            layout_audio_notechooser.setVisibility(View.GONE);
                            horizontal_scroll_editor.setVisibility(View.VISIBLE);
                            imageButtoncalander.setVisibility(View.VISIBLE);
                            imageButtonHamburg.setVisibility(View.GONE);

                            // TODO hide
                            imageButtoncheckbox.setVisibility(View.GONE);
                            imageButtonsquence.setVisibility(View.GONE);
                            if (textelementid.size() > 0)
                                textelementid.remove(0);

                            textelementid.add(v);
                        }
                    });

                    editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                        @Override
                        public void onTextChange(String s) {
                            n.setContent(s);
                            n.setContentA(getPlainText(s));
                            n.save();
                            modifyNoteTime();

                            if (height[0] < editor.getHeight()) {
                                height[0] = editor.getHeight();

                                Log.e("jay editor height", String.valueOf(editor.getHeight()));
                                Log.e("jay dp from px", String.valueOf(dpFromPx(getApplicationContext(), editor.getHeight())));
                                scrollView.setScrollY(scrollView.getScrollY() + pxFromDp(NoteMainActivity.this, 20));
                            }
                            if (height[0] > editor.getHeight()) {
                                height[0] = editor.getHeight();

                                Log.e("jay editor height", String.valueOf(editor.getHeight()));
                                scrollView.setScrollY(scrollView.getScrollY() - pxFromDp(NoteMainActivity.this, 20));
                            }
                        }
                    });


                    deleteText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            multipleDelete(buttonView, textView);
                        }

                    });

                    noteElements.addView(textView);

                    editor.getContentHeight();

                    editor.setOnInitialLoadListener(new RichEditor.AfterInitialLoadListener() {
                        @Override
                        public void onAfterInitialLoad(boolean isReady) {

                            if (isReady) {

                                textView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                                    @SuppressLint("NewApi")
                                    @SuppressWarnings("deprecation")
                                    @Override
                                    public void onGlobalLayout() {
                                        //now we can retrieve the width and height
                                        int width = editor.getWidth();
                                        int height = editor.getHeight();

                                        int conheight = editor.getContentHeight();
                                        Log.e("jay text height", String.valueOf(height));
                                        Log.e("jay con height", String.valueOf(conheight));
                                        //...
                                        //do whatever you want with them
                                        //...
                                        //this is an important step not to keep receiving callbacks:
                                        //we should remove this listener
                                        //I use the function to remove it based on the api level!

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                                            editor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        else
                                            editor.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    }
                                });
                            }
                        }
                    });


                } else {
                    if (n.type.equals("image")) {
                        // add image layout
                        noteElements = (LinearLayout) findViewById(R.id.noteElements);
                        LayoutInflater inflator = LayoutInflater.from(getApplicationContext());
                        View viewImage = inflator.inflate(R.layout.note_image, null, false);
                        final RelativeLayout note_image = (RelativeLayout) viewImage.findViewById(R.id.note_image);
                        ImageView note_imageview = (ImageView) note_image.findViewById(R.id.note_imageview);
                        //SubsamplingScaleImageView note_imageview = (SubsamplingScaleImageView)findViewById(R.id.note_imageview);
                        final CheckBox cbDelete = (CheckBox) viewImage.findViewById(R.id.cbDelete);


                        allDelete.add(cbDelete);
                        cbDelete.setTag(n.getId());

                        cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                multipleDelete(buttonView, note_image);
                            }

                        });

                        String name = n.content;
                        File f = new File(Environment.getExternalStorageDirectory() + "/NoteShare/NoteShare Images/" + name);
                        //int deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
                        //int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();


                        //Bitmap b = BitmapFactory.decodeFile(String.valueOf(f));

                        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                        DisplayMetrics dm = new DisplayMetrics();
                        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                        float height = dm.heightPixels;
                        float width = dm.widthPixels;

                        Bitmap b = decodeFile(f, (float) (height/1.5), width);

                        //Bitmap scaledBitmap = compressImage(Uri.fromFile(f).toString());

                        note_imageview.setImageBitmap(b);
                        note_imageview.setMaxHeight((int) (height / 1.5));

                        //note_imageview.setImage(Environment.getExternalStorageDirectory() + "/NoteShare/NoteShare Images/" + name);
                        //note_imageview.setImage(ImageSource.uri(f.getPath()));

                        noteElements.addView(note_image);

                        note_image.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                            @SuppressLint("NewApi")
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onGlobalLayout() {
                                //now we can retrieve the width and height
                                int width = note_image.getWidth();
                                int height = note_image.getHeight();
                                //Log.e("jay image height", String.valueOf(height));
                                //...
                                //do whatever you want with them
                                //...
                                //this is an important step not to keep receiving callbacks:
                                //we should remove this listener
                                //I use the function to remove it based on the api level!

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                                    note_image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                else
                                    note_image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        });

                    } else if (n.type.equals("scribble")) {
                        noteScribbleElements = (RelativeLayout) findViewById(R.id.scribbleRelative);
                        LayoutInflater inflator = LayoutInflater.from(getApplicationContext());
                        final View viewImage = inflator.inflate(R.layout.note_image_scribble, null, false);
                        final RelativeLayout note_image = (RelativeLayout) viewImage.findViewById(R.id.note_image);
                        final ImageView note_imageview = (ImageView) note_image.findViewById(R.id.note_imageview);

                        String name = n.content;

                        CheckBox scribble_delete = (CheckBox) note_image.findViewById(R.id.deleteScribbleImage);
                        allDelete.add(scribble_delete);
                        //allDelete.add(viewImage);
                        scribble_delete.setTag(n.getId());

                        scribble_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                multipleDelete(buttonView, note_imageview);
                            }

                        });

                        File f = new File(Environment.getExternalStorageDirectory() + "/NoteShare/.NoteShare/" + name);
                        Bitmap b = BitmapFactory.decodeFile(String.valueOf(f));
                        note_imageview.setImageBitmap(b);

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, Integer.parseInt(n.getContentA()), 0, 0);
                        note_imageview.setLayoutParams(params);


                        //RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(pxFromDp(this, 25), pxFromDp(this, 25));
                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params1.setMargins(0, Integer.parseInt(n.getContentA()), 0, 0);
                        scribble_delete.setLayoutParams(params1);
                        //scribble_delete.setPadding(5,5,5,5);
                        scribbleAdded = true;
                        noteScribbleElements.addView(note_image);

                    } else if (n.type.equals("audio")) {
                        // add audio layout
                        String name = n.getContent();
                        String status = n.getContentA();

                        if (status.equals("false"))
                            continue;

                        //addAudio(name);
                        noteElements = (LinearLayout) findViewById(R.id.noteElements);
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        View viewAudio = inflater.inflate(R.layout.note_audio, null, false);
                        final LinearLayout note_audio = (LinearLayout) viewAudio.findViewById(R.id.note_audio);

                        final MediaPlayer mp = new MediaPlayer();
                        final ImageView audio_play = (ImageView) viewAudio.findViewById(R.id.audio_play);
                        final SeekBar audio_seek = (SeekBar) viewAudio.findViewById(R.id.audio_seek);
                        final TextView audio_text = (TextView) viewAudio.findViewById(R.id.audio_text);
                        final CheckBox audioDelete = (CheckBox) viewAudio.findViewById(R.id.cbDelete);
                        audio_text.setTypeface(RegularFunctions.getAgendaMediumFont(this));
                        allDelete.add(audioDelete);
                        audioDelete.setTag(n.getId());


                        audioDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                multipleDelete(buttonView, note_audio);
                            }

                        });


                        final File f = new File(Environment.getExternalStorageDirectory() + "/NoteShare/NoteShare Audio/" + name);
                        try {
                            mp.setDataSource(f.getAbsolutePath());
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String totalAudioDuration = getDurationBreakdown(mp.getDuration());
                        audio_text.setText("00:00:00/" + totalAudioDuration);

                        // Audio Play
                        audio_play.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mp.isPlaying()) {
                                    mp.pause();
                                    audio_play.setImageResource(R.drawable.ic_audio_play);
                                } else {
                                    audio_play.setImageResource(R.drawable.ic_audio_pause);
                                    mp.start();
                                    audio_seek.setMax(mp.getDuration() / 1000);
                                    final Handler mHandler = new Handler();
                                    // Make sure you update Seekbar on UI thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mp != null) {
                                                int mCurrentPosition = mp.getCurrentPosition() / 1000;
                                                String currentduration = getDurationBreakdown(mp.getCurrentPosition());
                                                String currentduration1 = getDurationBreakdown(mp.getDuration());
                                                if (mCurrentPosition <= mp.getDuration() / 1000) {
                                                    System.out.println("CurrentDuration:" + currentduration);
                                                    audio_seek.setProgress(mCurrentPosition);
                                                    audio_text.setVisibility(View.VISIBLE);
                                                    audio_text.setText(currentduration + "/" + currentduration1);
                                                }
                                            }
                                            mHandler.postDelayed(this, 1000);
                                        }
                                    });
                                    mp.setOnCompletionListener(new OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            audio_play.setImageResource(R.drawable.ic_audio_play);
                                        }
                                    });
                                }
                            }
                        });

                        noteElements.addView(note_audio);

                        note_audio.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                            @SuppressLint("NewApi")
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onGlobalLayout() {
                                //now we can retrieve the width and height
                                int width = note_audio.getWidth();
                                int height = note_audio.getHeight();
                                Log.e("jay audio height", String.valueOf(height));
                                //...
                                //do whatever you want with them
                                //...
                                //this is an important step not to keep receiving callbacks:
                                //we should remove this listener
                                //I use the function to remove it based on the api level!

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                                    note_audio.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                else
                                    note_audio.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        });


                    } else if (n.type.equals("checkbox")) {
                        Boolean status = Boolean.valueOf(n.contentA);
                        String text = n.content;

                        noteElements = (LinearLayout) findViewById(R.id.noteElements);
                        LayoutInflater inflator = getLayoutInflater();
                        View viewChecklist = inflator.inflate(R.layout.note_checklist, null, false);
                        final RelativeLayout checkbox = (RelativeLayout) viewChecklist.findViewById(R.id.checkbox);
                        final ImageView checklist_icon = (ImageView) viewChecklist.findViewById(R.id.checkboxIcon);
                        final CheckBox checklistDelete = (CheckBox) viewChecklist.findViewById(R.id.cbDelete);
                        allDelete.add(checklistDelete);
                        checklistDelete.setTag(n.getId());
                        if (status)
                            checklist_icon.setTag(1);
                        else
                            checklist_icon.setTag(0);

                        final EditText checklist_text = (EditText) viewChecklist.findViewById(R.id.checkboxText);

                        checklist_text.setText(text);
                        checklist_text.setTypeface(RegularFunctions.getAgendaMediumFont(this));
                        //checklist_text.setTag(allCheckboxText.size()-1);

                        Log.e("jay height", String.valueOf(checkbox.getMeasuredHeight()));

                        noteElements.addView(checkbox);

                        if (status) {
                            checklist_icon.setImageResource(R.drawable.ic_checkbox_check);
                            checklist_text.setPaintFlags(checklist_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            checklist_icon.setImageResource(R.drawable.ic_checkbox_uncheck);
                            checklist_text.setPaintFlags(checklist_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }


                        checklistDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                multipleDelete(buttonView, checkbox);
                            }

                        });

                        checklist_text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    System.out.println("Pressed Enter");
                                    addNewCheckBox();
                                    return true;
                                }
                                return false;
                            }
                        });


                        checklist_text.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                n.setContent(s.toString());
                                n.save();
                                modifyNoteTime();

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        checklist_icon.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tag = v.getTag().toString();
                                if (tag.equals("1")) {
                                    checklist_icon.setImageResource(R.drawable.ic_checkbox_uncheck);
                                    n.setContentA("false");
                                    checklist_text.setPaintFlags(checklist_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                    v.setTag("0");
                                } else {
                                    checklist_icon.setImageResource(R.drawable.ic_checkbox_check);
                                    n.setContentA("true");
                                    checklist_text.setPaintFlags(checklist_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    v.setTag("1");
                                }
                                n.save();
                                modifyNoteTime();
                            }
                        });

                        checklist_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                //String id = v.getTag().toString();
                                drawingControls.setVisibility(View.GONE);
                                layout_note_more_Info.setVisibility(View.GONE);
                                isMoreShown = false;
                                layout_audio_notechooser.setVisibility(View.GONE);
                                imageButtoncalander.setVisibility(View.VISIBLE);
                                imageButtonHamburg.setVisibility(View.GONE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(checklist_text, InputMethodManager.SHOW_IMPLICIT);

                                // TODO hide
                                imageButtoncheckbox.setVisibility(View.GONE);
                                imageButtonsquence.setVisibility(View.GONE);

                                if (allCheckboxText.size() > 0)
                                    allCheckboxText.remove(0);

                                allCheckboxText.add((EditText) v);
                            }
                        });

                    }
                }
            }

            noteScribbleElements = (RelativeLayout) findViewById(R.id.scribbleRelative);
            int px = pxFromDp(this, 1500);
            noteScribbleElements.setPadding(0, 0, 0, px);
        }
    }

    public void deleteButton() {
        /*if(isRecordingAudio){
            Toast.makeText(getApplication(),"Oops can't delete while recording is on.", Toast.LENGTH_LONG).show();
        }else{*/
        if (!isDeleteModeSelected) {
            imageButtonDeleteMode.setBackgroundColor(getResources().getColor(R.color.A8b241b));
            for (int i = 0; i < allDelete.size(); i++) {
                //Log.e("jay i", String.valueOf(i));
                try {
                    allDelete.get(i).setVisibility(View.VISIBLE);
                } catch (NullPointerException npe) {
                    Log.e("jay ", Log.getStackTraceString(npe));
                }
            }
            isDeleteModeSelected = true;
        } else {
            imageButtonDeleteMode.setBackgroundColor(getResources().getColor(R.color.header_bg));
            for (int i = 0; i < allDelete.size(); i++) {
                try {
                    allDelete.get(i).setVisibility(View.GONE);
                } catch (NullPointerException npe) {
                    Log.e("jay ", Log.getStackTraceString(npe));
                }
            }
            isDeleteModeSelected = false;
        }
        //}
    }

    public String getPlainText(String htmlText) {
        String plainText = htmlText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        return plainText.replace("&nbsp;", "");
    }

    private String getNextFileName(String name) {
        return Environment.getExternalStorageDirectory() + "/NoteShare/NoteShare Audio/" + name;
    }

    private Runnable updateTimerMethod = new Runnable() {
        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes - (hours * 60);
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            audio_text.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)); // + ":"
            //+ String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };

    public void saveScribble() {
        drawingControls.setVisibility(View.GONE);
        layOutDrawingView.setVisibility(View.GONE);
        updateButtonUI(-1);

        drawView.setDrawingCacheEnabled(true);

        FileNameGenerator fileNameGenerator = new FileNameGenerator();
        String fileName = fileNameGenerator.getFileName("SCRIBBLE");
        File file = new File(Environment.getExternalStorageDirectory(), "/NoteShare/.NoteShare/" + fileName);

        int topCrop = (int) (drawView.getMinY() - pxFromDp(this, 15));
        int bottom = (int) (drawView.getMaxY() + pxFromDp(this, 15));
        int difference = bottom - topCrop;
        int width = drawView.getWidth();


        try {
            Bitmap bmp = Bitmap.createBitmap(drawView.getDrawingCache(), 0, topCrop, width, difference);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (noteIdForDetails == null) {
            makeNote();
        }

        if (file != null) {
            //Toast.makeText(getApplicationContext(), "Drawing saved to Gallery!", Toast.LENGTH_SHORT).show();
            // savedToast.show();

            int top = scrollView.getScrollY() + topCrop;

            if (isRecordingAudio) {
                int px = pxFromDp(this, 80); //70 dp for recording and rest 10 for blank space
                top = top - px;
            }

            NoteElement noteElement = new NoteElement(Long.parseLong(noteIdForDetails), getNoteElementOrderNumber(), "Yes", "scribble", fileName, String.valueOf(top), "");
            noteElement.save();
            modifyNoteTime();
            drawView.destroyDrawingCache();
            drawView.setUserDrawn(false);
            drawView.startNew();
            onResume();

        } else {
            Toast.makeText(getApplicationContext(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT).show();
        }
        isPaintMode = false;
        //dialog.dismiss();
        onResume();
    }

    @Override
    public void onClick(View v) {

    }

    public int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public int getNoteElementOrderNumber() {
        int lastNumber = 0;
        List<NoteElement> ne = NoteElement.findWithQuery(NoteElement.class, "SELECT ORDERNUMBER FROM NOTE_ELEMENT WHERE NOTEID = " + Long.parseLong(noteIdForDetails));
        if (ne.size() > 0)
            return lastNumber = (ne.get(ne.size() - 1).getOrderNumber()) + 1;
        else
            return 1;
    }

    public void saveRecording() {
        isRecordingAudio = false;
        NoteElement noteElement = NoteElement.findById(NoteElement.class, noteElementId);
        noteElement.setContentA("true");
        noteElement.save();
        modifyNoteTime();
        Toast.makeText(NoteMainActivity.this, "Recording Saved", Toast.LENGTH_SHORT).show();

        startTime = 0L;
        audioElement.removeAllViews();
        onResume();
    }

    public void multipleDelete(View v, View parent) {
        String tag = v.getTag().toString();
        //View  deleteView = v;
        String deleteView = String.valueOf(v.getId());
        int j = 0;

        CheckBox cb = (CheckBox) v;

        if (!cb.isChecked()) {

            for (int i = 0; i < multipleDeleteArray.size(); i++) {
                if (multipleDeleteArray.get(i).equals(tag)) {
                    multipleDeleteArray.remove(i);
                    multipleDeleteParentArray.remove(i);
                    parent.setBackgroundColor(Color.TRANSPARENT);
                    break;
                }
            }
        } else {
            multipleDeleteArray.add(tag);
            multipleDeleteParentArray.add(parent);
            parent.setBackgroundColor(2013223710);
        }
    }

    public void addNewCheckBox() {
        imageButtoncalander.setVisibility(View.VISIBLE);
        noteElements = (LinearLayout) findViewById(R.id.noteElements);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View viewChecklist = inflater.inflate(R.layout.note_checklist, null, false);
        final RelativeLayout checkbox = (RelativeLayout) viewChecklist.findViewById(R.id.checkbox);
        final ImageView checklist_icon = (ImageView) viewChecklist.findViewById(R.id.checkboxIcon);
        checklist_icon.setTag("0");
        final EditText checklist_text = (EditText) viewChecklist.findViewById(R.id.checkboxText);
        final CheckBox checklistDelete = (CheckBox) viewChecklist.findViewById(R.id.cbDelete);

        checklist_text.setTypeface(RegularFunctions.getAgendaMediumFont(this));

        checklist_icon.setImageResource(R.drawable.ic_checkbox_uncheck);

        allDelete.add(checklistDelete);

        checklistDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                multipleDelete(buttonView, checkbox);
            }
        });

        allCheckboxText.clear();

        if (allCheckboxText.size() == 0)
            allCheckboxText.add(checklist_text);

        checklist_text.requestFocus();

        noteElements.addView(checkbox);

        drawingControls.setVisibility(View.GONE);
        layout_note_more_Info.setVisibility(View.GONE);
        isMoreShown = false;
        layout_audio_notechooser.setVisibility(View.GONE);
        imageButtoncalander.setVisibility(View.VISIBLE);
        imageButtonHamburg.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(checklist_text, InputMethodManager.SHOW_IMPLICIT);

        // TODO hide
        imageButtoncheckbox.setVisibility(View.GONE);
        imageButtonsquence.setVisibility(View.GONE);

        final boolean[] cb_added = {false};
        final long[] thisnoteelementid = new long[1];


        checklist_text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNewCheckBox();
                    return true;
                }
                return false;
            }
        });


        checklist_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (noteIdForDetails == null) {
                    makeNote();
                }
                if (noteIdForDetails != null) {
                    String updatedText = s.toString();

                    if (!cb_added[0]) {
                        NoteElement ne = new NoteElement(Long.parseLong(noteIdForDetails), getNoteElementOrderNumber(), "yes", "checkbox", s.toString(), "false", "");
                        ne.save();
                        thisnoteelementid[0] = ne.getId();
                        cb_added[0] = true;
                        checklistDelete.setTag(ne.getId());
                    }
                    if (cb_added[0]) {
                        NoteElement ne = NoteElement.findById(NoteElement.class, thisnoteelementid[0]);
                        ne.setContent(s.toString());
                        ne.save();
                        modifyNoteTime();

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checklist_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                drawingControls.setVisibility(View.GONE);
                layout_note_more_Info.setVisibility(View.GONE);
                isMoreShown = false;
                layout_audio_notechooser.setVisibility(View.GONE);
                imageButtoncalander.setVisibility(View.VISIBLE);
                imageButtonHamburg.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(checklist_text, InputMethodManager.SHOW_IMPLICIT);

                // TODO hide
                imageButtoncheckbox.setVisibility(View.GONE);
                imageButtonsquence.setVisibility(View.GONE);
                if (allCheckboxText.size() > 0)
                    allCheckboxText.remove(0);

                allCheckboxText.add(v);
            }
        });

        checklist_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();

                NoteElement ne = NoteElement.findById(NoteElement.class, thisnoteelementid[0]);

                if (tag.equals("1")) {
                    checklist_icon.setImageResource(R.drawable.ic_checkbox_uncheck);
                    ne.setContentA("false");
                    checklist_text.setPaintFlags(checklist_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    v.setTag("0");
                } else {
                    checklist_icon.setImageResource(R.drawable.ic_checkbox_check);
                    ne.setContentA("true");
                    checklist_text.setPaintFlags(checklist_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setTag("1");
                }
                ne.save();
                modifyNoteTime();

            }
        });
    }

    public void screenshot() {

        final ProgressDialog progressDialog = new ProgressDialog(NoteMainActivity.this);
        progressDialog.setMessage("Taking Screenshot...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new AsyncTask<Void,Void,String>(){
                    boolean status = false;
                    @Override
                    protected String doInBackground(Void... params) {
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }

                        String filename = takeScreenshot(scrollView);

                        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/NoteShare/NoteShare Images/" + filename);

                        Uri uriToImage = Uri.fromFile(mediaStorageDir);

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                        shareIntent.setType("image/jpeg");
                        startActivity(Intent.createChooser(shareIntent, "Send Screenshot to"));


                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        progressDialog.dismiss();
                    }
                }.execute(null,null,null);
            }


        }, 5000);
    }

    public void showShareActionSheet(View v) {

        final Dialog myDialog = new Dialog(NoteMainActivity.this, R.style.CustomTheme);

        myDialog.setContentView(R.layout.actionsheet_share);
        Button buttonDissmiss = (Button) myDialog
                .findViewById(R.id.buttonDissmiss);

        TextView tvOptions = (TextView) myDialog.findViewById(R.id.tvOptions);
        tvOptions.setTypeface(RegularFunctions.getAgendaBoldFont(this));

        LinearLayout layoutShare = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutShare);
        TextView layoutShareTextView = (TextView) layoutShare
                .findViewById(R.id.textViewSlideMenuName);
        layoutShareTextView.setText("Share");
        layoutShareTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutShareImageView = (ImageView) layoutShare
                .findViewById(R.id.imageViewSlidemenu);
        layoutShareImageView.setImageResource(R.drawable.ic_note_share_dark);


        LinearLayout layoutDelete = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutDelete);
        TextView layoutDeleteTextView = (TextView) layoutDelete
                .findViewById(R.id.textViewSlideMenuName);
        layoutDeleteTextView.setText("Delete");
        layoutDeleteTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutDeleteImageView = (ImageView) layoutDelete
                .findViewById(R.id.imageViewSlidemenu);
        layoutDeleteImageView.setImageResource(R.drawable.ic_note_delete_dark);


        LinearLayout layoutMove = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutMove);
        TextView layoutMoveTextView = (TextView) layoutMove
                .findViewById(R.id.textViewSlideMenuName);
        layoutMoveTextView.setText("Move");
        layoutMoveTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutMoveImageView = (ImageView) layoutMove
                .findViewById(R.id.imageViewSlidemenu);
        layoutMoveImageView.setImageResource(R.drawable.ic_note_move_dark);


        LinearLayout layoutRemind = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutRemind);
        TextView layoutRemindTextView = (TextView) layoutRemind
                .findViewById(R.id.textViewSlideMenuName);
        layoutRemindTextView.setText("Remind");
        layoutRemindTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutRemindImageView = (ImageView) layoutRemind
                .findViewById(R.id.imageViewSlidemenu);
        layoutRemindImageView.setImageResource(R.drawable.ic_note_remainder_dark);


        LinearLayout layoutTimeBomb = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutTimeBomb);
        TextView layoutTimeBombTextView = (TextView) layoutTimeBomb
                .findViewById(R.id.textViewSlideMenuName);
        layoutTimeBombTextView.setText("Timebomb");
        layoutTimeBombTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutTimeBombImageView = (ImageView) layoutTimeBomb
                .findViewById(R.id.imageViewSlidemenu);
        layoutTimeBombImageView.setImageResource(R.drawable.ic_note_timebomb_dark);


        LinearLayout layoutLock = (LinearLayout) myDialog
                .findViewById(R.id.optionLayoutLock);
        TextView layoutLockTextView = (TextView) layoutLock
                .findViewById(R.id.textViewSlideMenuName);
        layoutLockTextView.setText("Lock");
        layoutLockTextView.setTypeface(RegularFunctions.getAgendaMediumFont(this));
        ImageView layoutLockImageView = (ImageView) layoutLock
                .findViewById(R.id.imageViewSlidemenu);
        layoutLockImageView.setImageResource(R.drawable.ic_note_lock_dark);


        layoutShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                share(v);
            }
        });

        layoutDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                delete(v);
            }
        });

        layoutMove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                move(v);
            }
        });

        layoutRemind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                remindClick(v);
            }
        });

        layoutTimeBomb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                timebomb(v);
            }
        });

        layoutLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                passcode(v);
            }
        });


        buttonDissmiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;
        myDialog.show();

        myDialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    public String takeScreenshot(View v) {

        ScrollView scroll = (ScrollView) v;

        int width = scroll.getChildAt(0).getWidth();
        int height = scroll.getChildAt(0).getHeight();

        int blankSpace = pxFromDp(this, 1500);

        Log.e("jay sw", String.valueOf(width));
        Log.e("jay sh", String.valueOf(height - blankSpace));

        int screenShotHeight = height - blankSpace;

        double j = ((double) screenShotHeight) / 200;

        Canvas bitmapCanvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(width, screenShotHeight, Bitmap.Config.ARGB_8888);

        bitmapCanvas.setBitmap(bitmap);

        Log.e("jay bg",background);

        boolean check = background.startsWith("#");

        Log.e("jay bg check", String.valueOf(check));

        if (background.startsWith("#"))
            bitmapCanvas.drawColor(Color.parseColor(background));
        else
            bitmapCanvas.drawColor(Color.parseColor("#ffffff"));

        //bitmapCanvas.scale(1.0f, 3.0f);
        scroll.draw(bitmapCanvas);


        String fileName = noteIdForDetails + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), "/NoteShare/NoteShare Images/" + fileName);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(context, "Screenshot taken", Toast.LENGTH_SHORT).show();

        return fileName;
    }



    private Bitmap decodeFile(File f, float height, float width) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=800;
            final int h = (int) height;
            final int w = (int) width;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= w &&
                    o.outHeight / scale / 2 >= h) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
}