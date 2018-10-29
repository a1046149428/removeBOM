package com.cherbini;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;

/**
 * @author cherbini
 * 2018/10/29 16:17
 */
public class Utf8BomRemover extends DirectoryWalker
{
    private String extension = null;
    private JFrame jFrame;
    private int count;

    public Utf8BomRemover(JFrame controller, String extension)
    {
        this.extension = extension;
        this.jFrame = this.jFrame;
        this.count = 0;
    }

    public void start(File rootDir) throws IOException
    {
        this.walk(rootDir, (Collection) null);
    }

    protected void handleFile(File file, int depth, Collection results) throws IOException
    {
        this.remove(file);
        ++this.count;
    }

    protected void handleEnd(Collection results) throws IOException
    {
        JOptionPane.showMessageDialog(this.jFrame, "你成功修改" + this.count + "个.java编码的BOM文件");
    }

    private void remove(File file) throws IOException
    {
        byte[] bs = FileUtils.readFileToByteArray(file);
        if (bs==null||bs.length==0) return;
        if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65)
        {
            byte[] nbs = new byte[bs.length - 3];
            System.arraycopy(bs, 3, nbs, 0, nbs.length);
            FileUtils.writeByteArrayToFile(file, nbs);
        }

    }
}
