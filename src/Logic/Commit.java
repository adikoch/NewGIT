package Logic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Commit {
    //private String SHA1;
    private Folder treeRoot;
    private LinkedList<String> SHA1PreveiousCommit;
    private String description;
    private String creationDate;
    private String changer;


    public Commit()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        Date date = new Date();
        treeRoot = new Folder();
        SHA1PreveiousCommit = new LinkedList<>();
        description = "Start The Machine";
        creationDate = dateFormat.format(date);
        changer = "Administrator";
        GitManager.generateSHA1FromString(this.getCommitFileContent());
    }
    public String getCommitFileContent() {
        String delimiter = ", ";
        StringBuilder sb = new StringBuilder();

        sb.append(GitManager.generateSHA1FromString(treeRoot.getFolderContentString()));
        sb.append(System.lineSeparator());
        sb.append(getPreviousCommitsSHAs(delimiter));
        sb.append(System.lineSeparator());
        sb.append(description);
        sb.append(System.lineSeparator());
        sb.append(creationDate);
        sb.append(System.lineSeparator());
        sb.append(changer);
        return sb.toString();
    }

    public String getSHA(){
        return  GitManager.generateSHA1FromString(getCommitFileContent());
    }

    public String getPreviousCommitsSHAs(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for(String sha: this.SHA1PreveiousCommit) {
            sb.append(sha);
            sb.append(delimiter);
        }
        sb.delete(sb.length()-delimiter.length()-1, sb.length()-1);

        return sb.toString();
    }

public Commit(String comment)
{

}

    public Commit(File file)
    {

    }

    public void exportToFile()
    {

    }


}
