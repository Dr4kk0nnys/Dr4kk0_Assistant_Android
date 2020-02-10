package com.drakkonnys.dr4kk0_assistant_test.Modules;

import android.content.Context;
import android.database.Cursor;

public class Database {

    private DatabaseHelper databaseHelper;
    private Cursor data;

    public Database(Context context) {
        databaseHelper = new DatabaseHelper(context); // Calling the constructor before running, to insert the data
        data = databaseHelper.getAllData();

        // Resets the id value, if the database is empty
        if (data.getCount() < 1 ) databaseHelper.ResetIdValue();
    }

    public String Read() {
        data = databaseHelper.getAllData();
        if (data.getCount() == 0) return "Data not found";
        else {
            StringBuilder item = new StringBuilder();

            // Runs through the entire data base, while it has items ...
            while (data.moveToNext()) {
                item.append("----------\n"); // Aesthetic
                item.append("ID: ").append(data.getString(0)).append("\n");
                item.append("Value: ").append(data.getString(1)).append("\n");
            }

            return item.toString();
        }
    }

    public String Add(String[] commands) {
        boolean isInserted = databaseHelper.InsertData(getSanitizedData(commands));

        if (isInserted) return "Data inserted";
        else return "Data incorrectly inserted";

    }

    public String Update(String[] commands) {
        if (commands.length >= 3) {
            boolean isUpdated = databaseHelper.UpdateData(commands[2], getSanitizedData(commands));

                /*
                    - It takes the id as a parameter
                    - But bear in mind, this id is for identifying the object ONLY
                    - It shouldn't in any way, be changed
                    - Specially because it's not possible
                 */

            if (isUpdated) return "Data updated";
            else return "Data incorrectly updated";
        }
        return "Incorrect use!";
    }

    public String Remove(String[] commands) {
        if (commands.length >= 3) {
            int isChanged = databaseHelper.DeleteData(commands[2]);

            if (isChanged == 1) return "Successfully deleted";
            else return "Couldn't delete";
        }
        return "Incorrect use!";
    }

    private String getSanitizedData(String[] commands) {
        StringBuilder command = new StringBuilder();

        for (int i = 2; i < commands.length; i++) { // removing commands[0] and commands[1]
            command.append(commands[i]).append(" "); // It allow us to add complex phrases, with spaces
        }

        return command.toString().trim();
    }
}
