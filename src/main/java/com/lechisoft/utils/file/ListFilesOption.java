package com.lechisoft.utils.file;

public enum ListFilesOption {
    // 递归
    RECURSION,
    // 忽略文件
    IGNORE_FILE,
    // 忽略目录
    IGNORE_DIRECTORY,
    // 忽略隐藏文件
    IGNORE_HIDDEN,
    // 包含扩展名
    INCLUDE_EXTENSION,
    // 排除扩展名
    EXCLUDE_EXTENSION,
    // 最后修改日期由近至远排序
    SORT_LAST_MODIFIED_DESC,
    // 最后修改日期由远至近排序
    SORT_LAST_MODIFIED_ASC,
    // 最后访问日期由近至远排序
    SORT_LAST_ACCESS_DESC,
    // 最后访问日期由远至近排序
    SORT_LAST_ACCESS_ASC,
    // 创建日期由近至远排序
    SORT_CREATION_DESC,
    // 创建日期由远至近排序
    SORT_CREATION_ASC;
}
