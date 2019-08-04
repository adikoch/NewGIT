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

public class GitManager {
    private Repository GITRepository;
    private String userName;

    //    private class diffLogClass {
    private LinkedList<Path> updatedFiles = new LinkedList<Path>();
    private LinkedList<Path> createdFiles = new LinkedList<Path>();
    private LinkedList<Path> deletedFile = new LinkedList<Path>();
    // }


    public LinkedList<Path> getUpdatedFiles() {
        return updatedFiles;
    }

    public LinkedList<Path> getCreatedFiles() {
        return createdFiles;
    }

    public LinkedList<Path> getDeletedFile() {
        return deletedFile;
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

    public void ExecuteCommit(String description, Boolean isCreateZip) {
        Path ObjectPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Objects");
        Path BranchesPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Branches");
        String headBranch = readTextFile(BranchesPath + "\\Head");
        String prevCommitSHA1 = readTextFile(BranchesPath + "\\" + headBranch);//לזה נעשה אןזיפ וגם לקובץ שהשם שלו הוא הsha1 שכתוב פה
        String prevCommitContent = readTextFile(ObjectPath + "\\" + prevCommitSHA1);
        //Date
        String creationDate = GitManager.getDate();

        Folder newFolder = GenerateFolderSha1(GITRepository.getRepositoryPath(), creationDate);// ייצג את הספרייה הראשית
        Folder oldFolder = GITRepository.getHeadBranch().getPointedCommit().getRootfolder();
        createShaAndZipForNewCommit(newFolder,oldFolder, isCreateZip,GITRepository.getRepositoryPath());


        if (isCreateZip) {
            GITRepository.getHeadBranch().setPointedCommit(new Commit(description, userName));
            GITRepository.getHeadBranch().getPointedCommit().setSHA1PreveiousCommit(prevCommitSHA1);
            GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(newFolder);
        }
    }

    private void createShaAndZipForNewCommit(Folder newFolder,Folder oldFolder, Boolean isCreateZip, Path path) {
        ArrayList<Folder.Component> newComponents = newFolder.getComponents();
        ArrayList<Folder.Component> oldComponents = oldFolder.getComponents();
        int oldd = 0;
        int neww = 0;

        // for (int i=0 , j=0; i<= newComponents.size() && j<=oldComponents.size(); i++) {
        while (oldd < oldComponents.size() && neww < newComponents.size()) {
            if (!newComponents.get(neww).getComponentName().equals(".magit")) {
                if (!oldComponents.get(oldd).getComponentName().equals(".magit")) {

                    if (oldComponents.get(oldd).getComponentName().equals(newComponents.get(neww).getComponentName())) {
                        if (oldComponents.get(oldd).getComponentSHA1().equals(newComponents.get(neww).getComponentSHA1())) {
                            //point old object
                            newComponents.set(neww, oldComponents.get(oldd));
                            return;
                        } else if (oldComponents.get(oldd).getComponentType().equals(newComponents.get(neww).getComponentType())) {
                            if (oldComponents.get(oldd).getComponentType().equals(FolderType.Folder)) {
                                Folder newF = new Folder(newComponents.get(neww));
                                Folder oldF = new Folder(oldComponents.get(oldd));
                                createShaAndZipForNewCommit(newF, oldF, isCreateZip, Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                            } else {
                                //both blob - updated
                                //createZipFile(path,newComponents.get(neww).getComponentSHA1(),newComponents.get(neww).);
                                //add updated file zip
                                //add to path
                                this.updatedFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));

                            }
                        } else {
                            //one blob one folder - one of them was created with the same name(diff type now)
                            //add new file creeate zip
                            //createZipFile(path,newComponents.get(neww).getComponentSHA1(),newComponents.get(neww).);

                            //add to list
                            this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                            neww++;
                        }
                    } else {
                        Integer result = newComponents.get(neww).getComponentName().compareTo(oldComponents.get(oldd).getComponentName());
                        if (result < 0) {
                            //file was deleted from old
                            oldd++;
                            //add to list
                            this.deletedFile.add(Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                        } else {
                            //new file was added
                            neww++;
                            //add new zip
                            //createZipFile(path,newComponents.get(neww).getComponentSHA1(),newComponents.get(neww).);

                            //add to list
                            this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                        }
                    }
                }

                oldd++;
            }
            neww++;
        }
        while (oldd < oldComponents.size() ) {
            this.deletedFile.add(Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentSHA1()));
            oldd++;
        }
        while ( neww < newComponents.size()) {
            this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
            neww++;

        }
    }
//
//                        if (oldComponents.get(oldd).getComponentType().equals(FolderType.Blob)) {
//
//                            if (!GITRepository.getSHA1Map().containsKey(c.getFolderSHA1())) {
//                                //add to path list
//                                //להוסיף בדיקה האם נוסף או התעדכן
//                                this.updatedFiles.add(path);
//                                if (isCreateZip) {
//                                    //create zip
//                                    //add to map
//                                    try {
//                                        createFileInMagit(c, path);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    GITRepository.getSHA1Map().put(c.getFolderSHA1(), path);
//                                }
//                            }
//
//                        } else {
//                            Folder f = new Folder(c);
//                            createShaAndZipForNewCommit(f, isCreateZip, Paths.get(path.toString() + "\\" + c.getFolderName()));
//
//                            sh1Hex = generateSHA1FromString(newFolder.stringComponentsToString());
//                            if (!GITRepository.getSHA1Map().containsKey(sh1Hex)) {
//                                //add to path list
//                                //להוסיף בדיקה האם נוסף או התעדכן
//                                this.updatedFiles.add(path);
//                                if (isCreateZip) {
//                                    //create zip
//                                    //add to map
//                                    try {
//                                        createFileInMagit(c, path);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    GITRepository.getSHA1Map().put(c.getFolderSHA1(), path);
//                                }
//                            }
//                        }
//                    }
//                }

//        }


