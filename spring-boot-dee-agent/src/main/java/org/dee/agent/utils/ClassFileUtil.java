package org.dee.agent.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClassFileUtil {

    /**
     * 将class字节码输出到指定文件中
     * @param classFilePath 要输出的class文件路径
     * @param data class的字节码
     */
    private static void saveToFile(String classFilePath, byte[] data) {
        try(FileOutputStream out = new FileOutputStream(classFilePath)) {
            out.write(data);
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
