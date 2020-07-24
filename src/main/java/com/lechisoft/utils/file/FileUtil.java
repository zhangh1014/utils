package com.lechisoft.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取指定路径的文件
     *
     * @param path 文件路径
     * @return File对象
     * @author zhangh
     * ${DATE}
     */
    public static File getFile(String path) {
        return new File(path);
    }

    /**
     * 获取指定路径文件的属性对象
     *
     * @param path 文件路径
     * @return FileAttributes对象
     * @author zhangh
     * ${DATE}
     */
    public static FileAttributes getFileAttributes(String path) {
        FileAttributes fileAttributes = null;
        try {
            File file = new File(path);
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(path), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            String fullName = file.getName();
            String name = fullName.lastIndexOf(".") == -1 ? fullName : fullName.substring(0, fullName.lastIndexOf("."));
            String extension = fullName.lastIndexOf(".") == -1 ? "" : fullName.substring(fullName.lastIndexOf("."));

            fileAttributes = new FileAttributes();

            fileAttributes.setFullName(fullName);
            fileAttributes.setName(name);
            fileAttributes.setExtension(extension);
            fileAttributes.setPath(path);
            fileAttributes.setParentPath(file.getParent());
            fileAttributes.setFile(attributes.isRegularFile());
            fileAttributes.setDirectory(attributes.isDirectory());
            fileAttributes.setHidden(file.isHidden());
            fileAttributes.setReadable(Files.isReadable(Paths.get(path)));
            fileAttributes.setWritable(Files.isWritable(Paths.get(path)));
            fileAttributes.setExecutable(Files.isExecutable(Paths.get(path)));
            fileAttributes.setSize(attributes.size());
            fileAttributes.setCreationTime(attributes.creationTime().toMillis());
            fileAttributes.setLastAccessTime((attributes.lastAccessTime().toMillis()));
            fileAttributes.setLastModifiedTime(attributes.lastModifiedTime().toMillis());
            fileAttributes.setOwner(Files.getOwner(Paths.get(path), LinkOption.NOFOLLOW_LINKS));

        } catch (IOException e) {
            logger.debug(getLinesString("获取文件属性失败，I/O异常。", path, e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("获取文件属性失败，安全异常。", path, e.getMessage()));
        }
        return fileAttributes;
    }

    /**
     * 设置文件属性
     *
     * @param path      文件路径
     * @param attribute 属性明晨过
     * @param value     属性值
     * @param options   LinkOption
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setFileAttribute(String path, String attribute, Object value, LinkOption... options) {

        try {
            if (options.length == 0) {
                options = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
            }
            Files.setAttribute(Paths.get(path), attribute, value, options);
            return true;
        } catch (UnsupportedOperationException e) {
            logger.debug(getLinesString("设置文件属性失败，属性视图" + Arrays.toString(options) + "不可用。", e.getMessage()));
        } catch (IllegalArgumentException e) {
            logger.debug(getLinesString("设置文件属性失败，不支持的属性" + attribute, e.getMessage()));
        } catch (ClassCastException e) {
            logger.debug(getLinesString("设置文件属性失败，属性值不是预期的类型" + value.getClass().toString(), e.getMessage()));
        } catch (IOException e) {
            logger.debug(getLinesString("设置文件属性失败，文件I/O异常。", e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("设置文件属性失败，文件安全异常。", e.getMessage()));
        }
        return false;
    }

    /**
     * 获取指定路径文件的名称，含扩展名
     *
     * @param path 文件路径
     * @return 文件名称
     * @author zhangh
     * ${DATE}
     */
    public static String getFullName(String path) {
        return getFile(path).getName();
    }

    /**
     * 获取指定路径文件的名称，不含扩展名
     *
     * @param path 文件路径
     * @return 文件名称
     * @author zhangh
     * ${DATE}
     */
    public static String getName(String path) {
        String fullName = getFullName(path);
        return fullName.lastIndexOf(".") == -1 ? fullName : fullName.substring(0, fullName.lastIndexOf("."));
    }

    /**
     * 获取指定路径文件的扩展名
     *
     * @param path 文件路径
     * @return 扩展名
     * @author zhangh
     * ${DATE}
     */
    public static String getExtension(String path) {
        String fullName = getFullName(path);
        return fullName.lastIndexOf(".") == -1 ? "" : fullName.substring(fullName.lastIndexOf("."));
    }

    /**
     * 获取指定路径文件的父路径
     *
     * @param path 文件路径
     * @return 父路径
     * @author zhangh
     * ${DATE}
     */
    public static String getParentPath(String path) {
        return Paths.get(path).getParent().toString();
    }

    /**
     * 获取指定路径文件的拥有者
     *
     * @param path 文件路径
     * @return UserPrincipal
     * @author zhangh
     * ${DATE}
     */
    public static UserPrincipal getOwner(String path) {
        try {
            return Files.getOwner(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
        } catch (UnsupportedOperationException e) {
            logger.debug(getLinesString("获取文件的拥有者失败，属性视图不可用。", e.getMessage()));
        } catch (IOException e) {
            logger.debug(getLinesString("获取文件的拥有者失败，文件I/O异常。", e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("获取文件的拥有者失败，文件安全异常。", e.getMessage()));
        }
        return null;
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 存在返回true，否则为false
     * @author zhangh
     * ${DATE}
     */
    public static boolean exists(String path) {
        return Files.exists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * 判断文件是否不存在
     *
     * @param path 文件路径
     * @return 不存在返回true，否则为false
     * @author zhangh
     * ${DATE}
     */
    public static boolean notExists(String path) {
        return Files.notExists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * 判断路径是否为文件
     *
     * @param path 路径
     * @return 是文件返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isFile(String path) {
        try {
            return Files.isRegularFile(Paths.get(path));
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断是否文件失败，安全异常。", path, e.getMessage()));
            return false;
        }
    }

    /**
     * 判断两个文件是否为同一个
     *
     * @param path1 路径
     * @param path2 路径
     * @return 是同一个返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isSameFile(String path1, String path2) {
        try {
            return Files.isSameFile(Paths.get(path1), Paths.get(path2));
        } catch (IOException e) {
            logger.debug(getLinesString("文件比较失败，I/O异常。", path1, path2));
            return false;
        } catch (SecurityException e) {
            logger.debug(getLinesString("文件比较失败，安全异常。", path1, path2));
            return false;
        }
    }

    /**
     * 判断路径是否为目录
     *
     * @param path 路径
     * @return 是否为目录
     * @author zhangh
     * ${DATE}
     */
    public static boolean isDirectory(String path) {
        try {
            return Files.isDirectory(Paths.get(path));
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断是否目录失败，安全异常。", path, e.getMessage()));
            return false;
        }
    }

    /**
     * 判断文件是否为隐藏文件
     *
     * @param path 路径
     * @return 是隐藏文件返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isHidden(String path) {
        try {
            return Files.isHidden(Paths.get(path));
        } catch (IOException e) {
            logger.debug(getLinesString("判断隐藏文件失败，I/O异常。", path, e.getMessage()));
            return false;
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断隐藏文件失败，安全异常。", path, e.getMessage()));
            return false;
        }
    }

    /**
     * 判断文件是否有读权限
     *
     * @param path 文件路径
     * @return 有权限返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isReadable(String path) {
        try {
            return Files.isReadable(Paths.get(path));
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断是否可读失败，安全异常。", e.getMessage()));
            return false;
        }
    }

    /**
     * 设置指定路径文件是否可读
     *
     * @param path     文件路径
     * @param readable 是否可读
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setReadable(String path, boolean readable) {
        File file = new File(path);
        return file.setReadable(readable);
    }

    /**
     * 判断文件是否有写权限
     *
     * @param path 文件路径
     * @return 有权限返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isWritable(String path) {
        try {
            return Files.isWritable(Paths.get(path));
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断是否可写失败，安全异常。", e.getMessage()));
            return false;
        }
    }

    /**
     * 设置指定路径文件是否可写
     *
     * @param path     文件路径
     * @param readable 是否可写
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setWritable(String path, boolean readable) {
        File file = new File(path);
        return file.setWritable(readable);
    }

    /**
     * 判断文件是否有执行权限
     *
     * @param path 文件路径
     * @return 有权限返回true，否则返回false
     * @author zhangh
     * ${DATE}
     */
    public static boolean isExecutable(String path) {
        try {
            return Files.isExecutable(Paths.get(path));
        } catch (SecurityException e) {
            logger.debug(getLinesString("判断是否可执行失败，安全异常。", e.getMessage()));
            return false;
        }
    }

    /**
     * 设置指定路径文件是否可执行
     *
     * @param path     文件路径
     * @param readable 是否可执行
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setExecutable(String path, boolean readable) {
        File file = new File(path);
        return file.setExecutable(readable);
    }

    /**
     * 获取指定路径文件的创建时间
     *
     * @param path 文件路径
     * @return 创建时间
     * @author zhangh
     * ${DATE}
     */
    public static long getCreationTime(String path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(path), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return attributes.creationTime().toMillis();
        } catch (IOException e) {
            logger.debug(getLinesString("获取创建时间失败，I/O异常。", path, e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("获取创建时间失败，安全异常。", path, e.getMessage()));
        }
        return 0L;
    }

    /**
     * 设置指定路径文件的创建时间
     *
     * @param path 文件路径
     * @param time 创建时间
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setCreationTime(String path, long time) {
        return setFileAttribute(path, "basic:creationTime", FileTime.fromMillis(time));
    }

    /**
     * 获取指定路径文件的最后访问时间
     *
     * @param path 文件路径
     * @return 最后访问时间
     * @author zhangh
     * ${DATE}
     */
    public static long getLastAccessTime(String path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(path), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return attributes.lastAccessTime().toMillis();
        } catch (IOException e) {
            logger.debug(getLinesString("获取最后访问时间失败，I/O异常。", path, e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("获取最后访问时间失败，安全异常。", path, e.getMessage()));
        }
        return 0L;
    }

    /**
     * 设置指定路径文件的最后访问时间
     *
     * @param path 文件路径
     * @param time 创建时间
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setLastAccessTime(String path, long time) {
        return setFileAttribute(path, "basic:lastAccessTime", FileTime.fromMillis(time));
    }

    /**
     * 获取指定路径文件的最后修改时间
     *
     * @param path 文件路径
     * @return 最后修改时间
     * @author zhangh
     * ${DATE}
     */
    public static long getLastModifiedTime(String path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(path), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return attributes.lastModifiedTime().toMillis();
        } catch (IOException e) {
            logger.debug(getLinesString("获取最后修改时间失败，I/O异常。", path, e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("获取最后修改时间失败，安全异常。", path, e.getMessage()));
        }
        return 0L;
    }

    /**
     * 设置指定路径文件的最后修改时间
     *
     * @param path 文件路径
     * @param time 创建时间
     * @return 是否设置成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean setLastModifiedTime(String path, long time) {
        return setFileAttribute(path, "basic:lastModifiedTime", FileTime.fromMillis(time));
    }

    /**
     * 获取系统根路径
     *
     * @return 跟路径迭代器
     * @author zhangh
     * ${DATE}
     */
    public static Iterable<Path> getRootDirectories() {
        return FileSystems.getDefault().getRootDirectories();
    }

    /**
     * 获取系统分隔符
     *
     * @return 分隔符
     * @author zhangh
     * ${DATE}
     */
    public static String getSeparator() {
        return FileSystems.getDefault().getSeparator();
    }

    // -- 创建目录 --

    /**
     * 创建多级目录，并设置权限
     *
     * @param path       目录路径
     * @param permission owners，group，others的权限，如rw-r-x--x
     * @return 是否创建成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean createDirectories(String path, String permission) {
        Set<PosixFilePermission> perms = null;
        FileAttribute<Set<PosixFilePermission>> attr = null;

        if (null != permission && permission.trim().length() > 0) {
            perms = PosixFilePermissions.fromString(permission);
            attr = PosixFilePermissions.asFileAttribute(perms);
        }

        try {
            if (null == attr) {
                Files.createDirectories(Paths.get(path));
            } else {
                Files.createDirectories(Paths.get(path), attr);
            }
            return true;
        } catch (FileAlreadyExistsException e) {
            logger.debug(getLinesString("创建目录失败，目录已存在。", e.getMessage()));
        } catch (UnsupportedOperationException | IOException e) {
            logger.debug(getLinesString("创建目录失败,指定的权限不正确。", e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("创建目录失败，安全异常。", e.getMessage()));
        }
        return false;
    }

    /**
     * 创建多级目录
     *
     * @param path 目录路径
     * @return 是否创建成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean createDirectories(String path) {
        return createDirectories(path, null);
    }


    // -- 列出指定目录路径下的文件 --

    /**
     * 列出指定目录路径下的文件
     *
     * @param path       目录路径
     * @param options    ListFilesOption数组，可取的值为：
     *                   RECURSION 递归
     *                   IGNORE_FILE 忽略文件
     *                   IGNORE_DIRECTORY 忽略目录
     *                   IGNORE_HIDDEN 忽略隐藏
     *                   SORT_LAST_MODIFIED_DESC 最后修改日期由近至远排序
     *                   SORT_LAST_MODIFIED_ASC 最后修改日期由远至近排序
     *                   SORT_LAST_ACCESS_DESC 最后访问日期由近至远排序
     *                   SORT_LAST_ACCESS_ASC 最后访问日期由远至近排序
     *                   SORT_CREATION_DESC 创建日期由近至远排序
     *                   SORT_CREATION_ASC 创建日期由远至近排序
     *                   INCLUDE_EXTENSION 包含扩展名
     *                   EXCLUDE_EXTENSION 排除扩展名
     * @param extensions 扩展名
     * @return 文件列表
     * @author zhangh
     * ${DATE}
     */
    public static List<File> listFiles(String path, ListFilesOption[] options, String... extensions) {
        List<File> files = new ArrayList<>();

        if (isDirectory(path)) {
            boolean isRecursion = false;
            boolean ignoreFile = false;
            boolean ignoreDirectory = false;
            boolean ignoreHidden = false;
            ListFilesOption extensionOption = null;
            ListFilesOption sortOption = null;

            if (null != options) {
                for (ListFilesOption option : options) {
                    switch (option) {
                        case RECURSION:
                            isRecursion = true;
                            break;
                        case IGNORE_FILE:
                            ignoreFile = true;
                            break;
                        case IGNORE_DIRECTORY:
                            ignoreDirectory = true;
                            break;
                        case IGNORE_HIDDEN:
                            ignoreHidden = true;
                            break;
                        case INCLUDE_EXTENSION:
                            extensionOption = ListFilesOption.INCLUDE_EXTENSION;
                            break;
                        case EXCLUDE_EXTENSION:
                            extensionOption = ListFilesOption.EXCLUDE_EXTENSION;
                            break;
                        case SORT_LAST_MODIFIED_DESC:
                            sortOption = ListFilesOption.SORT_LAST_MODIFIED_DESC;
                            break;
                        case SORT_LAST_MODIFIED_ASC:
                            sortOption = ListFilesOption.SORT_LAST_MODIFIED_ASC;
                            break;
                        case SORT_LAST_ACCESS_DESC:
                            sortOption = ListFilesOption.SORT_LAST_ACCESS_DESC;
                            break;
                        case SORT_LAST_ACCESS_ASC:
                            sortOption = ListFilesOption.SORT_LAST_ACCESS_ASC;
                            break;
                        case SORT_CREATION_DESC:
                            sortOption = ListFilesOption.SORT_CREATION_DESC;
                            break;
                        case SORT_CREATION_ASC:
                            sortOption = ListFilesOption.SORT_CREATION_ASC;
                            break;
                    }
                }
            }

            File pathFile = new File(path);
            File[] listFiles = pathFile.listFiles(); // 可能会返回null

            // 排序
            if (null != sortOption) {
                sort(listFiles, sortOption);
            }

            if (null != listFiles) {
                // 遍历目录下所有File
                for (File file : listFiles) {

                    // 如果是文件，且不忽略文件，且没隐藏（或隐藏但不忽略隐藏）
                    if (file.isFile()
                            && !ignoreFile
                            && (!file.isHidden() || !ignoreHidden)) {

                        // 如果不指定扩展过滤
                        if (null == extensionOption) {
                            files.add(file);
                        }
                        // 如果有扩展名，则进行过滤
                        else if (null != extensions && extensions.length > 0) {
                            String fileName = file.getName().toLowerCase();
                            boolean hasFlg = false;

                            for (String ext : extensions) {
                                if (fileName.endsWith(ext.toLowerCase())) {
                                    hasFlg = true;
                                    break;
                                }
                            }

                            // 包含
                            if (extensionOption == ListFilesOption.INCLUDE_EXTENSION && hasFlg) {
                                files.add(file);
                            }

                            // 排除
                            if (extensionOption == ListFilesOption.EXCLUDE_EXTENSION && !hasFlg) {
                                files.add(file);
                            }
                        }
                    }

                    // 如果是目录，且不忽略目录，且没隐藏（或隐藏但不忽略隐藏）
                    if (file.isDirectory()
                            && !ignoreDirectory
                            && (!file.isHidden() || !ignoreHidden)) {
                        if (null == extensionOption) {
                            files.add(file);
                        }
                    }

                    // 递归
                    if (isRecursion && file.isDirectory()) {
                        List<File> subFiles = listFiles(file.getPath(), options, extensions);
                        files.addAll(subFiles);
                    }
                }
            }
        }

        return files;
    }

    /**
     * 列出指定目录路径下的文件
     *
     * @param path    目录路径
     * @param options ListFilesOption数组，可取的值为：
     *                RECURSION 递归
     *                IGNORE_FILE 忽略文件
     *                IGNORE_DIRECTORY 忽略目录
     *                IGNORE_HIDDEN 忽略隐藏
     *                SORT_LAST_MODIFIED_DESC 最后修改日期由近至远排序
     *                SORT_LAST_MODIFIED_ASC 最后修改日期由远至近排序
     *                SORT_LAST_ACCESS_DESC 最后访问日期由近至远排序
     *                SORT_LAST_ACCESS_ASC 最后访问日期由远至近排序
     *                SORT_CREATION_DESC 创建日期由近至远排序
     *                SORT_CREATION_ASC 创建日期由远至近排序
     * @return 文件列表
     * @author zhangh
     * ${DATE}
     */
    public static List<File> listFiles(String path, ListFilesOption... options) {
        return listFiles(path, options, null);
    }

    /**
     * 列出指定目录路径下的文件，包含子文件
     *
     * @param path 目录路径
     * @return 文件列表
     * @author zhangh
     * ${DATE}
     */
    public static List<File> listFiles(String path) {
        return listFiles(path, null, null);
    }

    // -- 拷贝文件或目录 --

    /**
     * 拷贝文件或目录文件自身到指定目录，仅拷贝文件自身
     */
    private static boolean copyFile(String path, String dirPath, boolean copyAttributes, CopyFilesOption option, String rename) {
        String toFileName = (null == rename || rename.trim().length() == 0) ? getFullName(path) : rename;
        Path toPath = Paths.get(dirPath, toFileName);

        // 如果SKIP_EXISTING且存在就跳过，否则都得拷贝
        if (option == CopyFilesOption.SKIP_EXISTING && exists(toPath.toString())) {
            return true;
        }

        List<CopyOption> copyOptions = new ArrayList<CopyOption>();
        copyOptions.add(LinkOption.NOFOLLOW_LINKS);
        if (copyAttributes) {
            copyOptions.add(StandardCopyOption.COPY_ATTRIBUTES);
        }
        if (option == CopyFilesOption.REPLACE_EXISTING) {
            copyOptions.add(StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            Files.copy(Paths.get(path), toPath, copyOptions.toArray(new CopyOption[0]));
            return true;
        } catch (UnsupportedOperationException e) {
            logger.debug(getLinesString("拷贝文件失败，不支持的CopyOption。", e.getMessage()));
        } catch (FileAlreadyExistsException e) {
            logger.debug(getLinesString("拷贝文件失败，文件已存在。", toPath.toString(), e.getMessage()));
        } catch (DirectoryNotEmptyException e) {
            logger.debug(getLinesString("拷贝文件失败，目录不为空。", toPath.toString(), e.getMessage()));
        } catch (IOException e) {
            logger.debug(getLinesString("拷贝文件失败，I/O异常。", toPath.toString(), e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("拷贝文件失败，安全异常。", toPath.toString(), e.getMessage()));
        }
        return false;
    }

    /**
     * 拷贝文件或目录文件到指定目录，如果是目录，则递归拷贝其子文件
     */
    private static boolean copy(String path, String dirPath, boolean copyAttributes, CopyFilesOption option, List<FileAttributes> copied) {
        // 拷贝结果
        boolean r = false;

        if (exists(path)) {
            // 目标文件或目录的路径
            String toPath = Paths.get(dirPath, getFullName(path)).toString();

            // 先拷贝文件或目录文件
            r = copyFile(path, dirPath, copyAttributes, option, null);
            if (r) {
                // 记录已创建的文件或目录
                FileAttributes fileAttributes = getFileAttributes(toPath);
                copied.add(fileAttributes);
            }

            // 再递归目录下的文件
            if (isDirectory(path)) {
                for (File file : listFiles(path)) {
                    r = copy(file.getPath(), toPath, copyAttributes, option, copied);
                    if (!r) {
                        break;
                    }
                }
            }
        }
        return r;
    }

    /**
     * 拷贝文件或目录文件到指定目录
     *
     * @param path           文件路径或目录路径
     * @param dirPath        目录路径
     * @param copyAttributes 是否拷贝属性
     * @param option         REPLACE_EXISTING, SKIP_EXISTING
     * @return 是否拷贝成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean copy(String path, String dirPath, boolean copyAttributes, CopyFilesOption option) {
        // 先创建目录路径
        createDirectories(dirPath);

        List<FileAttributes> copied = new ArrayList<>();
        boolean r = copy(path, dirPath, copyAttributes, option, copied);

        // 如果拷贝出错，删除已拷贝的文件或目录
        if (!r) {
            for (int i = copied.size() - 1; i >= 0; i--) {
                delete(copied.get(i).getPath(), false);
            }
        }
        return r;
    }

    /**
     * 拷贝文件或目录文件到指定目录，如果存在，则覆盖
     *
     * @param path    文件路径或目录路径
     * @param dirPath 目录路径
     * @return 是否拷贝成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean copy(String path, String dirPath) {
        return copy(path, dirPath, true, CopyFilesOption.REPLACE_EXISTING);
    }

    // -- 删除文件或目录 --

    /**
     * 删除文件或目录文件
     */
    private static boolean deleteFile(String path) {
        try {
            // 删除文件或目录文件
            Files.delete(Paths.get(path));
            return true;
        } catch (NoSuchFileException e) {
            logger.debug(getLinesString("删除文件失败，文件不存在。", path, e.getMessage()));
        } catch (DirectoryNotEmptyException e) {
            logger.debug(getLinesString("删除文件失败，目录不为空。", path, e.getMessage()));
        } catch (IOException e) {
            logger.debug(getLinesString("创建目录失败，I/O异常。", path, e.getMessage()));
        } catch (SecurityException e) {
            logger.debug(getLinesString("创建目录失败，安全异常。", path, e.getMessage()));
        }
        return false;
    }

    /**
     * 删除指定的文件或目录，如果是目录的话，递归删除其下所有文件
     */
    private static boolean delete(String path, List<FileAttributes> deleted) {
        boolean r = false;

        if (exists(path)) {
            // 如果是目录的话，先递归删除子文件
            if (isDirectory(path)) {
                for (File file : listFiles(path)) {
                    r = delete(file.getPath(), deleted);
                    if (!r) {
                        // 直接返回false，后面删除文件或目录的代码不再执行了
                        return false;
                    }
                }
            }

            // 删除文件或目录文件
            r = deleteFile(path);
            if (r) {
                FileAttributes fAttr = getFileAttributes(path);
                deleted.add(fAttr);
            }

        }
        return r;
    }

    /**
     * 删除指定目录路径下的文件
     *
     * @param path      目录路径
     * @param safeModel 安全模式下，会先备份，再删除，如果其中某个文件删除失败，则还原已删除的文件
     * @return 是否删除成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean delete(String path, boolean safeModel) {
        boolean r = false;

        // 安全模式下，先备份
        String backupPath = "";
        if (safeModel) {
            backupPath = createBackupDirectory(path);
            copy(path, backupPath);
        }

        // 删除
        List<FileAttributes> deleted = new ArrayList<>();
        r = delete(path, deleted);

        //安全模式下，会产生备份，删除无论成败，都需要把备份删掉
        if (safeModel) {
            // 如果删除失败，先还原
            if (!r) {
                for (int i = deleted.size() - 1; i >= 0; i--) {
                    FileAttributes fileAttributes = deleted.get(i);
                    if (fileAttributes.isFile) {
                        // 获取已删除的文件的备份文件的路径
                        String path2 = Paths.get(backupPath, fileAttributes.getPath().substring(getParentPath(backupPath).length())).toString();
                        copy(path2, fileAttributes.getParentPath());
                    }
                    if (fileAttributes.isDirectory) {
                        createDirectories(fileAttributes.getPath());
                    }
                }
            }
            // 还原完成后，删除备份
            delete(backupPath, false);
        }
        return r;
    }

    /**
     * 删除指定目录路径下的文件
     *
     * @param path 目录路径
     * @return 是否删除成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean delete(String path) {
        return delete(path, false);
    }

    /**
     * 创建备份名称
     */
    private static String createBackupName() {
        return "bak" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 删除指定文件列表的文件，不能是目录文件
     *
     * @param files     文件列表
     * @param safeModel 安全模式，安全模式下，删除前先备份文件，如果删除的其中一个文件出错，则还原
     * @return 是否删除成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean delete(List<File> files, boolean safeModel) {
        // 检查是否为目录文件
        for (File file : files) {
            if (file.isDirectory()) {
                logger.debug(getLinesString("删除文件失败，不能是目录文件。", file.getPath()));
                return false;
            }
        }

        boolean r = false;
        List<FileAttributes> handled = new ArrayList<>();

        for (File file : files) {

            // 如果是安全模式，先备份
            if (safeModel) {
                FileAttributes fileAttributes = getFileAttributes(file.getPath());
                String path = file.getPath();
                String parentPath = file.getParent();
                String backupName = createBackupName();
                copyFile(path, parentPath, true, CopyFilesOption.REPLACE_EXISTING, backupName);
                fileAttributes.setBackupPath(Paths.get(parentPath, backupName).toString());
                handled.add(fileAttributes);
            }

            r = deleteFile(file.getPath());
            if (!r) {
                break;
            }
        }

        // 如果是安全模式，就会产生备份，无论删除成功与否，都需要处理备份的文件
        if (safeModel) {
            for (int i = handled.size() - 1; i >= 0; i--) {
                FileAttributes fAttr = handled.get(i);
                if (!r) {
                    copyFile(fAttr.getBackupPath(), fAttr.getParentPath(), true, CopyFilesOption.SKIP_EXISTING, fAttr.getFullName());
                }
                deleteFile(fAttr.getBackupPath());
            }
        }

        return r;
    }

    /**
     * 删除指定文件列表的文件
     *
     * @param files 文件列表
     * @return 是否删除成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean delete(List<File> files) {
        return delete(files, false);
    }

    /**
     * 创建备份目录
     */
    private static String createBackupDirectory(String path) {
        String backupName = createBackupName();
        String backupPath = Paths.get(getParentPath(path), backupName).toString();
        if (createDirectories(backupPath)) {
            return backupPath;
        }
        return null;
    }


    // -- 重命名文件或目录 --

    /**
     * 重命名文件或目录
     *
     * @param path 文件路径
     * @param name 新名称
     * @return 是否重命名成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean rename(String path, String name) {
        boolean r = false;

        if (exists(path)) {
            String toPath = Paths.get(getParentPath(path), name).toString();
            if (!exists(toPath)) {
                File file = new File(path);
                r = file.renameTo(new File(toPath));
            }
        }
        return r;
    }

    // -- 重命名文件或目录 --

    /**
     * 移动指定路径的文件或目录到指定路径
     *
     * @param path    文件路径
     * @param dirPath 目标路径
     * @return 是否移动成功
     * @author zhangh
     * ${DATE}
     */
    public static boolean move(String path, String dirPath) {
        boolean r = false;

        if (exists(path)) {
            // 目标文件或目录的路径
            String toPath = Paths.get(dirPath, getFullName(path)).toString();
            if (notExists(toPath)) {
                if (copy(path, dirPath)) {
                    if (delete(path)) {
                        r = true;
                    } else {
                        delete(toPath);
                    }
                }
            }
        }
        return r;
    }

    // -- 文件排序 --

    /**
     * 按照最后更新日期排序文件
     *
     * @param files  文件数组
     * @param option 排序方向
     */
    public static void sort(File[] files, ListFilesOption option) {
        if (option == ListFilesOption.SORT_LAST_MODIFIED_DESC
                || option == ListFilesOption.SORT_LAST_MODIFIED_ASC
                || option == ListFilesOption.SORT_LAST_ACCESS_DESC
                || option == ListFilesOption.SORT_LAST_ACCESS_ASC
                || option == ListFilesOption.SORT_CREATION_DESC
                || option == ListFilesOption.SORT_CREATION_ASC) {

            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    FileAttributes fa1 = getFileAttributes(f1.getPath());
                    FileAttributes fa2 = getFileAttributes(f2.getPath());

                    long diff = 0L;
                    if (option == ListFilesOption.SORT_LAST_MODIFIED_DESC
                            || option == ListFilesOption.SORT_LAST_MODIFIED_ASC) {
                        diff = fa1.getLastModifiedTime() - fa2.getLastModifiedTime();
                    }
                    if (option == ListFilesOption.SORT_LAST_ACCESS_DESC
                            || option == ListFilesOption.SORT_LAST_ACCESS_ASC) {
                        diff = fa1.getLastAccessTime() - fa2.getLastAccessTime();
                    }
                    if (option == ListFilesOption.SORT_CREATION_DESC
                            || option == ListFilesOption.SORT_CREATION_ASC) {
                        diff = fa1.getCreationTime() - fa2.getCreationTime();
                    }

                    if (option == ListFilesOption.SORT_LAST_MODIFIED_DESC
                            || option == ListFilesOption.SORT_LAST_ACCESS_DESC
                            || option == ListFilesOption.SORT_CREATION_DESC) {
                        if (diff > 0) {
                            return -1;
                        } else if (diff == 0) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        if (diff > 0) {
                            return 1;
                        } else if (diff == 0) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                }
            });
        }
    }

    /**
     * 按照最后更新日期排序文件
     *
     * @param files  文件列表
     * @param option 排序方向
     */
    public static void sortByLastModified(List<File> files, ListFilesOption option) {
        File[] temp = files.toArray(new File[0]);

        sort(temp, option);

        // 不能直接赋值，否则内部files与外部files指向不同的对象
//        files = Arrays.asList(temp);

        files.clear();
        files.addAll(Arrays.asList(temp));
    }

    // -- 文件读写 --

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return 字节数组
     */
    public static byte[] readBytes(String path) {
        if (isFile(path)) {
            try {
                return Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                logger.debug(getLinesString("读文件失败，I/O异常。", path, e.getMessage()));
            } catch (OutOfMemoryError e) {
                logger.debug(getLinesString("读文件失败，文件不能大于2GB。", path, e.getMessage()));
            } catch (SecurityException e) {
                logger.debug(getLinesString("读文件失败，安全异常。", path, e.getMessage()));
            }
        } else {
            logger.debug(getLinesString("读文件失败，文件不存在或不是文件。", path));
        }
        return null;
    }

    /**
     * 读文件
     *
     * @param path    文件路径
     * @param charset 编码
     * @return 文件内容
     */
    public static String read(String path, Charset charset) {
        byte[] bytes = readBytes(path);
        if (null != bytes) {
            return new String(bytes, charset);
        }
        return null;
    }

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String read(String path) {
        return read(path, StandardCharsets.UTF_8);
    }


    /**
     * 读文件
     *
     * @param path    文件路径
     * @param charset 编码
     * @return 文件内容
     */
    public static List<String> readLines(String path, Charset charset) {
        if (isFile(path)) {
            try {
                return Files.readAllLines(Paths.get(path), charset);
            } catch (IOException e) {
                logger.debug(getLinesString("读文件失败，I/O异常。", path, e.getMessage()));
            } catch (SecurityException e) {
                logger.debug(getLinesString("读文件失败，安全异常。", path, e.getMessage()));
            }
        } else {
            logger.debug(getLinesString("读文件失败，文件不存在或不是文件。", path));
        }
        return null;
    }

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static List<String> readLines(String path) {
        return readLines(path, StandardCharsets.UTF_8);
    }

    /**
     * 写文件
     *
     * @param path   文件路径
     * @param bytes  字节数组
     * @param option 操作类型
     * @return 是否写入成功
     */
    public static boolean writeBytes(String path, byte[] bytes, OpenOption option) {
        if (isFile(path)) {
            try {
                Files.write(Paths.get(path), bytes, option);
                return true;
            } catch (IOException e) {
                logger.debug(getLinesString("写文件失败，I/O异常。", path, e.getMessage()));
            } catch (UnsupportedOperationException e) {
                logger.debug(getLinesString("写文件失败，错误的操作类型。", path, e.getMessage()));
            } catch (SecurityException e) {
                logger.debug(getLinesString("写文件失败，安全异常。", path, e.getMessage()));
            }
        } else {
            logger.debug(getLinesString("写文件失败，不存在或不是文件。"), path);
        }
        return false;
    }

    /**
     * 写文件
     *
     * @param path    文件路径
     * @param content 写入的内容
     * @param charset 编码
     * @param option  操作类型
     * @return 是否写入成功
     */
    public static boolean write(String path, String content, Charset charset, OpenOption option) {
        return writeBytes(path, content.getBytes(charset), option);
    }

    /**
     * 写文件
     *
     * @param path    文件路径
     * @param content 写入的内容
     * @param option  操作类型
     * @return 是否写入成功
     */
    public static boolean write(String path, String content, OpenOption option) {
        return writeBytes(path, content.getBytes(StandardCharsets.UTF_8), option);
    }

    /**
     * 写文件
     *
     * @param path    文件路径
     * @param content 写入的内容
     * @return 是否写入成功
     */
    public static boolean write(String path, String content) {
        return writeBytes(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    /**
     * 写文件
     *
     * @param path    文件路径
     * @param lines   多行文本
     * @param charset 编码
     * @param option  操作类型
     * @return 是否写入成功
     */
    public static boolean writeLines(String path, List<String> lines, Charset charset, OpenOption option) {
        String s = getLinesString(lines.toArray(new String[0]));
        return writeBytes(path, s.getBytes(charset), option);
    }

    /**
     * 写文件
     *
     * @param path   文件路径
     * @param lines  多行文本
     * @param option 操作类型
     * @return 是否写入成功
     */
    public static boolean writeLines(String path, List<String> lines, OpenOption option) {
        String s = getLinesString(lines.toArray(new String[0]));
        return writeBytes(path, s.getBytes(StandardCharsets.UTF_8), option);
    }

    /**
     * 写文件
     *
     * @param path  文件路径
     * @param lines 多行文本
     * @return 是否写入成功
     */
    public static boolean writeLines(String path, List<String> lines) {
        String s = getLinesString(lines.toArray(new String[0]));
        return writeBytes(path, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    // 以多行的形式拼接多个字符串
    private static String getLinesString(String... ss) {
        return String.join(System.getProperty("line.separator"), ss);
    }
}