    private Folder GenerateFolderSha1(Path currentPath, String dateModified) {
        File[] allFileComponents = currentPath.toFile().listFiles();
        String sh1Hex = "";
        String fileContent = "";
        String objectsPath = currentPath + "\\Objects";
        Folder currentFolder = new Folder();

        for (File f : allFileComponents) {
            if (!f.getName().equals(".magit")) {
                if (!f.isDirectory()) {
                    fileContent = readTextFile(f.toString());
                    sh1Hex = DigestUtils.sha1Hex(fileContent);
                    //לוגית יוצרת את האובייקט שהוא קומפוננט שמתאר בלוב
                    currentFolder.getComponents().add(new Folder.Component(f.getName(), sh1Hex, "Blob", userName, dateModified));
                    //objects פיזית בתוך התיקייה
                    /*try {
                        createBlobZip(new Blob(fileContent), currentPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     */
                } else {
                    Folder folder = GenerateFolderSha1(Paths.get(f.getPath()), dateModified);
                    sh1Hex = generateSHA1FromString(folder.stringComponentsToString());
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

        return currentFolder;
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
            this.GITRepository = (new Repository(workingPath , new Branch("Master")));
            GITRepository.getHeadBranch().setPointedCommit(new Commit());
            GITRepository.getHeadBranch().getPointedCommit().setRootfolder(workingPath.toString());
            GITRepository.getHeadBranch().getPointedCommit().setCommitFileContentToSHA();
//Create commit file

            createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(), workingPath);//commit
            createFileInMagit(GITRepository.getHeadBranch(), workingPath);
            createFile("Head", "Master", Paths.get(repPath + repName + "\\.magit\\branches"));

            GITRepository.setBranchByName("Master").setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());

//            //create origcommit
           Folder folder = GenerateFolderSha1(GITRepository.getRepositoryPath(), GitManager.getDate());
            GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
            this.userName = "Ädministrator";
            GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
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

        //create origcommit
        Folder folder = GenerateFolderSha1(GITRepository.getRepositoryPath(), GitManager.getDate());
        GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
    }


    public void CreateNewBranch(String newBranchName) throws FileAlreadyExistsException {
        for (Branch X : GITRepository.getBranches()) {
            if (X.getBranchName() == newBranchName) {
                throw new FileAlreadyExistsException("This Branch is already exist!");
            } else {
                Branch newB = new Branch(newBranchName);
                GITRepository.getBranches().add(newB);
                newB.setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());
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
//        String content = commit.getCommitFileContent();
//        StringBuilder b = new StringBuilder();
//        b.append(GitManager.generateSHA1FromString(path.toString()));
//        b.append(System.lineSeparator());
//
//        b.append(content);

//        String SHA = generateSHA1FromString(b.toString());

        createZipFile(path, commit.getSHA(), commit.getSHAContent());
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

        File file = new File(path + "\\" + fileName);
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file)));
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

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}