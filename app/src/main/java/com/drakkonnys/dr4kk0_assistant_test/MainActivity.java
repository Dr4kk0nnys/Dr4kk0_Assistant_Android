package com.drakkonnys.dr4kk0_assistant_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.drakkonnys.dr4kk0_assistant_test.Modules.Database;
import com.drakkonnys.dr4kk0_assistant_test.Modules.Helper;
import com.drakkonnys.dr4kk0_assistant_test.Modules.QuickMethods;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Database database;
    private QuickMethods quickMethods;
    private Helper helper;

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        quickMethods = new QuickMethods();
        helper = new Helper();


        GetLocalTime();

        final TextView commandHistoric = findViewById(R.id.commandHistoric); // Main output
        commandHistoric.setMovementMethod(new ScrollingMovementMethod());

        final EditText commandInput = findViewById(R.id.commandInput); // Main input
        commandInput.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // If the user presses "enter"

                    String[] commands = commandInput.getText().toString().split(" ");

                    PrintToConsole(commands);
                    IdentifyCommand(commands);

                    commandInput.setText(""); // Cleaning the input
                }
                return false;
            }
        });

        final ImageButton voiceRecognitionButton = findViewById(R.id.voiceRecognitionButton);
        voiceRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                voiceRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                voiceRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                voiceRecognizer.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

                try {
                    startActivityForResult(voiceRecognizer, REQ_CODE_SPEECH_OUTPUT);
                } catch (Exception e) {
                    PrintToConsole("Unable to listen!", false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> voiceText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            assert voiceText != null;

            /* Handling the data:

                - The first index of voiceText ( 0 )
                - Corresponds to the command with the highest chance
                - Suppose the user says " Hi ", it'll return an array with -> { "Hi", "High", "Hit", "Hip" }
                - But we only want the first index ( 0 )["Hi"]
             */

            String[] commands = voiceText.get(0).toLowerCase().split(" ");

            PrintToConsole(commands);
            IdentifyCommand(commands);
        } else
            PrintToConsole("Unable to read the voice", false);
    }

    private void IdentifyCommand(String[] commands) {
        switch (commands[0]) {
            case "print":
            case "echo":
                /*
                    - useCase: "echo {parameters}"
                    - parameters: {String[] arguments}
                    - possible exception: none
                    - output: true
                 */

                PrintToConsole(SanitizeData(commands), false);
                break;

            case "clear":
            case "cl":
                /*
                    - useCase: "cl"
                    - parameters: none
                    - possible exception: none
                    - output: false
                 */

                TextView commandHistoric = findViewById(R.id.commandHistoric);
                commandHistoric.setText("");
                break;

            case "time":
                /*
                    - useCase: "time"
                    - parameters: none
                    - possible exception: none
                    - output: true
                 */

                GetLocalTime();
                break;

            case "search":
                /*
                    - useCase: "search {arguments}"
                    - parameters: {String[] arguments}
                    - possible exception: none
                    - output: false
                 */

                WebModule("https://www.google.com/search?q=", commands, " ");
                break;

            case "wikipedia":
                /*
                    - useCase: "wikipedia {arguments}"
                    - parameters: {String[] arguments}
                    - possible exception: none
                    - output: false
                 */

                WebModule("https://en.m.wikipedia.org/wiki/", commands, "_");
                break;

            case "translate":
                /*
                    - useCase: "translate {arguments}"
                    - parameters: {String[] arguments}
                    - possible exception: none
                    - output: false
                 */

                WebModule("https://translate.google.com/#view=home&op=translate&sl=auto&tl=en&text=", commands, "%20");
                break;

            case "math":
                /*
                    - useCase: "math {number_1} {operation} {number_2}"
                    - parameters: {int number_1, String operation, int number_2}
                    - possible exception: true
                    - output: true
                 */

                try {
                    PrintToConsole(quickMethods.MathModule(commands[2], Double.parseDouble(commands[1]), Double.parseDouble(commands[3])), false);
                } catch (Exception exception) {
                    PrintErrorMessage(exception);
                }
                break;

            case "table":
                /*
                    - useCase: "table {number}"
                    - parameters: {int number}
                    - possible exception: true
                    - output: true
                 */

                if (commands.length == 2) {

                    StringBuilder line = new StringBuilder();

                    for (int i = 1; i <= 10; i++) {
                        line.append(commands[1]).append(" x ").append(i).append(" = ").append(Integer.parseInt(commands[1]) * i).append("\n"); // 5 x i = (5*i)
                    }

                    PrintToConsole(line.toString(), false);

                } else PrintToConsole("-----Incorrect use-----", false);

                break;

            case "miles":
            case "miletokm":
                /*
                    - useCase: "miles | miletokm {value}"
                    - parameters: {double value}
                    - possible exception: true
                    - output: true
                 */

                try {
                    PrintToConsole(commands[1] + " miles: " + (Float.parseFloat(commands[1]) * 1.609) + " km", false);
                } catch (Exception e) {
                    PrintErrorMessage(e);
                }

                break;

            case "km":
            case "kmtomile":
                 /*
                    - useCase: "km | kmtomile {value}"
                    - parameters: {double value}
                    - possible exception: true
                    - output: true
                 */

                try {
                    PrintToConsole(commands[1] + " km: " + (Float.parseFloat(commands[1]) / 1.609) + " miles", false);
                } catch (Exception e) {
                    PrintErrorMessage(e);
                }

                break;

            case "fahrenheit":
            case "ftoc":
                /*
                    - useCase: "fahrenheit | ftoc {value}"
                    - parameters: {double value}
                    - possible exception: true
                    - output: true
                 */

                try {
                    PrintToConsole(commands[1] + " fahrenheit: " + (Float.parseFloat(commands[1]) - 32) * 5 / 9 + " celsius", false);
                } catch (Exception e) {
                    PrintErrorMessage(e);
                }

                break;

            case "celsius":
            case "ctof":
                /*
                    - useCase: "celsius | ctof {value}"
                    - parameters: {double value}
                    - possible exception: true
                    - output: true
                 */

                try {
                    PrintToConsole(commands[1] + " celsius: " + (Float.parseFloat(commands[1]) * 9 / 5 + 32) + " fahrenheit", false);
                } catch (Exception e) {
                    PrintErrorMessage(e);
                }

                break;

            case "database":
            case "db":
                /*
                    - useCase: "database | db {read}"
                    - useCase: "database | db {add} {@String value}"
                    - useCase: "database | db {update} {@Integer id} {@String value}"
                    - useCase: "database | db {remove} {@Integer id}"

                    - parameters: {...}
                    - possible exception: true
                    - output: true
                 */

                try {
                    DatabaseModule(commands);
                } catch (Exception e) {
                    PrintErrorMessage(e);
                }

                break;

            case "light":
            case "lumos":
                PrintToConsole(quickMethods.FlashLight(this, true), false);
                break;

                /*
                    - useCase: "lumos | knox"
                    - parameters: none
                    - possible exception: true
                    - output: true
                 */

            case "knox":
            case "nox":
                PrintToConsole(quickMethods.FlashLight(this, false), false);
                break;

            case "alarm":
            case "reminder":
                /*
                    - useCase: "alarm | reminder {hour} {minute}"
                    - useCase: "alarm | reminder {minute}"

                    - parameters: {int hour, int minute}
                    - possible exception: true
                    - output: true
                 */

                startActivity(quickMethods.SetAlarm(commands));
                break;

            case "help":
            case "?":
                if (commands.length == 1)
                    PrintToConsole(helper.getFullHelp(), false);
                else
                    try {
                        for (String help : helper.getHelp(commands[1]))
                            PrintToConsole(help, true);
                    } catch (Exception e) {
                        PrintErrorMessage(e);
                    }
                break;
        }
    }

    private void PrintToConsole(String[] commands) {
        final TextView commandHistoric = findViewById(R.id.commandHistoric);
        commandHistoric.append(quickMethods.PrintToConsole(commands) + "\n");
    }

    private void PrintToConsole(String command, boolean isCommand) {
        final TextView commandHistoric = findViewById(R.id.commandHistoric);
        commandHistoric.append(quickMethods.PrintToConsole(command, isCommand) + "\n");
    }

    private void PrintErrorMessage(Exception e) {
        final TextView commandHistoric = findViewById(R.id.commandHistoric);
        commandHistoric.append(quickMethods.PrintErrorMessage(e) + "\n");
    }

    private String SanitizeData(String[] data) { // Replacement was always " "   //, String replacement) {
        return quickMethods.SanitizeData(data, " ");                  // replacement);
    }

    private void GetLocalTime() {
        PrintToConsole(quickMethods.GetLocalTime(), false);
    }

    private void PopUp(String text) {
        quickMethods.PopUp(text, getApplicationContext());
    }

    private void WebModule(String URL, String[] searchTerms, String replacement) {
        Intent search = quickMethods.WebModule(URL, searchTerms, replacement);

        if (search.resolveActivity(this.getPackageManager()) != null)
            startActivity(quickMethods.WebModule(URL, searchTerms, replacement));
        else PopUp("Unable to complete operation");
    }


    private void DatabaseModule(String[] commands) {
        switch (commands[1]) {
            case "read":
                PrintToConsole(database.Read(), false);
                break;

            case "add":
                PopUp(database.Add(commands));
                break;

            case "update":
                PopUp(database.Update(commands));
                break;

            case "remove":
                PopUp(database.Remove(commands));
                break;
        }
    }
}

// TODO: Make a todo module, integrating both Database and Alarm Module ( add a todo at {time} todo: {Text}
