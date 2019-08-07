package Logic;


//import com.sun.jdi.request.ExceptionRequest;
//import com.sun.tools.classfile.Code_attribute;
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
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class GitManager {
    private Repository GITRepository;
    private String userName;

    //    private class diffLogClass {
    private LinkedList<Path> updatedFiles = new LinkedList<Path>();
    private LinkedList<Path> createdFiles = new LinkedList<Path>();
    private LinkedList<Path> deletedFiles = new LinkedList<Path>();
    // }


    public LinkedList<Path> getUpdatedFiles() {
        return updatedFiles;
    }

    public LinkedList<Path> getCreatedFiles() {
        return createdFiles;
    }

    public LinkedList<Path> getDeletedFile() {
        return deletedFiles;
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

    static String generateSHA1FromString(String str) {
        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(str);
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

    public void ExecuteCommit(String description, Boolean isCreateZip) throws Exception {
        Path ObjectPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Objects");
        Path BranchesPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Branches");
        String headBranch = readTextFile(BranchesPath + "\\Head");
        String prevCommitSHA1 = readTextFile(BranchesPath + "\\" + headBranch);//לזה נעשה אןזיפ וגם לקובץ שהשם שלו הוא הsha1 שכתוב פה
        //Date
        //String creationDate = GitManager.getDate();

        Folder newFolder = GenerateFolder(GITRepository.getRepositoryPath());// ייצג את הספרייה הראשית
        Folder oldFolder = GITRepository.getHeadBranch().getPointedCommit().getRootfolder();
        if(!generateSHA1FromString(newFolder.getFolderContentString()).equals(generateSHA1FromString(oldFolder.getFolderContentString()))) {
            createShaAndZipForNewCommit(newFolder, oldFolder, isCreateZip, GITRepository.getRepositoryPath());


            if (isCreateZip) {
                Commit c = new Commit(description, userName);
                GITRepository.getHeadBranch().setPointedCommit(c); //creation
                GITRepository.getHeadBranch().getPointedCommit().setSHA1PreveiousCommit(prevCommitSHA1); //setting old commits sha1
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(newFolder); //setting old commit
                GITRepository.getCommitList().add(GITRepository.getHeadBranch().getPointedCommit()); //adding to commits list of the current reposetory
                GITRepository.getHeadBranch().getPointedCommit().setCommitFileContentToSHA(); //
                createFile(GITRepository.getHeadBranch().getBranchName(), GITRepository.getHeadBranch().getPointedCommit().getSHA(), BranchesPath);
                GITRepository.getHeadBranch().setPointedCommitSHA1(c.getSHA());
                createZipFile(ObjectPath,generateSHA1FromString(newFolder.getFolderContentString()),newFolder.getFolderContentString());
                    try{
                createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(),GITRepository.getRepositoryPath());}
                catch (Exception e) {throw new Exception();}
            }
        }
    }

    private void createShaAndZipForNewCommit(Folder newFolder, Folder oldFolder, Boolean isCreateZip, Path path) throws IOException {
        ArrayList<Folder.Component> newComponents = newFolder.getComponents();
        ArrayList<Folder.Component> oldComponents;
        Path objectPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Objects");
        int oldd = 0;
        int neww = 0;
        if ((oldFolder != null)) {
            oldComponents = oldFolder.getComponents();

// indexes of the component in the lists
            // for (int i=0 , j=0; i<= newComponents.size() && j<=oldComponents.size(); i++) {
            while (oldd < oldComponents.size() && neww < newComponents.size()) { // while two folders are not empty
                if (oldComponents.get(oldd).getComponentName().equals(newComponents.get(neww).getComponentName())) { // if names are the same
                    if (oldComponents.get(oldd).getComponentSHA1().equals(newComponents.get(neww).getComponentSHA1())) { //if sha1 is the same
                        //point old object
                        newComponents.set(neww, oldComponents.get(oldd)); // if nothing changed, point at the original tree
                   neww++;
                   oldd++;
                    } else if (oldComponents.get(oldd).getComponentType().equals(newComponents.get(neww).getComponentType())) { //different sha1, updated file
                        if (oldComponents.get(oldd).getComponentType().equals(FolderType.Folder)) {
                            Folder newf = (Folder) newComponents.get(neww).getDirectObject();
                            Folder oldf = (Folder) oldComponents.get(oldd).getDirectObject();

                            // Folder newF = new Folder(newComponents.get(neww));
                            //Folder oldF = new Folder(oldComponents.get(oldd));
                            createShaAndZipForNewCommit(newf, oldf, isCreateZip, Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                            createZipFile(objectPath, generateSHA1FromString(newf.getFolderContentString()), newf.getFolderContentString());
                            neww++;
                            oldd++;
                        } else {
                            //both blob - updated
                            Blob b = (Blob) newComponents.get(neww).getDirectObject();
                            createZipFile(objectPath, newComponents.get(neww).getComponentSHA1(), b.getContent());
                            //add updated file zip
                            //add to path
                            this.updatedFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                            neww++;
                            oldd++;
                        }
                    } else {
                        //one blob one folder - one of them was created with the same name(diff type now)
                        //add new file creeate zip

//                            createZipFile(path,newComponents.get(neww).getComponentSHA1(),newComponents.get(neww).getDirectObject().);
//
//                            //add to list
//                            this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
//                            neww++;
                    }
                } else {
                    int result = newComponents.get(neww).getComponentName().compareTo(oldComponents.get(oldd).getComponentName());
                    if (result > 0) {
                        //file was deleted from old
                        //add to list
                        this.deletedFiles.add(Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                        oldd++;

                    } else {
                        //new file was added
                        //add new zip
                        //createZipFile(path,newComponents.get(neww).getComponentSHA1(),newComponents.get(neww).);

                        //add to list
                        if (newComponents.get(neww).getComponentType().equals(FolderType.Blob)) {
                            Blob b = (Blob) newComponents.get(neww).getDirectObject();
                            createZipFile(objectPath, newComponents.get(neww).getComponentSHA1(), b.getContent());
                        } else {
                            Folder f = (Folder) newComponents.get(neww).getDirectObject();

                            //Folder f = new Folder(newComponents.get(neww));
                            createShaAndZipForNewCommit(f, null, isCreateZip, Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                            createZipFile(objectPath, generateSHA1FromString(f.getFolderContentString()), f.getFolderContentString());

                        }
                        this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                        neww++;
                    }
                }
            }

            while (oldd < oldComponents.size()) {
                if (oldComponents.get(oldd).getComponentType().equals(FolderType.Folder)) {
                    Folder f = (Folder) oldComponents.get(oldd).getDirectObject();

                    //Folder f = new Folder(newComponents.get(neww));
                    createShaAndZipForNewCommit(f, null, isCreateZip, Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                }
                this.deletedFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                oldd++;

            }
        }
        while (neww < newComponents.size()) {
            if (newComponents.get(neww).getComponentType().equals(FolderType.Blob)) {
                Blob b = (Blob) newComponents.get(neww).getDirectObject();
                createZipFile(objectPath, newComponents.get(neww).getComponentSHA1(), b.getContent());
            } else {
                Folder f = (Folder) newComponents.get(neww).getDirectObject();

                //Folder f = new Folder(newComponents.get(neww).getDirectObject().);
                createShaAndZipForNewCommit(f, null, isCreateZip, Paths.get(path.toString() + "\\" + newComponents.get(neww)));
                createZipFile(objectPath, generateSHA1FromString(f.getFolderContentString()), f.getFolderContentString());

            }
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


    private Folder GenerateFolder(Path currentPath) {
        File[] allFileComponents = currentPath.toFile().listFiles();
        String sh1Hex = "";
        String fileContent = "";
        String objectsPath = currentPath + "\\Objects";
        Folder currentFolder = new Folder();

        for (File f : allFileComponents) {
            if (!f.getName().equals(".magit")) {
                if (!f.isDirectory()) {
                    fileContent = readTextFile(f.toString());
                    sh1Hex = generateSHA1FromString((fileContent));
                    //לוגית יוצרת את האובייקט שהוא קומפוננט שמתאר בלוב
                    Folder.Component newComponent = new Folder.Component(f.getName(), sh1Hex, FolderType.Blob, userName, f.lastModified());
                    newComponent.setDirectObject(new Blob(fileContent));
                    currentFolder.getComponents().add(newComponent);

                } else {
                    Folder folder = GenerateFolder(Paths.get(f.getPath()));
                    sh1Hex = generateSHA1FromString(folder.stringComponentsToString());

                    Folder.Component newComponent = new Folder.Component(f.getName(), sh1Hex, FolderType.Folder, userName, f.lastModified());
                    newComponent.setDirectObject(new Folder(folder.getComponents()));
                    currentFolder.getComponents().add(newComponent);
                    Collections.sort(currentFolder.getComponents());
                }
            }
        }
        return currentFolder;
    }

    public void CreatBranch(String newBranchName) {
        Path pathOfNewFile= Paths.get(getGITRepository().getRepositoryPath().toString()+"\\"+".magit\\branches\\");
                //Path workingPath = Paths.get(repPath + repName + "\\");
        String nameOfBranch=readTextFile(getGITRepository().getRepositoryPath().toString()+"\\"+".magit\\branches\\Head");//name of main branch
        String sha1OfCurrCommit=readTextFile(getGITRepository().getRepositoryPath().toString()+"\\"+".magit\\branches\\"+nameOfBranch);
        createFile(newBranchName,sha1OfCurrCommit,pathOfNewFile);// a file created in branches

        Branch newBranch= new Branch(newBranchName,GITRepository.getHeadBranch().getPointedCommit().getSHA());
        newBranch.setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());//creating and initialising

        GITRepository.getBranches().add(newBranch);//adding to logic//not good

    }

    public void DeleteBranch(String FileName) throws Exception {
        if(FileName.equals(readTextFile(GITRepository.getRepositoryPath().toString()+"\\"+".magit\\branches\\Head")))
            throw new Exception();
        File file= new File(GITRepository.getRepositoryPath().toString()+"\\"+".magit\\branches\\"+FileName);
        file.delete();//// erasing it physically
        System.out.println("Branch deleted successfully");

        Branch branch=GITRepository.setBranchByName(FileName);
        GITRepository.getBranches().remove(branch);// erasing it logically

    }

    public static void CheckOut() {

    }

    public static void ShowHistoryOfActiveBranch() {

    }

    public void createEmptyRepositoryFolders(String repPath, String repName)
            throws ExceptionInInitializerError, IllegalArgumentException   {
        if (repPath.substring(repPath.length() - 1) != "/") {
            repPath += "\\";
        }
        if (Files.exists(Paths.get(repPath)))//הpath קיים
        {
        if (!Files.exists(Paths.get(repPath + repName))) {
                new File(repPath + repName + "\\.magit\\objects").mkdirs();
                new File(repPath + repName + "\\.magit\\branches").mkdirs();
                Path workingPath = Paths.get(repPath + repName + "\\");
                this.GITRepository = (new Repository(workingPath, new Branch("Master")));
                GITRepository.getHeadBranch().setPointedCommit(new Commit());
                GITRepository.getHeadBranch().getPointedCommit().setRootfolder(workingPath.toString());
                GITRepository.getHeadBranch().getPointedCommit().setCommitFileContentToSHA();
                GITRepository.getHeadBranch().setPointedCommitSHA1(GITRepository.getHeadBranch().getPointedCommit().getSHA());
                //Create commit file
                try{
                createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(), workingPath);//commit
                createFileInMagit(GITRepository.getHeadBranch(), workingPath);}
                catch (Exception e) {System.out.println("File creation failed");}
                createFile("Head", "Master", Paths.get(repPath + repName + "\\.magit\\branches"));

                GITRepository.setBranchByName("Master").setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());

//            //create origcommit
                Folder folder = GenerateFolder(GITRepository.getRepositoryPath());
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
                this.userName = "Ädministrator";
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
            }   else throw new IllegalArgumentException(); // the wanted name already exist
        }
            else throw new ExceptionInInitializerError() ; // the wanted path doesnt exist
    }

//מכאן רוצה להוציא שני אקספשנס שונים שכל אחד יסמל בעיה אחרת
    public void switchRepository(Path newRepPath)
            throws ExceptionInInitializerError, UnsupportedOperationException, IllegalArgumentException, IOException  {
        Path checkIfMagit = Paths.get(newRepPath + "\\.magit");
        if (Files.exists(newRepPath)) {
            if (Files.exists(checkIfMagit)) {

                File f = Paths.get(newRepPath.toString() + "\\.magit\\branches\\Head").toFile();
                String content = readTextFile(newRepPath + "\\.magit\\branches\\" + f.getName());
                String name = readTextFile(newRepPath + "\\.magit\\branches\\" + content);

                this.GITRepository = new Repository(newRepPath, new Branch(content));
                GITRepository.Switch(newRepPath);
                Path commitPath = Paths.get( newRepPath + "\\.magit\\objects\\" + name+".zip");
                String commitContent = extractZipFile(commitPath,name);
                BufferedReader br = new BufferedReader(new StringReader(commitContent));
                ArrayList<String> st = new ArrayList<>();
                String a;
                int i = 0;
                while ((a = br.readLine()) != null) {
                    st.add(i,a);
                    i++;
                }
                Commit newCommit = new Commit(st, newRepPath);
                newCommit.setCommitFileContentToSHA();
                newCommit.setRootFolder(GenerateFolder(newRepPath));

                GITRepository.getHeadBranch().setPointedCommit(newCommit);
                this.GITRepository.getRepositorysBranchesObjecets();
                //GITRepository.getRepositoryName() = ךהחליף שם של רפוסיטורי
                //לא יצרנו קומיט שההד יצביע עליו כי אין צורך
                GITRepository.getHeadBranch().setPointedCommitSHA1(newCommit.getSHA());
            } else throw new ExceptionInInitializerError();//exeption forG not being magit

        } else throw new IllegalArgumentException();//exception for not existing

        //create origcommit
        Folder folder = GenerateFolder(GITRepository.getRepositoryPath());
        GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
    }




    private static void createFileInMagit(Object obj, Path path) throws Exception {
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
        else throw new Exception();

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
    public static String extractZipFile(Path path, String fileName) throws IOException
    {
        ZipFile zip = new ZipFile(path.toString());
        ZipEntry entry = zip.entries().nextElement();
        StringBuilder out = getTxtFiles(zip.getInputStream(entry));
        return out.toString();
    }

    private  static StringBuilder getTxtFiles(InputStream in)  {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();//ענביתתתתתתתתתתתתת
        }
        return out;
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

    public static void createFile(String fileName, String fileContent, Path path) { // gets a name for new file,what to right inside, where to put it
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


    public void deleteBranchFromRepository(String branchName) {
        Branch b = GITRepository.setBranchByName(branchName);
        if (b != null) {
            if (!getGITRepository().getHeadBranch().equals(b)) {
                GITRepository.getBranches().remove(b);
            }
        }
    }

    public static String readTextFile(String filePath) {
        String returnValue = "";
        String line;
        try {
            FileReader file = new FileReader(filePath);
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
    public static File getFileFromSHA1(String ShA1, Path path)
    {
        Path objectsPath = Paths.get(path.toString() + "\\objects");
        File f = Paths.get(objectsPath + ShA1 + ".zip").toFile();
        return f;

    }

    @Override
    public String toString() {
        String separator = System.lineSeparator() + "***" + System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append("All the updated files:");
        sb.append(separator);
        for (Path updatedFilePath: updatedFiles){
            sb.append(updatedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append("All the created files:");
        sb.append(separator);
        for (Path addedFilePath: createdFiles) {
            sb.append(addedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append("All the deleted files:");
        sb.append(separator);
        for (Path deletedFilePath: deletedFiles){
            sb.append(deletedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString();
    }

}