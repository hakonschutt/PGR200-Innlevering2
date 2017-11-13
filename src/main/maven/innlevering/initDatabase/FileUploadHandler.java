package innlevering.initDatabase;

/**
 * Created by hakonschutt on 22/10/2017.
 */
public class FileUploadHandler {

    public void startInputScan(){
        for (int i = 1; i <= 8; i++){
            String file = getFile(i);
            DataUploadAsThread job = new DataUploadAsThread(file);
            new Thread(job).start();
        }
        System.out.println();
    }

    private String getFile(int fileNr){
        String ext = ".txt";
        String file;

        switch(fileNr){
            case 1:
                file = "day-teach";
                break;
            case 2:
                file = "field-of-study";
                break;
            case 3:
                file = "possible-day";
                break;
            case 4:
                file = "room";
                break;
            case 5:
                file = "study-subject";
                break;
            case 6:
                file = "subject";
                break;
            case 7:
                file = "teacher";
                break;
            case 8:
                file = "teacher-subject";
                break;
            default:
                return null;
        }

        return file + ext;
    }
}
