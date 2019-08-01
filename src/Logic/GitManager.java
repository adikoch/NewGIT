package Logic;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.System.out;

public class GitManager {
    private Repository GITRepository;
    private String userName;

    private class diffLog {
        private List<Path> updatedFiles;
        private List<Path> createdFiles;
        private List<Path> deletedFiles;
    }

    public Repository getGITRepository() {
        return GITRepository;
    }

    public String getUserName() {
        return userName;
    }


    public static String generateSHA1FromFile(File file) {
        String str = file.toString();
        return generateSHA1FromString(str);
    }

    public static String generateSHA1FromString(String str) {
        String sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex(str);
        return sha1;
    }

    public void updateNewUserNameInLogic(String NewUserName) {
        userName = NewUserName;
    }

    public void ImportRepFromXML() {

    }

    public void ShowFilesOfCurrCommit() {

    }

    public void ShowStatus() {


    }

    public void ExecuteCommit(String description) {
        Path ObjectPath= Paths.get(GITRepository.getRepositoryPath().toString()+"/.magit/Objects");
        Path BranchesPath= Paths.get(GITRepository.getRepositoryPath().toString()+"/.magit/Branches");
        String headBranch = readTextFile(BranchesPath+"/Head");
        String prevCommitSha1 = readTextFile(BranchesPath+"/"+headBranch);
        Commit newCommit= new Commit();
        //Date
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        Date date = new Date();
        String creationDate = dateFormat.format(date);
        Folder folder=new Folder();
        String treeRootSha1= Sh1Directory(folder,GITRepository.getRepositoryPath(),creationDate);
        try {
            createFolderZip(folder,ObjectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newCommit.setSHA1PreveiousCommit(prevCommitSha1);
        try {
            createCommitZip(newCommit,ObjectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String Sh1Directory(Folder currentFolder,Path currentPath, String dateModified) {
        File[] allFileComponents = currentPath.toFile().listFiles();
        String sh1Hex = "";
        String fileContent = "";
        String objectsPath= currentPath+"\\Objects";

        for (File f : allFileComponents) {
            if (!f.getName().equals(".magit")) {
                if (!f.isDirectory()) {
                    fileContent = readTextFile(f.toString());
                    sh1Hex = DigestUtils.sha1Hex(fileContent);
                    currentFolder.getComponents().add(new Folder.Component(f.getName(), sh1Hex, "Blob", userName, dateModified));
                    //כאן יהיה if שבודק
                    /*try {
                        createBlobZip(new Blob(fileContent), currentPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     */
                } else {
                    Folder folder = new Folder();
                    sh1Hex = Sh1Directory(folder, Paths.get(f.getPath()), dateModified);
                    currentFolder.getComponents().add(new Folder.Component(
                            f.getName(), sh1Hex, "FOLDER", userName, dateModified));

                    /*try {
                        createFolderZip(folder, Paths.get(objectsPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    //נעשה את החלק הזה במקרה שבו התיקייה או הקובץ השתנו (התעדכנה או נוספה)
                }
            }
        }



        Collections.sort(currentFolder.getComponents());

        return DigestUtils.sha1Hex(currentFolder.toString());
    }

    public void CreatBranch() {

    }

    public void DeleteBranch() {

    }

    public static void CheckOut() {

    }

    public static void ShowHistoryOfActiveBranch() {

    }

    public void createEmptyRepositoryFolders(String repPath, String repName) throws IOException {
        if (repPath.substring(repPath.length() - 1) != "/") {
            repPath += "\\";
        }
        if (!Files.exists(Paths.get(repPath + repName))) {
            new File(repPath + repName + "\\.magit\\objects").mkdirs();
            new File(repPath + repName + "\\.magit\\branches").mkdirs();
            Path workingPath = Paths.get(repPath + repName + "\\");
            this.GITRepository = (new Repository(workingPath, new Branch("Master")));
            GITRepository.getHeadBranch().pointedCommit = new Commit();

//Create commit file

            createFileInMagit(GITRepository.getHeadBranch().pointedCommit, workingPath);
            createFileInMagit(GITRepository.getHeadBranch(), workingPath);
            createFile("Head", "Master", Paths.get(repPath + repName + "\\.magit\\branches"));

            GITRepository.setBranchByName("Master").pointedCommit = GITRepository.getHeadBranch().getPointedCommit();
        }

    }


    public void switchRepository(Path newRepPath) throws Exception {
        Path checkIfMagit = Paths.get(newRepPath + "\\.magit");
        if (Files.exists(newRepPath)) {
            if (Files.exists(checkIfMagit)) {
                File f = Paths.get(newRepPath.toString() + "\\.magit\\branches\\Head").toFile();
                String content;
                    content = readTextFile(newRepPath + "\\.magit\\branches\\" + f.getName());
                String name = readTextFile(newRepPath + "\\.magit\\branches\\" + content);
                this.GITRepository = new Repository(newRepPath, new Branch(content));
                GITRepository.Switch(newRepPath);
                //GITRepository.getRepositoryName() = ךהחליף שם של רפוסיטורי
                //לא יצרנו קומיט שההד יצביע עליו כי אין צורך
            } else throw new Exception();//exeption forG not being magit

        } else throw new Exception();//exception for not existing
    }


    public void CreateNewBranch(String newBranchName) throws FileAlreadyExistsException {
        for (Branch X : GITRepository.getBranches()) {
            if (X.getBranchName() == newBranchName) {
                throw new FileAlreadyExistsException("This Branch is already exist!");
            } else {
                Branch newB = new Branch(newBranchName);
                GITRepository.getBranches().add(newB);
                newB.pointedCommit = GITRepository.getHeadBranch().pointedCommit;
            }

        }

    }

    private static void createFileInMagit(Object obj, Path path) throws IOException {
        Class objClass = obj.getClass();
        Path magitPath = Paths.get(path.toString() + "\\.magit");
        Path objectsPath = Paths.get(magitPath.toString() + "\\objects");
        Path branchesPath = Paths.get(magitPath.toString() + "\\branches");

        if (Commit.class.equals(obj.getClass())) {
            createCommitZip((Commit) obj, objectsPath);
        } else if (Branch.class.equals(objClass)) {
            Branch branch = (Branch) obj;
            createFile(branch.getBranchName(), branch.getPointedCommit().getSHA(), branchesPath);
        } else if (Folder.class.equals(objClass)) {
            createFolderZip((Folder) obj, objectsPath);
        } else if (Blob.class.equals(objClass)) {
            createBlobZip((Blob) obj, objectsPath);
        }
    }

    private static void createCommitZip(Commit commit, Path path) throws IOException {
        String content = commit.getCommitFileContent();
        String SHA = generateSHA1FromString(content);

        createZipFile(path, SHA, content);
    }


    private static void createFolderZip(Folder folder, Path path) throws IOException {
        String content = folder.stringComponentsToString();
        String SHA = generateSHA1FromString(content);

        createZipFile(path, SHA, content);
    }

    private static void createBlobZip(Blob blob, Path path) throws IOException {
        String content = blob.getContent();
        String SHA = generateSHA1FromString(content);

        createZipFile(path, SHA, content);
    }

    private static void createZipFile(Path path, String fileName, String fileContent) throws IOException {
        File f = new File(path + "\\" + fileName + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        ZipEntry e = new ZipEntry(fileName);
        out.putNextEntry(e);

        byte[] data = fileContent.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();
        out.close();
    }

    public static void createFile(String fileName, String fileContent, Path path) {
        Writer out = null;

        File master = new File(path + "\\" + fileName);
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(master)));
            out.write(fileContent);
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void deleteBranchfromRepository(String branchName) {
        Branch b = GITRepository.setBranchByName(branchName);
        if (b != null) {
            if (!getGITRepository().getHeadBranch().equals(b)) {
                GITRepository.getBranches().remove(b);
            }
        }
    }

    public String readTextFile(String fileName) {
        String returnValue = "";
        String line;
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);
            try {
                while ((line = reader.readLine()) != null) {
                    returnValue += line;
                }
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IO Error occured");
        }
        return returnValue;
    }
}










// הפונקציה של ניהול הקבצים רקורסיבית
//מקבלת
// (תאריך,תיקייה במובן המוחשי,תיקייה במובן הלוגי)
/*
private String Sh1Directory(Folder currentFolder,Path currentPath, String dateModified) {
        File[] allFileComponents = currentPath.toFile().listFiles();
        String sh1Hex = "";
        String fileContent = "";
        //Folder currentFolder = new Folder();

        for (File f : allFileComponents) {
            if (!f.getName().equals(".magit")) {
                if (!f.isDirectory()) {
                    fileContent = readTextFile(f.toString());
                    sh1Hex = DigestUtils.sha1Hex(fileContent);
                    if (!Files.exists(Paths.get(currentPath.toString() + f.getName()))) {//if a file with the given sh1 does not exist
                        currentFolder.getComponents().add(new Folder.FolderComponent(
                                f.getName(), sh1Hex, "BLOB", username, dateModified));
                        createNewObjectFile(sh1Hex,fileContent);
                    }
                } else {
                    if (!Files.exists(Paths.get(currentPath.toString() + f.getName()))){//if a folder with the given sh1 does not exist)
                        Folder folder = new Folder();
                        sh1Hex = Sh1Directory(folder,Paths.get(f.getPath()), dateModified);
                        currentFolder.getComponents().add(new Folder.FolderComponent(
                                f.getName(), sh1Hex, "FOLDER", username, dateModified));
                    }
                }
            }
        }
 */