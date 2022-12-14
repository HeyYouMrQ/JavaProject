package media;

import javax.swing.*;
import java.awt.*;

public class ImageStuff {
    public static void main(String[] args) {
        JFrame frame = new JFrame();  //create a Frame
        //从硬盘里将picture.jpg的文件按照100，100大小，导入到内存中，并转化为image的格式。
        //建议做project时：启动时就一次性将20种棋子图片都导入到内存（存放到20个image引用里），后续在paintComponent时只调用相应的image引用。
        //建议不要把图片放在src，可以resource/picture.jpg
        Image image = Toolkit.getDefaultToolkit().getImage("picture.jpg").getScaledInstance(100, 50, Image.SCALE_FAST);

        JComponent imageComponent = new ImageComponent(image);// create an instance of ImageComponent
        imageComponent.setSize(100,50);
        System.out.printf("imageComponent [%d,%d]\n", imageComponent.getWidth(), imageComponent.getHeight());

        frame.setLocationRelativeTo(null);
        imageComponent.setLocation(50, 50); // set absolute location
        frame.setLayout(null);   //set layout with null to suit absolute location
        frame.setVisible(true);  //Set the window to visible
        frame.add(imageComponent);// add Jcomponent into Jframe
        //set the size of the window
        frame.setSize(imageComponent.getWidth() + 100, 200);
        System.out.printf("window Frame [%d,%d]\n", frame.getWidth(), frame.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //let the window can be close by click "x"
    }

    static class ImageComponent extends JComponent {
        Image paintImage;

        public ImageComponent(Image image) {
            this.setLayout(null);
            this.setFocusable(true);//Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
            this.paintImage = image;
            repaint();//调用当前组件的paintComponent方法
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(paintImage, 0, 0, paintImage.getWidth(this), paintImage.getHeight(this), this);
        }
    }
}