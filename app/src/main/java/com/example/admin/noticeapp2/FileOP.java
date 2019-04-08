package com.example.admin.noticeapp2;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

public class FileOP {
    Context context;
    public FileOP(Uri filePath) {
    }

    public FileOP(Context context) {
        this.context = context;
    }

    public String GetFileExtension(Uri uri) {
    if(uri == null){return "";}
    ContentResolver contentResolver=context.getContentResolver();
    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

    // Return file Extension
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
}
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
