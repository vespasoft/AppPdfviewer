package es.enaire.apptest;

import android.content.Context;
import com.google.common.collect.Iterables;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


public class FileHandler {

    private static final String PATH_SEPARATOR = "_";
    private static final String PDF_EXTENSION = ".pdf";

    private Context context;

    public FileHandler(Context context){
        this.context = context;
    }

    public String createFile(int id, String date, InputStream inputStream) throws Exception{

        String filePath = getFilePath(id, date);
        if(filePath!=null){
            return filePath;
        }

        try {
            File file = new File(generateFilePath(id, date));

            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return file.getAbsolutePath();
            } catch (IOException exception) {
                throw new FileException(exception.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException exception) {
            throw new FileException(exception.getMessage());
        }
    }

    public String getFilePath(int id, String date){
        try{
            String[] fileArray = context.getExternalFilesDir(null).list();
            if(fileArray!=null) {
                List<String> fileList = new ArrayList<>(Arrays.asList(fileArray));
                String fileName = Iterables.find(fileList, input -> input.startsWith(getPrefix(id)));
                if(fileName.contains(date)){
                    return generateFilePath(fileName);
                } else{
                    File file = new File(generateFilePath(fileName));
                    file.delete();
                }
            }
        } catch (NoSuchElementException ignored){ }

        return null;
    }

    private String generateFilePath(int id, String date){
        return getBasePath() + getPrefix(id) + date + PDF_EXTENSION;
    }

    private String generateFilePath(String fileName){
        return getBasePath() + fileName;
    }

    private String getBasePath(){
        return context.getExternalFilesDir(null) + File.separator;
    }

    private String getPrefix(int id){
        return id + PATH_SEPARATOR;
    }

}
