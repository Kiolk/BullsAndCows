package com.example.notepad.bullsandcows.utils.converters;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;

import java.util.Map;
import java.util.Set;

//TODO rename to clear name, maybe split to bussines entities
public class QueryConverterUtil {

    public static QuerySelectionArgsModel convertSelectionArg(Map<String, String> pSelectionArgs) {


        StringBuilder builderSelection = new StringBuilder();
        Set<String> keys = pSelectionArgs.keySet();

        int size = keys.size();

        String[] arrayKeys = keys.toArray(new String[size]);
        String[] selectionArray = new String[size];

        for (int i = 0; i < pSelectionArgs.size(); ++i) {
            builderSelection.append(arrayKeys[i]);

            if (!arrayKeys[i].equals(UserRecordsDB.ID)) {

                if (!pSelectionArgs.get(arrayKeys[i]).equals("")
                        && !pSelectionArgs.get(arrayKeys[i]).equals("Eny")) {
                    builderSelection.append(" = ?");
                } else {
                    builderSelection.append(" is not ?");
                }

            } else {

                if (pSelectionArgs.get(arrayKeys[i]).equals("Last day")) {
                    builderSelection.append(" < ? ");
                    pSelectionArgs.put(arrayKeys[i], String.valueOf(TimeConvertersUtil.getActualDay(System.currentTimeMillis())));
                } else if (pSelectionArgs.get(arrayKeys[i]).equals("Last week")) {
                    builderSelection.append(" < ? ");
                    pSelectionArgs.put(arrayKeys[i], String.valueOf(TimeConvertersUtil.getActualWeek(System.currentTimeMillis())));
                } else if (pSelectionArgs.get(arrayKeys[i]).equals("Eny")) {
                    builderSelection.append(" is not ? ");
                } else {
                    builderSelection.append(" is not ? ");
                }
            }

            if(i <size -1) {
                builderSelection.append(" and ");
            }

            selectionArray[i] = pSelectionArgs.get(arrayKeys[i]);
        }

        return  new QuerySelectionArgsModel(builderSelection.toString(), selectionArray);
    }
}
