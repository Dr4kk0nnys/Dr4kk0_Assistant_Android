package com.drakkonnys.dr4kk0_assistant_test.Modules;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuickMethods {

    public String PrintToConsole(String[] commands) {
        StringBuilder line = new StringBuilder();

        line.append("$ ");
        for (String command : commands)
            line.append(command).append(" ");

        return line.toString().trim();
    }

    public String PrintToConsole(String command, boolean isCommand) {
        if (isCommand) return "$ " + command;
        else return command;
    }

    public String PrintErrorMessage(Exception e) {
        return "\n\n------Incorrect use!------\n" + e.toString() + "\n\n";
    }

    public String SanitizeData(String[] data, String replacement) {
        StringBuilder sanitizedData = new StringBuilder();

        for (int i = 1; i < data.length; i++) {
            sanitizedData.append(data[i]);

            if (data.length > 2 && i < (data.length - 1))
                sanitizedData.append(replacement);
        }
        return sanitizedData.toString();
    }

    public String GetLocalTime() { // Returns the local hour
        Date date = Calendar.getInstance().getTime();
        return (new SimpleDateFormat("dd/MM/yyyy   HH:mm:ss", Locale.US).format(date));
    }

    public void PopUp(String text, Context context) { // Show a quick pop up in the screen
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    public String FlashLight(Context context, boolean turnedOn) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
        /*
            - In order to access the flashlight, it's needed to check whether the phone has the feature, or not
            - In case it does, it will check if the android version is greater than the 6.0 version ( Marshmallow )
                                                                                                      - API 23.0
            - If the version is greater than 6.0 it ll try to turn the flashlight on
         */

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Checking the api version
                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

                String cameraId;
                try {
                    assert cameraManager != null;
                    cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, turnedOn);
                    return "";
                } catch (Exception e) {
                    return e.toString();
                }
            } else
                return "Unable to turn flashlight on.\nAndroid version needs to be at least 6.0.1 or higher";
        }
        return "Incorrect use!";
    }

    public Intent SetAlarm(String[] commands) {
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

        if (commands.length == 3) { // alarm 10 30 ( 10 hours, and 30 minutes )

            int hours = Integer.parseInt(commands[1]);
            int minutes = Integer.parseInt(commands[2]);

            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Dr4kk0 Assistant");

            if (hours <= 23 && minutes <= 59) return alarmIntent;
            else
                PrintToConsole("Hour has to be < 24, and Minutes has to be < 60", false);
        } else if (commands.length == 2) {
            // alarm 8 ( In 8 minutes )

            Calendar calendar = Calendar.getInstance();

            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int time = minutes + Integer.parseInt(commands[1]);

            if (time > 59) { // If it has to pass to another hour
                double doubleValue = (double) time / 60; // Passing the time to a 60% base

                // doubleValue will be a value like 2.25

                hours += (int) doubleValue;

                // hours += the integer part of doubleValue ( in this case 2 )

                double decimalPart = doubleValue - (int) doubleValue;

                // decimalPart = {doubleValue} - {hours}      2.25 - 2           decimalPart = 0.25

                double value = decimalPart * 60;

                // value = {decimalPart} * 60          0.25 * 60     to return the value into a 60% base ( instead of 100% )

                time = (int) Math.round(value);

                // time = the integer part of value ( if value == 1.4, time = 1
                //                                  ( else if value = 1.6, time = 2 )
            }

            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, time);
            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Dr4kk0 Assistant");

            return alarmIntent;
        }
        return alarmIntent;
    }

    public String MathModule(String operation, double number1, double number2) {
        if (operation.equals("+"))
            return number1 + " " + operation + " " + number2 + " = " + (number1 + number2);
        if (operation.equals("-"))
            return number1 + " " + operation + " " + number2 + " = " + (number1 - number2);
        if (operation.equals("*"))
            return number1 + " " + operation + " " + number2 + " = " + (number1 * number2);
        if (operation.equals("/"))
            return number1 + " " + operation + " " + number2 + " = " + (number1 / number2);
        return "";
    }

    public Intent WebModule(String URL, String[] searchTerms, String replacement) {
        Uri GOOGLE_URL = Uri.parse(URL + SanitizeData(searchTerms, replacement));

        return new Intent(Intent.ACTION_VIEW, GOOGLE_URL);
    }
}
