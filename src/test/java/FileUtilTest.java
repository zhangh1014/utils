import com.lechisoft.utils.file.FileUtil;
import com.lechisoft.utils.file.ListFilesOption;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FileUtilTest {

    @Test
    public void getFiles() {
        List<File> files = FileUtil.listFiles("/Users/zhanghao/Desktop/xxx");
////        files=FileUtil.ignoreExtension(files,"jar");
//        FileUtil.sortByLastModified(files, FileUtil.Direction.ASC);
        for (File file : files) {
            System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())))+file.getPath());
        }
//FileUtil.copyDirectory("/Users/zhanghao/Desktop/xxx/service-uaa","/Users/zhanghao/Desktop/xxx/service-uaa2");

//        try {
//            FileUtil.copyFile("/Users/zhanghao/Movies/一个叫欧维的男人决定去死.2015.简繁中字￡CMCT呆呆/[一个叫欧维的男人决定去死].En.man.som.heter.Ove.2015.BluRay.720p.x264.AC3-CMCT.mkv"
//                    ,"/Users/zhanghao/Movies/x/xx");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//       List<File> files = FileUtil.listFiles("/Users/zhanghao/Desktop/aaa", FileUtil.ListFilesOption.SORT_ASC);
//        for (File file : files) {
//            System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())))+file.getPath());
//        }
//        FileUtil.sortByLastModified(files, FileUtil.ListFilesOption.SORT_ASC);

//        Logger logger = LoggerFactory.getLogger(this.getClass());


//        String s =  FileUtil.read("/Users/zhanghao/Desktop/xxx/eureka");


//        boolean r = FileUtil.rename("/Users/zhanghao/Desktop/aaa/SimpleCode.xlsx","x.xlsx");
//        System.out.println(r);
    }
}
