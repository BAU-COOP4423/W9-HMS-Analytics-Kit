package com.example.analyticskitdemocansu;

import static com.huawei.hms.analytics.type.HAEventType.SUBMITSCORE;
import static com.huawei.hms.analytics.type.HAParamType.SCORE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.crash.AGConnectCrash;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private final int[] questions =
            new int[]{R.string.q1, R.string.q2, R.string.q3, R.string.q4, R.string.q5};
    private final boolean[] answers = new boolean[]{true, true, false, false, true};
    private int curQuestionIdx = 0;
    private TextView txtQuestion;
    private int score = 0;
    HiAnalyticsInstance instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Analytics Kit logging.
        HiAnalyticsTools.enableLog();
        // Generate an Analytics Kit instance.
        instance = HiAnalytics.getInstance(this);

        txtQuestion = findViewById(R.id.question_text_view);
        txtQuestion.setText(questions[curQuestionIdx]);
        Button btnSetting = findViewById(R.id.setting_button);
        btnSetting.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(i, 0);
        });

        Button btnNext = findViewById(R.id.next_button);
        btnNext.setOnClickListener(v -> {
            curQuestionIdx = (curQuestionIdx + 1) % questions.length;
            nextQuestion();
        });

        Button btnTrue = findViewById(R.id.true_button);
        btnTrue.setOnClickListener(v -> {
            checkAnswer(true);
            reportAnswerEvt("true");
        });

        Button btnFalse = findViewById(R.id.false_button);
        btnFalse.setOnClickListener(v -> {
            checkAnswer(false);
            reportAnswerEvt("false");
        });

        Button postScore = findViewById(R.id.post_score_button);
        postScore.setOnClickListener(v -> postScore());

        Button btnCustomReport = findViewById(R.id.CustomReport);
        btnCustomReport.setOnClickListener(v -> {
            AGConnectCrash.getInstance().setUserId("testUser2");
            AGConnectCrash.getInstance().log(Log.DEBUG,"set debug log.");
            AGConnectCrash.getInstance().log(Log.INFO,"set info log.");
            AGConnectCrash.getInstance().log(Log.WARN,"set warning log.");
            AGConnectCrash.getInstance().log(Log.ERROR,"set error log.");
            AGConnectCrash.getInstance().setCustomKey("stringKey", "Hello world");
            AGConnectCrash.getInstance().setCustomKey("booleanKey", false);
            AGConnectCrash.getInstance().setCustomKey("doubleKey", 1.1);
            AGConnectCrash.getInstance().setCustomKey("floatKey", 1.1f);
            AGConnectCrash.getInstance().setCustomKey("intKey", 0);
            AGConnectCrash.getInstance().setCustomKey("longKey", 11L);
        });

        Button btnCrash = findViewById(R.id.btn_crash);
        btnCrash.setOnClickListener(view ->
                AGConnectCrash.getInstance().testIt(MainActivity.this));
    }


    private void nextQuestion() {
        txtQuestion.setText(questions[curQuestionIdx]);
    }

    private void checkAnswer(boolean answer) {
        // String q = txtQuestion.getText().toString().trim();
        if (answer == answers[curQuestionIdx]) {
            score += 20;
            Toast.makeText(this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }
    }

    private void reportAnswerEvt(String answer) {
        // Event Name: Answer
        // Event Parameters:
        //  -- question: String
        //  -- answer: String
        //  -- answerTime: String
        Bundle bundle = new Bundle();
        bundle.putString("question", txtQuestion.getText().toString().trim());
        bundle.putString("answer", answer);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        bundle.putString("answerTime", sdf.format(new Date()));
        // Report a custom event.
        instance.onEvent("Answer", bundle);
    }

    private void postScore() {
        // Initialize parameters.
        Bundle bundle = new Bundle();
        bundle.putLong(SCORE, score);
        // Report a predefined event.
        instance.onEvent(SUBMITSCORE, bundle);
    }
}
