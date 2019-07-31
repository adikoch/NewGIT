package Logic;


import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    public Repository getGITRepository() {return GITRepository;}
    public String getUserName() {return userName;}


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

    public void Commit() {

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
            if (Files.exists(checkIfMagit))
                GITRepository.Switch(newRepPath);
            else throw new Exception();//exeption for not being magit

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

    public static void createFileInMagit(Object obj, Path path) throws IOException {
        Class objClass = obj.getClass();
        Path magitPath = Paths.get(path.toString()+ "\\.magit");
        Path objectsPath = Paths.get(magitPath.toString()+ "\\objects");
        Path branchesPath = Paths.get(magitPath.toString()+"\\branches");

        if (Commit.class.equals(obj.getClass())) {
            createCommitZip((Commit)obj, objectsPath);
        } else if (Branch.class.equals(objClass)) {
            Branch branch = (Branch)obj;
            createFile(branch.getBranchName(), branch.getPointedCommit().getSHA(), branchesPath);
        } else if (Folder.class.equals(objClass)) {
            createFolderZip((Folder)obj, objectsPath);
        } else if (Blob.class.equals(objClass)) {
            createBlobZip((Blob)obj, objectsPath);
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

        byte[] data = fileContent.toString().getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();
        out.close();
    }

    public static void createFile(String fileName, String fileContent, Path path) {
        Writer out = null;

        File master = new File(path + "\\" + fileName);
        try {
            out = null;

            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(master)));
            out.write(fileContent);
        } catch (IOException e) {
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}