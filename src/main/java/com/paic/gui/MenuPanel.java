package com.paic.gui;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.paic.util.HbaseSqlGenerator;
import com.paic.util.PhoenixSqlGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class MenuPanel
{

    private Frame f;
    private MenuBar bar;
    private TextArea ta;
    private Menu fileMenu, importMenu,exportMenu;
    private MenuItem importHbaseItem, importPhoenixItem,saveItem,closeItem;
    private MenuItem exportHbaseItem, exportPhoenixItem;
    private JToolBar jToolBar;

    private FileDialog openDia,saveDia;


    private JLabel jToolBarlabel ;

    private File file;
    MenuPanel()
    {
        init();
    }
    public void init()
    {
        f = new Frame("my window");
        f.setBounds(300,100,600,300);

        bar = new MenuBar();

        ta = new TextArea();

        fileMenu = new Menu("文件");


        importMenu = new Menu("导入");
        exportMenu = new Menu("下载模板");
        importHbaseItem = new MenuItem("hbase");
        importPhoenixItem = new MenuItem("phoenix");
        exportHbaseItem = new MenuItem("hbase");
        exportPhoenixItem = new MenuItem("phoenix");
        importMenu.add(importHbaseItem);
        importMenu.add(importPhoenixItem);
        exportMenu.add(exportHbaseItem);
        exportMenu.add(exportPhoenixItem);

        saveItem = new MenuItem("保存");
        closeItem = new MenuItem("退出");

        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);
        bar.add(fileMenu);

        f.setMenuBar(bar);


        openDia = new FileDialog(f,"打开文件",FileDialog.LOAD);
        saveDia = new FileDialog(f,"存储文件",FileDialog.SAVE);


        f.add(ta);

        jToolBar = new JToolBar();
        jToolBarlabel=new JLabel("running");
        jToolBar.add(jToolBarlabel);//把标签加到工具栏上
        f.add(jToolBar,BorderLayout.SOUTH);

        myEvent();

        f.setVisible(true);

    }
    private void myEvent()
    {

        saveItem.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                if(file==null)
                {
                    saveDia.setVisible(true);

                    String dirPath = saveDia.getDirectory();
                    String fileName = saveDia.getFile();
                    if(dirPath==null || fileName==null)
                        return ;
                    file = new File(dirPath,fileName);
                }

                try
                {
                    BufferedWriter bufw  = new BufferedWriter(new FileWriter(file));

                    String text = ta.getText();

                    bufw.write(text);
                    //bufw.flush();
                    bufw.close();
                }
                catch (IOException ex)
                {
                    throw new RuntimeException();
                }

            }
        });


        importHbaseItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openDia.setVisible(true);
                String dirPath = openDia.getDirectory();
                String fileName = openDia.getFile();
//				System.out.println(dirPath+"..."+fileName);
                if(dirPath==null || fileName==null)
                    return ;
                ta.setText("");

                // 解析文件，生成sql语句
                String result = new HbaseSqlGenerator().generate(dirPath, fileName, jToolBarlabel);
                ta.setText(result);
            }
        });

        exportHbaseItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
                String savePath = path.substring(0,path.lastIndexOf(System.getProperty("file.separator")) + 1 )+ "hbase.xlsx";
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("hbase.xlsx");
                ExcelReader reader = ExcelUtil.getReader(in);
                List<List<Object>> read = reader.read();
                ExcelWriter writer = ExcelUtil.getWriter(savePath);
                writer.write(read);
                writer.close();
                jToolBarlabel.setText("保存模板至:" + savePath);

            }
        });

        exportPhoenixItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
                String savePath = path.substring(0,path.lastIndexOf(System.getProperty("file.separator")) + 1 )+ "phoenix.xlsx";
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("hbase.xlsx");
                ExcelReader reader = ExcelUtil.getReader(in);
                List<List<Object>> read = reader.read();
                ExcelWriter writer = ExcelUtil.getWriter(savePath);
                writer.write(read);
                writer.close();
                jToolBarlabel.setText("保存模板至:" + savePath);

            }
        });



        importPhoenixItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openDia.setVisible(true);
                String dirPath = openDia.getDirectory();
                String fileName = openDia.getFile();
//				System.out.println(dirPath+"..."+fileName);
                if(dirPath==null || fileName==null)
                    return ;

                // 解析文件，生成sql语句
                String result = new PhoenixSqlGenerator().generate(dirPath, fileName, jToolBarlabel);
                ta.setText(result);
            }
        });


        closeItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        f.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    private void generatePhoenixSql(String dirPath, String fileName) {
    }


    public static void main(String[] args)
    {
        new MenuPanel();
    }
}
