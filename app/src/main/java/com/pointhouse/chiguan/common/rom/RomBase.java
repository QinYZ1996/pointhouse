package com.pointhouse.chiguan.common.rom;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/6/21.
 */

public class RomBase extends Environment{
    public void saveRom(){
        File file = new File("abc/abc/test");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(("保存了数据").getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] readRom() throws IOException {
        File file = new File("abc/abc/test");
        if(file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            //把字节流转换成字符流
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String text = br.readLine();
            return text.split("");
        }
        return null;
    }



}
