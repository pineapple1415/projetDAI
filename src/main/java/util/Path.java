package util;
import java.io.File;
public class Path {
    public static void main(String[] args) {
        String projectRootPath = new File(System.getProperty("user.dir")).getAbsolutePath();
        String localWebAppPath = projectRootPath + "/src/main/webapp/static/img";
        System.out.println("📂 本地 WebApp 目录: " + localWebAppPath);

    }
}
