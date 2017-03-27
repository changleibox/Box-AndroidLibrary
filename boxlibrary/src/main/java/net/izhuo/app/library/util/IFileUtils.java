/**
 * Copyright © All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * @author Box
 * @version v1.0
 * @date 2015年10月15日 上午11:51:36
 * @Description 根据uri获取文件路径
 * @最后修改日期 2015年10月15日 上午11:51:36
 * @修改人 Box
 * @since Jdk1.6 或 Jdk1.7
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class IFileUtils {

    @Deprecated
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentResolver == null) {
                    return null;
                }
                cursor = contentResolver.query(uri, projection, null, null, null);
                if (cursor == null) {
                    return null;
                }
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static class GetPathFromUri4kitkat {

        /**
         * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
         */
        @SuppressLint("NewApi")
        public static String getPath(final Context context, final Uri uri) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            boolean documentUri = false;
            try {
                documentUri = isKitKat && DocumentsContract.isDocumentUri(context, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isKitKat && documentUri) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file
         * path.
         */
        public static String getDataColumn(Context context, Uri uri,
                                           String selection, String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor
                            .getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static boolean saveFile(File file, String filePath, String fileName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            InputStream is;
            OutputStream os;
            try {
                File toFile = new File(filePath);
                if (!toFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    toFile.mkdir();
                }
                is = new FileInputStream(file);
                os = new FileOutputStream(filePath + "/" + fileName, true);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
                is.close();
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public static String getUrlWithQueryString(boolean shouldEncodeUrl, String url) {
        if (url == null) {
            return null;
        } else {
            String paramString;
            if (shouldEncodeUrl) {
                try {
                    paramString = URLDecoder.decode(url, "UTF-8");
                    URL _url = new URL(paramString);
                    URI _uri = new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), _url.getRef());
                    url = _uri.toASCIIString();
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            return url;
        }
    }

    public static long getFileSize(String urlpath) {
        try {
            URL u = new URL(getUrlWithQueryString(true, urlpath));
            HttpURLConnection urlcon = (HttpURLConnection) u.openConnection();
            urlcon.setConnectTimeout(5000);
            urlcon.setRequestMethod("GET");
            urlcon.setRequestProperty("Accept-Encoding", "identity");
            urlcon.setRequestProperty("Referer", urlpath);
            urlcon.setRequestProperty("Charset", "UTF-8");
            urlcon.setRequestProperty("Connection", "Keep-Alive");
            urlcon.connect();
            return urlcon.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0) {
            return null;
        }
        return path.substring(lastDot + 1).toUpperCase(Locale.getDefault());
    }

    public static String getFileName(String path) {
        int lastIndexOf = path.lastIndexOf("/");
        if (lastIndexOf < 0) {
            return null;
        }
        return path.substring(lastIndexOf + 1);
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static boolean cleanCaches(Context context) {
        return cleanFiles(context) && cleanInternalCache(context);
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    @SuppressWarnings("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static boolean cleanFiles(Context context) {
        return deleteDir(context.getFilesDir());
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    @SuppressWarnings("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static boolean cleanInternalCache(Context context) {
        return deleteDir(context.getCacheDir());
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                //noinspection ResultOfMethodCallIgnored
                item.delete();
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }

}
