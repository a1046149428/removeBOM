package com.cherbini;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * @author cherbini
 * 2018/10/29 16:16
 */
public class Controller extends JFrame
{
    int count = 0;

    public Controller()
    {
        super("批量移除BOM编码");
        this.setSize(500, 400);
        this.setLocation(400, 200);
        this.setBackground(Color.WHITE);
        this.setVisible(true);
        this.setResizable(false);
        this.initView();
    }

    private void initView()
    {
        final JTextField jTF = new JTextField();
        jTF.setBounds(60, 50, 200, 30);
        this.add(jTF);
        JButton jButton = new JButton();
        jButton.setText("浏览");
        jButton.addActionListener(this.getFolderPath(jTF));
        jButton.setBounds(270, 50, 80, 30);
        this.add(jButton);
        JButton removeBom = new JButton("移除bom");
        removeBom.setBounds(270, 100, 100, 30);
        removeBom.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (Global.isEmpty(jTF.getText()))
                {
                    JOptionPane.showMessageDialog(Controller.this.getContentPane(), "移除BOM的指定路劲不能为空");
                }
                else
                {
                    try
                    {
                        (new Utf8BomRemover(Controller.this, "java")).start(new File(jTF.getText()));
                    } catch (IOException var3)
                    {
                        var3.printStackTrace();
                    }

                }
            }
        });
        this.add(removeBom);
        JPanel jPanel = new JPanel();
        jPanel.setBounds(0, 0, 500, 200);
        jPanel.setSize(500, 200);
        this.add(jPanel);
        this.validate();
        this.setVisible(true);
    }

    private ActionListener getFilePath(final JTextField jTF)
    {
        return new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(Controller.this) == 0)
                {
                    jTF.setText(jfc.getSelectedFile().getAbsolutePath());
                }

            }
        };
    }

    private ActionListener getFolderPath(final JTextField jTF)
    {
        return new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setDialogTitle("请选择文件夹");
                chooser.setFileSelectionMode(1);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(Controller.this) == 0)
                {
                    jTF.setText(chooser.getSelectedFile().getAbsolutePath());
                    System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
                }
                else
                {
                    System.out.println("No Selection ");
                }

            }
        };
    }

    private boolean deleteFile(String path, String deleteName)
    {
        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                JOptionPane.showMessageDialog(this.getContentPane(), "文件路径不存在");
                return false;
            }

            if (!file.isDirectory())
            {
                file.delete();
                ++this.count;
            }
            else if (file.isDirectory())
            {
                String[] fileList = file.list();

                for (int i = 0; i < fileList.length; ++i)
                {
                    File delFile = new File(path + File.separator + fileList[i]);
                    if (!delFile.isDirectory() && (deleteName.isEmpty() || deleteName.equals(delFile.getName())))
                    {
                        delFile.delete();
                        ++this.count;
                        System.out.println(delFile.getAbsolutePath() + "删除文件成功");
                    }
                    else if (delFile.isDirectory())
                    {
                        this.deleteFile(path + File.separator + fileList[i], deleteName);
                    }
                }

                System.out.println(file.getAbsolutePath() + "删除成功");
                file.delete();
            }
        } catch (Exception var7)
        {
            System.out.println("deletefile() Exception:" + var7.getMessage());
        }

        return true;
    }
}