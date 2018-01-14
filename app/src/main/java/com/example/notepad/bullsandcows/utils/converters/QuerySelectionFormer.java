package com.example.notepad.bullsandcows.utils.converters;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;

import java.util.Map;
import java.util.Set;

import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.ENY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_WEEK;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.NOT_UPDATED;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.UPDATE;
import static com.example.notepad.bullsandcows.utils.Constants.EMPTY_STRING;

public final class QuerySelectionFormer {

    private static final String AND = " and ";
    private static final String IS_NOT_SMTH = " is not ?";
    private static final String EQUAL_SMTH = " = ?";
    private static final String LESS_THEN = " < ? ";

    public static QuerySelectionArgsModel convertSelectionArg(final Map<String, String> pSelectionArgs) {

        final StringBuilder builderSelection = new StringBuilder();
        final Set<String> keys = pSelectionArgs.keySet();

        final int size = keys.size();

        final String[] arrayKeys = keys.toArray(new String[size]);
        final String[] selectionArray = new String[size];

        for (int i = 0; i < pSelectionArgs.size(); ++i) {
            builderSelection.append(arrayKeys[i]);

            switch (arrayKeys[i]) {
                case UserRecordsDB.IS_UPDATE_ONLINE:

                    if (pSelectionArgs.get(arrayKeys[i]).equals(UPDATE)
                            && pSelectionArgs.get(arrayKeys[i]).equals(NOT_UPDATED)) {
                        builderSelection.append(EQUAL_SMTH);
                    } else {
                        builderSelection.append(IS_NOT_SMTH);
                    }

                    break;
                case UserRecordsDB.ID:

                    if (pSelectionArgs.get(arrayKeys[i]).equals(LAST_DAY)) {
                        builderSelection.append(LESS_THEN);
                        pSelectionArgs.put(arrayKeys[i], String.valueOf(TimeConvertersUtil.getActualDay(System.currentTimeMillis())));
                    } else if (pSelectionArgs.get(arrayKeys[i]).equals(LAST_WEEK)) {
                        builderSelection.append(LESS_THEN);
                        pSelectionArgs.put(arrayKeys[i], String.valueOf(TimeConvertersUtil.getActualWeek(System.currentTimeMillis())));
                    } else if (pSelectionArgs.get(arrayKeys[i]).equals(ENY)) {
                        builderSelection.append(IS_NOT_SMTH);
                    } else {
                        builderSelection.append(IS_NOT_SMTH);
                    }
                    break;
                default:
                    if (!pSelectionArgs.get(arrayKeys[i]).equals(EMPTY_STRING)
                            && !pSelectionArgs.get(arrayKeys[i]).equals(ENY)) {
                        builderSelection.append(EQUAL_SMTH);
                    } else {
                        builderSelection.append(IS_NOT_SMTH);
                    }
                    break;
            }

            if (i < size - 1) {
                builderSelection.append(AND);
            }

            selectionArray[i] = pSelectionArgs.get(arrayKeys[i]);
        }

        return new QuerySelectionArgsModel(builderSelection.toString(), selectionArray);
    }
}
