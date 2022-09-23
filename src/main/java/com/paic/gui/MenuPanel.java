package com.paic.gui;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.paic.core.*;
import com.paic.util.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class MenuPanel
{

    private Frame f;
    private MenuBar bar;
    private TextArea ta;
    private JTextField sourceyu;
    private JTextField targetyu;
    private JTextField pks;
    private Menu fileMenu, importMenu,exportMenu;

    private Map<String,MenuItem> importItems = new LinkedHashMap<>();
    private Map<String,MenuItem> exportItems = new LinkedHashMap<>();
    private MenuItem saveItem;
    private MenuItem closeItem;
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
        f.setLayout(new FlowLayout());

        bar = new MenuBar();
        sourceyu=new JTextField();
        targetyu=new JTextField();
        pks=new JTextField();
        ta = new TextArea();
        fileMenu = new Menu("文件");
        importMenu = new Menu("导入");
        exportMenu = new Menu("下载模板");
        saveItem = new MenuItem("保存");
        closeItem = new MenuItem("退出");

        // 初始化并加载下载模板ITEM
        initImportItems();
        // 初始化并加载导入模板ITEM
        initExportItems();


        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);
        bar.add(fileMenu);
        f.setMenuBar(bar);
        openDia = new FileDialog(f,"打开文件",FileDialog.LOAD);
        saveDia = new FileDialog(f,"存储文件",FileDialog.SAVE);




//        sourceyu.setBackground(Color.yellow);
        sourceyu.setColumns(10);
        sourceyu.setText("来源域");
        f.add(BorderLayout.SOUTH,sourceyu);
//        targetyu.setBackground(Color.red);
        targetyu.setColumns(10);
        targetyu.setText("目标域");
        f.add(BorderLayout.NORTH,targetyu);
//        pks.setBackground(Color.red);
        pks.setColumns(20);
        pks.setText("主键，多个以，分割，带上括号");
        f.add(BorderLayout.NORTH,pks);

//        jToolBar = new JToolBar();
//        jToolBarlabel=new JLabel("running");
//        jToolBar.add(jToolBarlabel);//把标签加到工具栏上
//        f.add(jToolBar,BorderLayout.SOUTH);

        f.add(ta);
        try {
            myEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        f.setVisible(true);

    }

    private void initExportItems() {
        exportItems.put(Item.Export.HBASE.getKey(), new MenuItem(Item.Export.HBASE.getName()));
        exportItems.put(Item.Export.PHOENIX.getKey(), new MenuItem(Item.Export.PHOENIX.getName()));
        // 添加下载kafka的模板
        exportItems.put(Item.Export.KAFKA.getKey(), new MenuItem(Item.Export.KAFKA.getName()));

        // 添加Hive
        exportItems.put(Item.Export.HIVE_DDL_AND_DML.getKey(), new MenuItem(Item.Export.HIVE_DDL_AND_DML.getName()));
        initItem(exportMenu, exportItems.values().iterator());


    }

    private void initImportItems() {

        importItems.put(Item.Import.CK2MYSQL.getKey(), new MenuItem(Item.Import.CK2MYSQL.getName()));

        initItem(importMenu, importItems.values().iterator());
    }

    private void initItem(Menu menu, Iterator<MenuItem> iterator){
        while(iterator.hasNext()){
            menu.add(iterator.next());
        }
    }

    private void myEvent() throws Exception {

        // ======== 保存关闭 相关 start =====
        saveItem.addActionListener(new ActionListener()
        {

            @Override
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

        closeItem.addActionListener(new ActionListener()
        {
            @Override
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

        // ======== 保存关闭 相关 start =====

        // ======== hive 相关 start =====
        parseSql2TxWrapper(Item.Import.CK2MYSQL.getKey(), CK2MysqlGenerator.class, "generateCK2Mysql");
        // ======== hive 相关 end =====


    }

    // 导出excel到本地包装
    public void exportExl2FSWrapper(String key, String sourceFileFullName, String sinkFileFullName){
        exportItems.get(key).addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
//                String savePath = path.substring(0,path.lastIndexOf(System.getProperty("file.separator")) + 1 )+ "phoenix.xlsx";
                String savePath = path.substring(0,path.lastIndexOf(System.getProperty("file.separator")) + 1 )+ sinkFileFullName;
//                InputStream in = this.getClass().getClassLoader().getResourceAsStream("hbase.xlsx");
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(sourceFileFullName);
                ExcelReader reader = ExcelUtil.getReader(in);
                List<List<Object>> read = reader.read();
                ExcelWriter writer = ExcelUtil.getWriter(savePath);
                writer.write(read);
                writer.close();
                jToolBarlabel.setText("保存模板至:" + savePath);

            }
        });
    }

    public void parseSql2TxWrapper(String key, Class clazz, String methodName) throws Exception{
        importItems.get(key).addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    openDia.setVisible(true);
                    String dirPath = openDia.getDirectory();
                    String fileName = openDia.getFile();
                    // System.out.println(dirPath+"..."+fileName);
                    if(dirPath==null || fileName==null)
                        return ;

                    // 解析文件，生成sql语句
                    Object o = clazz.newInstance();
                    Method method = clazz.getDeclaredMethod(methodName,String[].class, JLabel.class);
                    String result = (String)method.invoke(o,new Object[]{new String[]{dirPath, fileName, sourceyu.getText(),targetyu.getText(), pks.getText()}, jToolBarlabel});
                    ta.setText(result);
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (NoSuchMethodException noSuchMethodException) {
                    noSuchMethodException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args)
    {
        new MenuPanel();
    }
}
