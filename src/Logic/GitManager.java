package Logic;

import java.io.*;
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

        Folder newFolder = GenerateFolderFromWC(GITRepository.getRepositoryPath());// ייצג את הספרייה הראשית
        Folder oldFolder = GITRepository.getHeadBranch().getPointedCommit().getRootfolder();
        if (!generateSHA1FromString(newFolder.getFolderContentString()).equals(generateSHA1FromString(oldFolder.getFolderContentString()))) {
            createShaAndZipForNewCommit(newFolder, oldFolder, isCreateZip, GITRepository.getRepositoryPath());


            if (isCreateZip) {
                Commit c = new Commit(description, userName);
                String prevprevSHA1 = GITRepository.getHeadBranch().getPointedCommit().getSHA1PreveiousCommit();//האם יש סבא
                if (prevprevSHA1 != null) {
                    c.setSHA1PrevPrevCommit(prevprevSHA1);
                }
                GITRepository.getHeadBranch().setPointedCommit(c); //creation
                GITRepository.getHeadBranch().getPointedCommit().setSHA1PreveiousCommit(prevCommitSHA1); //setting old commits sha1
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(newFolder); //setting old commit
                GITRepository.getHeadBranch().getPointedCommit().setRootFolderSHA1(generateSHA1FromString(newFolder.getFolderContentString()));
                GITRepository.getHeadBranch().getPointedCommit().setCommitFileContentToSHA(); //
                GITRepository.getCommitList().put(GITRepository.getHeadBranch().getPointedCommit().getSHA(), GITRepository.getHeadBranch().getPointedCommit()); //adding to commits list of the current reposetory
                createFile(GITRepository.getHeadBranch().getBranchName(), GITRepository.getHeadBranch().getPointedCommit().getSHA(), BranchesPath);
                GITRepository.getHeadBranch().setPointedCommitSHA1(c.getSHA());
                createZipFile(ObjectPath, generateSHA1FromString(newFolder.getFolderContentString()), newFolder.getFolderContentString());
                try {
                    createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(), GITRepository.getRepositoryPath());
                } catch (Exception e) {
                    throw new Exception();
                }

                try {
                    createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(), GITRepository.getRepositoryPath());
                } catch (Exception e) {
                    throw new Exception();
                }
            }
        }
    }

    private void createShaAndZipForNewCommit(Folder newFolder, Folder oldFolder, Boolean isCreateZip, Path path) throws IOException {
        ArrayList<Folder.Component> newComponents = new ArrayList<>();
        ArrayList<Folder.Component> oldComponents = new ArrayList<>();
        Path objectPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Objects");
        int oldd = 0;
        int neww = 0;

        if ((oldFolder != null) && (newFolder != null)) {
            oldComponents = oldFolder.getComponents();
            newComponents = newFolder.getComponents();
            if (!oldComponents.isEmpty() && !newComponents.isEmpty()) {

// indexes of the component in the lists
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
                        }
                    } else {
                        int result = newComponents.get(neww).getComponentName().compareTo(oldComponents.get(oldd).getComponentName());
                        if (result > 0) {
                            //file was deleted from old
                            //add to list
                            if (oldComponents.get(oldd).getComponentType().equals(FolderType.Folder)) {
                                Folder f = (Folder) oldComponents.get(oldd).getDirectObject();

                                //Folder f = new Folder(newComponents.get(neww));
                                createShaAndZipForNewCommit(null, f, isCreateZip, Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                            }
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
            }
        }
        if (oldFolder != null) {
            oldComponents = oldFolder.getComponents();
            while (oldd < oldComponents.size()) {
                if (oldComponents.get(oldd).getComponentType().equals(FolderType.Folder)) {
                    Folder f = (Folder) oldComponents.get(oldd).getDirectObject();

                    //Folder f = new Folder(newComponents.get(neww));
                    createShaAndZipForNewCommit(null, f, isCreateZip, Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                }
                this.deletedFiles.add(Paths.get(path.toString() + "\\" + oldComponents.get(oldd).getComponentName()));
                oldd++;
            }
        }

        if (newFolder != null) {
            newComponents = newFolder.getComponents();
            while (neww < newComponents.size()) {
                if (newComponents.get(neww).getComponentType().equals(FolderType.Blob)) {
                    Blob b = (Blob) newComponents.get(neww).getDirectObject();
                    createZipFile(objectPath, newComponents.get(neww).getComponentSHA1(), b.getContent());
                } else {
                    Folder f = (Folder) newComponents.get(neww).getDirectObject();

                    //Folder f = new Folder(newComponents.get(neww).getDirectObject().);
                    createShaAndZipForNewCommit(f, null, isCreateZip, Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                    createZipFile(objectPath, generateSHA1FromString(f.getFolderContentString()), f.getFolderContentString());
                }
                this.createdFiles.add(Paths.get(path.toString() + "\\" + newComponents.get(neww).getComponentName()));
                neww++;
            }

        }
    }

    private Folder GenerateFolderFromWC(Path currentPath) {
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
                    Folder.Component newComponent = new Folder.Component(f.getName(), sh1Hex, FolderType.Blob, userName, getDate(f.lastModified()));
                    newComponent.setDirectObject(new Blob(fileContent));
                    currentFolder.getComponents().add(newComponent);

                } else {
                    Folder folder = GenerateFolderFromWC(Paths.get(f.getPath()));
                    sh1Hex = generateSHA1FromString(folder.stringComponentsToString());

                    Folder.Component newComponent = new Folder.Component(f.getName(), sh1Hex, FolderType.Folder, userName, getDate(f.lastModified()));
                    newComponent.setDirectObject(new Folder(folder.getComponents()));
                    currentFolder.getComponents().add(newComponent);
                    Collections.sort(currentFolder.getComponents());
                }
            }
        }
        return currentFolder;
    }

    public void CreatBranch(String newBranchName) {
        Path pathOfNewFile = Paths.get(getGITRepository().getRepositoryPath().toString() + "\\" + ".magit\\branches\\");
        //Path workingPath = Paths.get(repPath + repName + "\\");
        String nameOfBranch = readTextFile(getGITRepository().getRepositoryPath().toString() + "\\" + ".magit\\branches\\Head");//name of main branch
        String sha1OfCurrCommit = readTextFile(getGITRepository().getRepositoryPath().toString() + "\\" + ".magit\\branches\\" + nameOfBranch);//sha1 of main commit
        createFile(newBranchName, sha1OfCurrCommit, pathOfNewFile);// a file created in branches

        Branch newBranch = new Branch(newBranchName, GITRepository.getHeadBranch().getPointedCommit().getSHA());
        newBranch.setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());//creating and initialising

        GITRepository.getBranches().add(newBranch);//adding to logic//not good

    }

    public void DeleteBranch(String FileName) throws Exception {
        if (FileName.equals(readTextFile(GITRepository.getRepositoryPath().toString() + "\\" + ".magit\\branches\\Head")))
            throw new Exception();
        File file = new File(GITRepository.getRepositoryPath().toString() + "\\" + ".magit\\branches\\" + FileName);
        file.delete();//// erasing it physically
        System.out.println("Branch deleted successfully");

        Branch branch = GITRepository.getBranchByName(FileName);
        GITRepository.getBranches().remove(branch);// erasing it logically

    }

    public static void CheckOut() {


    }


    public void createEmptyRepositoryFolders(String repPath, String repName)
            throws ExceptionInInitializerError, IllegalArgumentException {
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
                //GITRepository.getHeadBranch().getPointedCommit().setRootfolder(workingPath.toString());
                GITRepository.getHeadBranch().getPointedCommit().setCommitFileContentToSHA();
                GITRepository.getHeadBranch().setPointedCommitSHA1(GITRepository.getHeadBranch().getPointedCommit().getSHA());
                //Create commit file
                try {
                    createFileInMagit(GITRepository.getHeadBranch().getPointedCommit(), workingPath);//commit
                    createFileInMagit(GITRepository.getHeadBranch(), workingPath);
                } catch (Exception e) {
                    System.out.println("File creation failed");
                }
                createFile("Head", "Master", Paths.get(repPath + repName + "\\.magit\\branches"));

                GITRepository.getBranchByName("Master").setPointedCommit(GITRepository.getHeadBranch().getPointedCommit());

//            //create origcommit
                Folder folder = GenerateFolderFromWC(GITRepository.getRepositoryPath());
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
                this.userName = "Ädministrator";
                GITRepository.getHeadBranch().getPointedCommit().setOrigCommit(folder);
            } else throw new IllegalArgumentException(); // the wanted name already exist
        } else throw new ExceptionInInitializerError(); // the wanted path doesnt exist
    }


    public boolean DoesPathExist(String repPath)
    {
        return Files.exists(Paths.get(repPath));
    }

    private boolean isFileMagit(String repPath)
    {
        return Files.exists(Paths.get(repPath));
    }

    public void switchRepository(Path newRepPath)
            throws ExceptionInInitializerError, UnsupportedOperationException, IllegalArgumentException, IOException {
        Path checkIfMagit = Paths.get(newRepPath + "\\.magit");
        if (Files.exists(newRepPath)) {
            if (Files.exists(checkIfMagit)) {

                File f = Paths.get(newRepPath.toString() + "\\.magit\\branches\\Head").toFile();
                String content = readTextFile(newRepPath + "\\.magit\\branches\\" + f.getName());
                String name = readTextFile(newRepPath + "\\.magit\\branches\\" + content);
                this.GITRepository = new Repository(newRepPath);

                this.GITRepository.getRepositorysBranchesObjecets();
                GITRepository.Switch(newRepPath);
                GITRepository.setHeadBranch(GITRepository.getBranchByName(content));


                getCommitForBranches(newRepPath);
            } else throw new ExceptionInInitializerError();//exeption forG not being magit

        } else throw new IllegalArgumentException();//exception for not existing

    }

    /*
        public void switchRepository(Path newRepPath)
            throws ExceptionInInitializerError, UnsupportedOperationException, IllegalArgumentException, IOException {
        Path checkIfMagit = Paths.get(newRepPath + "\\.magit");
        if (Files.exists(newRepPath)) {
            if (Files.exists(checkIfMagit)) {

                File f = Paths.get(newRepPath.toString() + "\\.magit\\branches\\Head").toFile();
                String content = readTextFile(newRepPath + "\\.magit\\branches\\" + f.getName());
                String name = readTextFile(newRepPath + "\\.magit\\branches\\" + content);
                this.GITRepository = new Repository(newRepPath);

                this.GITRepository.getRepositorysBranchesObjecets();
                GITRepository.Switch(newRepPath);
                GITRepository.setHeadBranch(GITRepository.getBranchByName(content));


                getCommitForBranches(newRepPath);
            } else throw new ExceptionInInitializerError();//exeption forG not being magit

        } else throw new IllegalArgumentException();//exception for not existing

    }
     */

    public void getCommitForBranches(Path newRepPath) throws IOException {
        for (Branch b : GITRepository.getBranches()) {
            Path commitPath = Paths.get(newRepPath + "\\.magit\\objects\\" + b.getPointedCommitSHA1() + ".zip");
            String commitContent = extractZipFile(commitPath);
            BufferedReader br = new BufferedReader(new StringReader(commitContent));
            ArrayList<String> st = new ArrayList<>();
            String a;
            int i = 0;
            while ((a = br.readLine()) != null) {
                st.add(i, a);
                i++;
            }
            Commit newCommit = new Commit(st);

            b.setPointedCommit(newCommit);
            //GITRepository.getRepositoryName() = ךהחליף שם של רפוסיטורי
            //לא יצרנו קומיט שההד יצביע עליו כי אין צורך
            Folder folder = generateFolderFromCommitObject(newCommit.getRootFolderSHA1());
            b.getPointedCommit().setOrigCommit(folder);
            newCommit.setCommitFileContentToSHA();
                br.close();

        }
    }

    public Folder generateFolderFromCommitObject(String rootFolderName) throws IOException {
        Path ObjectPath = Paths.get(GITRepository.getRepositoryPath().toString() + "\\.magit\\Objects");
        String folderContent = extractZipFile(Paths.get(ObjectPath + "\\" + rootFolderName + ".zip"));
        Folder currentFolder = new Folder();

        currentFolder.setComponents(currentFolder.setComponentsFromString(folderContent));

        for (Folder.Component c : currentFolder.getComponents()) {

            if (c.getComponentType().equals(FolderType.Blob)) {
                String blocContent = extractZipFile(Paths.get(ObjectPath + "\\" + c.getComponentSHA1() + ".zip"));
                Blob b = new Blob(blocContent);
                c.setDirectObject(b);
            } else {
                Folder folder = generateFolderFromCommitObject(c.getComponentSHA1());
                Collections.sort(folder.getComponents());
                c.setDirectObject(folder);
            }
        }
        return currentFolder;
    }


    private static void createFileInMagit(Object obj, Path path) throws Exception {
        Class objClass = obj.getClass();
        Path magitPath = Paths.get(path.toString() + "\\.magit");
        Path objectsPath = Paths.get(magitPath.toString() + "\\objects");
        Path branchesPath = Paths.get(magitPath.toString() + "\\branches");

        if (obj instanceof Commit) {
            createCommitZip((Commit) obj, objectsPath);
        } else if (obj instanceof Branch) {
            Branch branch = (Branch) obj;
            createFile(branch.getBranchName(), branch.getPointedCommit().getSHA(), branchesPath);
        } else if (obj instanceof Folder) {
            createFolderZip((Folder) obj, objectsPath);
        } else if (obj instanceof Blob) {
            createBlobZip((Blob) obj, objectsPath);
        } else throw new Exception();

    }

    private static void createCommitZip(Commit commit, Path path) throws IOException {

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

    public static String extractZipFile(Path path) throws IOException {
        ZipFile zip = new ZipFile(path.toString());
        ZipEntry entry = zip.entries().nextElement();
        StringBuilder out = getTxtFiles(zip.getInputStream(entry));
        return out.toString();
    }

    private static StringBuilder getTxtFiles(InputStream in) throws IOException {
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
        finally {
            reader.close();
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
            out.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }

    }

    public String getAllBranches() {
        StringBuilder toReturn = new StringBuilder();
        for (Branch b : GITRepository.getBranches()) {
            toReturn.append("Branch name: ");
            toReturn.append(b.getBranchName());
            toReturn.append(System.lineSeparator());
            toReturn.append(b.getPointedCommit().getSHAContent());
            toReturn.append(System.lineSeparator());
            toReturn.append(System.lineSeparator());
        }
        return toReturn.toString();
    }


    public void ShowHistoryActiveBranch() {
        String sha1OfMainBranch = GITRepository.getHeadBranch().getPointedCommit().getSHA();
        ShowHistoryActiveBranchRec(sha1OfMainBranch);
    }

    public void ShowHistoryActiveBranchRec(String sha1OfMainBranch) {
        Commit com = GITRepository.getCommitList().get(sha1OfMainBranch);
        if (com == null)// if first commit ever
        {
            return;
        }
        System.out.println(com.getSHAContent());// printing the one i got
        System.out.println(System.lineSeparator());
        ShowHistoryActiveBranchRec(com.getSHA1PreveiousCommit());

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

    public static String getDate(Object date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        if (date != null) {
            return dateFormat.format(date);
        }
        return dateFormat.format(new Date());

    }

    public static File getFileFromSHA1(String ShA1, Path path) {
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
        for (Path updatedFilePath : updatedFiles) {
            sb.append(updatedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append("All the created files:");
        sb.append(separator);
        for (Path addedFilePath : createdFiles) {
            sb.append(addedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append("All the deleted files:");
        sb.append(separator);
        for (Path deletedFilePath : deletedFiles) {
            sb.append(deletedFilePath.toString());
            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public void executeCheckout(String branchName) throws Exception {
        Branch b = GITRepository.getBranchByName(branchName);
        GITRepository.setHeadBranch(b);
        deleteFilesInWC(GITRepository.getRepositoryPath().toFile());
        Commit c = GITRepository.getHeadBranch().getPointedCommit();
        createFilesInWCFromCommitObject(c.getRootFolder(), GITRepository.getRepositoryPath());
    }

    public void deleteFilesInWC(File mainFile) {
        File[] allFileComponents = mainFile.listFiles();
        for (File f : allFileComponents) {
            if (!f.getName().equals(".magit")) {
                if (f.isDirectory()) {
                    deleteFilesInWC(f);
                } else {
                    f.delete();
                }
            }
        }
        mainFile.delete();
    }

    public void createFilesInWCFromCommitObject(Folder rootFolder, Path pathForFile) throws Exception {
        //
        for (Folder.Component c : rootFolder.getComponents()) {

            if (c.getComponentType().equals(FolderType.Blob)) {
                Blob b = (Blob)c.getDirectObject();
               createFile(c.getComponentName(),b.getContent(),pathForFile);
            } else {
                Folder f = (Folder)c.getDirectObject();
                createFilesInWCFromCommitObject(f,Paths.get(pathForFile.toString() + "\\" + c.getComponentName()));
                            }
        }
    }

    //inbar, need to debug

    public String showFilesOfCommit() throws IOException {
        Commit commit= GITRepository.getHeadBranch().getPointedCommit();
        //build a folder that represents the commit
        Folder folder= generateFolderFromCommitObject(commit.getSHA());
        return showFilesOfCommitRec(folder, "");
    }


                                    //commitFolder
    public String showFilesOfCommitRec(Folder rootFolder, String toPrint){
        //
        StringBuilder builder= new StringBuilder();
        builder.append(toPrint);
        for (Folder.Component c : rootFolder.getComponents()) {

            if (c.getComponentType().equals(FolderType.Blob)) {
                builder.append(c.getComponentsStringFromComponent());
                return builder.toString();
                //add blob component to string, return;
            } else {
                builder.append(c.getComponentName());
                builder.append(":");
                builder.append(System.lineSeparator());
                builder.append(c.getComponentsStringFromComponent());
                builder.append(showFilesOfCommitRec((Folder)c.getDirectObject(), toPrint));
            }
        }
        builder.append(System.lineSeparator());
        return builder.toString();

    }

}

//אקספשנים
//הפונקציה של אופציה 4 בתפריט V
//לבדוק אם עשיתי סוויצ רפוזטורי עדיין מדפיס לי V
//אם עושים סוויצ רפוזטורי פעולה 11 לא עובדת, יכול להיות בגלל 2 סיבות: או שאין קישור בין קומיט לאבא שלו באובייקט עצמו, או שבסווית רפוזטורי לא מעדכנות את ההד להצביע על הקומיט הנחוץ