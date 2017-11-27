package com.example.texturecombined.Util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 123 on 2017/11/25.
 */

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, String str){
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream =
                    context.getResources().getAssets().open(str);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                body.append(nextLine);
                body.append('\n');
            }

        }catch (IOException e){
            throw new RuntimeException("Could not open resource: " + str,e);

        }catch (Resources.NotFoundException nfe){
            throw new RuntimeException("Resource not found: " + str,nfe);
        }

        return body.toString();
    }
}
