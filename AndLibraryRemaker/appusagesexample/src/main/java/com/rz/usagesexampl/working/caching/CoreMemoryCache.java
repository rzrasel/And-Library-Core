package com.rz.usagesexampl.working.caching;

import android.content.Context;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <h1>CoreMemoryCache</h1>
 * <p>
 * Use for object memory cache
 * </p>
 *
 * @author Rz Rasel (Md. Rashed - Uz - Zaman)
 * @version 100.00.01
 * @since 2018-12-17
 */

class CoreMemoryCache {
    private static String methodName = "methodName-var";

    public static void setCache(Context argContext, String argCacheKey, Object argObject) throws FileNotFoundException, NoSuchAlgorithmException, ClassNotFoundException, IOException {
        methodName = "static void setCache(Context argContext, String argCacheKey, Object argObject) throws FileNotFoundException, NoSuchAlgorithmException, ClassNotFoundException, IOException";
        String caseKey = "";
        caseKey = getBase64MD5(argCacheKey);
        //caseKey = argCacheKey;
        logPrint("setCache -> dataCacheKey " + caseKey);
        try {
            FileOutputStream fileOutputStream = argContext.openFileOutput(caseKey, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(argObject);
            objectOutputStream.close();
            fileOutputStream.close();
            updateCoreCacheFileList(argContext, caseKey);
        } catch (FileNotFoundException ex) {
            //e.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            //e.printStackTrace();
            throw ex;
        }
    }

    public static Object getCache(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException, ClassNotFoundException, IOException {
        methodName = "static Object getCache(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException, ClassNotFoundException, IOException";
        String caseKey = "";
        caseKey = getBase64MD5(argCacheKey);
        //caseKey = argCacheKey;
        logPrint("getCache -> dataCacheKey " + caseKey);
        Object object = null;
        try {
            FileInputStream fileInputStream = argContext.openFileInput(caseKey);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
            return object;
        } catch (FileNotFoundException ex) {
            //e.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (ClassNotFoundException ex) {
            //ex.printStackTrace();
            throw ex;
        }
    }

    public static boolean onDeleteCache(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        methodName = "static boolean onDeleteCache(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException";
        String caseKey = "";
        caseKey = getBase64MD5(argCacheKey);
        String filePath = onRetrieveDirectoryPath(argContext, caseKey);
        /*String cacheDirectory = argContext.getCacheDir().getParent();
        File file = new File(cacheDirectory, caseKey);*/
        File file = new File(filePath);
        logPrint("onDeleteCache dataCacheKey " + file);
        if (file.exists()) {
            return file.delete();
        } else {
        }
        return false;
    }

    public static void onClearAll(Context argContext) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException, ClassNotFoundException, IOException {
        methodName = "static void onClearAll(Context argContext) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException, ClassNotFoundException, IOException";
        Set<String> coreCacheFileSet;
        String coreCacheFileName = getCoreCacheFileName(argContext);
        Object object = getCache(argContext, coreCacheFileName);
        if (object != null) {
            coreCacheFileSet = (Set<String>) object;
        } else {
            coreCacheFileSet = new HashSet<>();
        }
        logPrint("clearAll -> size " + coreCacheFileSet.size());
        logPrint("clearAll -> data " + coreCacheFileSet.toString());
        for (String item : coreCacheFileSet) {
            String filePath = onRetrieveDirectoryPath(argContext, item);
            //System.out.println("DEBUG_LOG_PRINT: clearAll -> file_path " + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                logPrint("onClearAll dataCacheKey " + file);
                file.delete();
            }
        }
    }

    private static void updateCoreCacheFileList(Context argContext, String argCacheFile) throws FileNotFoundException, NoSuchAlgorithmException, ClassNotFoundException, IOException {
        methodName = "static void updateCoreCacheFileList(Context argContext, String argCacheFile) throws FileNotFoundException, NoSuchAlgorithmException, ClassNotFoundException, IOException";
        Set<String> coreCacheFileSet;
        //List<String> listCache;
        String coreCacheFileName = getCoreCacheFileName(argContext);
        String caseKey = "";
        caseKey = getBase64MD5(coreCacheFileName);
        Object object = getCache(argContext, coreCacheFileName);
        if (object != null) {
            coreCacheFileSet = (Set<String>) object;
        } else {
            coreCacheFileSet = new HashSet<>();
        }
        coreCacheFileSet.add(argCacheFile);
        /*System.out.println("DEBUG_LOG_PRINT: updateCacheFileList -> dataCacheKey " + argCacheFile);
        System.out.println("DEBUG_LOG_PRINT: updateCacheFileList -> dataCacheKey " + caseKey);
        System.out.println("DEBUG_LOG_PRINT: updateCacheFileList -> size " + coreCacheFileSet.size());
        System.out.println("DEBUG_LOG_PRINT: updateCacheFileList -> data " + coreCacheFileSet.toString());*/
        try {
            FileOutputStream fileOutputStream = argContext.openFileOutput(caseKey, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(coreCacheFileSet);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw ex;
        }
    }

    private static String getCoreCacheFileName(Context argContext) {
        methodName = "static String getCoreCacheFileName(Context argContext)";
        String coreCacheListFileName = "rzrasel"
                + "rz_rasel"
                + "core_cache_file_name"
                + argContext.getPackageName()
                + "fly_cache"
                + argContext.getPackageName()
                + "rz_rasel"
                + "cache_file_name"
                + "core_cache_file_name"
                + "fly_cache"
                + "rz_rasel"
                + "rzrasel";
        return coreCacheListFileName;
    }

    public static boolean getCacheTime(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        methodName = "static boolean getCacheTime(Context argContext, String argCacheKey) throws UnsupportedEncodingException, NoSuchAlgorithmException";
        String caseKey = "";
        caseKey = getBase64MD5(argCacheKey);
        String filePath = onRetrieveDirectoryPath(argContext, caseKey);
        /*String cacheDirectory = argContext.getCacheDir().getParent() + "";
        File file = new File(cacheDirectory, caseKey);*/
        File file = new File(filePath);
        logPrint("getCacheTime dataCacheKey " + file);
        if (file.exists()) {
            //return file.delete();
            long timestamp = file.lastModified();
            logPrint("getCacheTime timestamp " + timestamp);
            logPrint("getCacheTime timestamp " + System.currentTimeMillis());
            logPrint("dataCacheKey last modified date = " + new Date(timestamp));
        } else {
            logPrint("getCacheTime file not found");
        }
        return false;
    }

    private static String onRetrieveDirectoryPath(Context argContext, String argCacheKey) {
        methodName = "static String onRetrieveDirectoryPath(Context argContext, String argCacheKey)";
        String cacheDirectory = argContext.getCacheDir().getParent() + "";
        File file = new File(cacheDirectory);
        File[] files = file.listFiles();
        for (File item : files) {
            if (item.isDirectory()) {
                file = new File(item, argCacheKey);
                //System.out.println("DEBUG_LOG_PRINT: onRetriveDirectoryPath " + file + "");
                if (file.exists()) {
                    //System.out.println("DEBUG_LOG_PRINT: onRetriveDirectoryPath found " + file);
                    return file + "";
                }
            }
        }
        return "";
    }

    private static String getBase64MD5(String argString) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        methodName = "static String getBase64MD5(String argString) throws UnsupportedEncodingException, NoSuchAlgorithmException";
        try {
            //argString = argString.replaceAll("\\s+", "");
            //argString = argString.replaceAll("(\\\\+|/+)", Matcher.quoteReplacement(System.getProperty("file.separator")));
            /*argString = argString.replaceAll("/", "");
            argString = argString.replaceAll("\\\\", "");*/
            byte[] byteArray = argString.getBytes("UTF-8");
            String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //base64String = base64String.replaceAll("\\s+", "");
            //base64String = base64String.replaceAll("(\\\\+|/+)", Matcher.quoteReplacement(System.getProperty("file.separator")));
            //base64String = base64String.replaceAll("(?<!^)(\\\\|/)", "");
            /*base64String = base64String.replaceAll("/", "");
            base64String = base64String.replaceAll("\\\\", "");*/
            base64String = getMD5(base64String);
            return base64String;
        } catch (UnsupportedEncodingException ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //return null;
    }

    private static String getMD5(String argString) throws NoSuchAlgorithmException {
        methodName = "static String getMD5(String argString) throws NoSuchAlgorithmException";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(argString.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(String.format("%02x", b & 0xff));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //return null;
    }

    private static void logPrint(String argMessage) {
        methodName = "static void logPrint(String argMessage)";
        System.out.println("DEBUG_LOG_PRINT: " + " " + argMessage);
    }

    private static void createCachedFile(Context argContext) throws IOException {
        String filename = "myfile";
        String fileContents = "Hello world!";
        List<String> list = new ArrayList<>();
        list.add("aaaa");
        list.add("bbbb");
        list.add("cccc");
        list.add("dddd");
        FileOutputStream outputStream;
        outputStream = argContext.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(list);
        oos.close();
        outputStream.close();

        /*try {
            outputStream = argContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private static Object readCachedFile(Context argContext) throws IOException, ClassNotFoundException {
        String filename = "myfile";
        FileInputStream fis = argContext.openFileInput(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    private static void createCachedFile(Context argContext, String argKey, ArrayList<String> argFileName) throws IOException {

        String tempFile = null;
        //File file = new File(Environment.getExternalStorageDirectory() + "/afdadf");
        for (String item : argFileName) {
            FileOutputStream fos = argContext.openFileOutput(argKey, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(argFileName);
            oos.close();
            fos.close();

        }
    }

    //To Read a ArrayList<File>
    private static Object readCachedFile(Context argContext, String argKey) throws IOException, ClassNotFoundException {
        FileInputStream fis = argContext.openFileInput(argKey);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    private static void Serialize(Object object, String filePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            fileOutputStream.close();
            //Logging.log(String.format("SERIALIZED [%s]: %s", object.getClass().getName(), object));
        } catch (NotSerializableException exception) {
            // Output expected NotSerializeableExceptions.
            //Logging.log(exception);
        } catch (IOException exception) {
            // Output unexpected IOException.
            //Logging.log(exception, false);
        }
    }

    /**
     * Deserializes object found in passed filePath.
     *
     * @param filePath Path to file where serialized object is found.
     * @param <T>      Type of object that is expected.
     */
    private static <T> void Deserialize(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            T object = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            //Logging.log(String.format("DESERIALIZED [%s]: %s", object.getClass().getName(), object));
        } catch (NotSerializableException exception) {
            // Output expected NotSerializeableExceptions.
            //Logging.log(exception);
        } catch (IOException | ClassNotFoundException exception) {
            // Output unexpected IOExceptions and ClassNotFoundExceptions.
            //Logging.log(exception, false);
        }
    }
}
/*
FileMemoryCache fileMemoryCache = new FileMemoryCache();
ArrayList<File> fileList = new ArrayList<>();
try {
    fileList.add("test.text");
    fileMemoryCache.createCachedFile(context, "apk", fileList);
} catch (IOException e) {
    e.printStackTrace();
}
readCachedFile(context, "apk");
https://readyandroid.wordpress.com/caching-arraylist-of-custom-objects-in-android-internal-storage/
http://www.vogella.com/tutorials/JavaSerialization/article.html
https://www.wikihow.com/Serialize-an-Object-in-Java
https://www.tutorialspoint.com/java/java_serialization.htm
https://stackoverflow.com/questions/8116147/java-how-to-make-this-serializable
how to serialize an object in java without using serializable interface
java.io.FileNotFoundException: /data/data/com.sm.cattleshurjohms/files/SFRUUFJlcXVlc3RTcGlubmVyQ293SWRMaXN0aHR0cDovc2h1cmpvaG1zLmNvbS9obXNhcGkvY293Lz9mYXJtPTE=
public class Box<T> {
   private T t;
   public void add(T t) {
      this.t = t;
   }
   public T get() {
      return t;
   }
   public static void main(String[] args) {
      Box<Integer> integerBox = new Box<Integer>();
      Box<String> stringBox = new Box<String>();

      integerBox.add(new Integer(10));
      stringBox.add(new String("Hello World"));
      System.out.printf("Integer Value :%d\n\n", integerBox.get());
      System.out.printf("String Value :%s\n", stringBox.get());
   }
}
http://www.technotalkative.com/android-load-images-from-web-and-caching/
*/