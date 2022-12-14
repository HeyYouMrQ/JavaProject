package media;
import javax.swing.*;
public class FileChooser
{
    public static String chooseFile(int mode) throws ClassNotFoundException//0载入1存档
            , InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        String path = null;
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//设置界面风格
        JFileChooser jdir = new JFileChooser();
        /*FileNameExtensionFilter filter = new FileNameExtensionFilter(//过滤文件类型
               "Excel文件(*.xls;*.xlsx;)","xls", "xlsx" );
        jdir.setFileFilter(filter);*/
        if(mode==0)
        {
            jdir.setDialogTitle("请选择载入文件路径");//设置对话框标题
            jdir.setFileSelectionMode(JFileChooser.FILES_ONLY);//设置选择路径模式
        }
        else if(mode==1)
        {
            jdir.setDialogTitle("请选择存档路径");//设置对话框标题
            jdir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置选择路径模式
        }
        if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {//用户点击了确定
            path = jdir.getSelectedFile().getAbsolutePath();//取得路径选择
        }
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");//设置界面风格
        return path;
    }
}