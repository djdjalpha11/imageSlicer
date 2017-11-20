import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ImgGUI extends JFrame
{
  BufferedImage image;
  File myFile;
  Path mySave;
  JTextArea inputDirectoryText = new JTextArea();
  JTextArea outputDirectoryText = new JTextArea();
  int rows;
  int cols;
  
  public ImgGUI() {
      setTitle("IMGSlicer");
      setSize(480, 150);
      setLayout(new FlowLayout());
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
      setDefaultCloseOperation(3);
      createMenu();
      createInside();
      setVisible(true);
  }
  
public void createMenu() {
    JMenuBar menubar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem item = new JMenuItem();
    ImgGUI.FileMenuHandler fmh = new ImgGUI.FileMenuHandler();
    item = new JMenuItem("Load Image");
    item.addActionListener(fmh);
    fileMenu.add(item);
    fileMenu.addSeparator();
    item = new JMenuItem("Quit");
    item.addActionListener(fmh);
    fileMenu.add(item);
    setJMenuBar(menubar);
    menubar.add(fileMenu);
}
  
public void createInside() {
  JPanel thePanel1 = new JPanel();
  thePanel1.setLayout(new FlowLayout());
  JPanel thePanel2 = new JPanel();
  JButton setRowCol = new JButton("Set Rows and Cols slicing");
  ImgGUI.RowColHandler rch = new ImgGUI.RowColHandler();
  setRowCol.addActionListener(rch);
  
  JLabel input = new JLabel("Input Image Directory: ");
  JLabel output = new JLabel("Output Directory: ");
  thePanel1.add(input);
  thePanel1.add(this.inputDirectoryText);
  thePanel1.add(output);
  thePanel1.add(this.outputDirectoryText);
    
    JButton outputDirectoryFinder = new JButton("Save DIR...");
    ImgGUI.SaveDirectoryHandler sdh = new ImgGUI.SaveDirectoryHandler();
    outputDirectoryFinder.addActionListener(sdh);
    
    thePanel2.add(outputDirectoryFinder);
    
    this.outputDirectoryText.setEditable(false);
    this.inputDirectoryText.setEditable(false);
    
    JButton slicer = new JButton("Slice");
    ImgGUI.SlicerButtonHandler sbh = new ImgGUI.SlicerButtonHandler();
    slicer.addActionListener(sbh);
    add(thePanel1);
    add(thePanel2);
    add(setRowCol);
    add(slicer);
}
  
public class SaveDirectoryHandler implements ActionListener {
    public SaveDirectoryHandler() {}
    
    public void actionPerformed(ActionEvent event) { 
        if (loadDirectory())
        {
            ImgGUI.this.outputDirectoryText.setText(ImgGUI.this.mySave.toString());
        }
    }
    
    private boolean loadDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(1);
        int status = chooser.showSaveDialog(null);
        ImgGUI.this.mySave = chooser.getSelectedFile().toPath();
        System.out.println(ImgGUI.this.mySave.toString());
        if (status == 0)
            return true;
        return false;
    }
}
  
 public class SlicerButtonHandler implements ActionListener
{
    public SlicerButtonHandler() {}
    
    public void actionPerformed(ActionEvent event)
    {
        int width = ImgGUI.this.image.getWidth() / ImgGUI.this.cols;
        int height = ImgGUI.this.image.getHeight() / ImgGUI.this.rows;
        int counter = 0;
        String name = ImgGUI.this.getOutputFileName();
        File output = new File(ImgGUI.this.mySave.toFile(), name);
        for (int i = 0; i < ImgGUI.this.rows; i++)
        {
          for (int j = 0; j < ImgGUI.this.cols; j++)
          {
              output = new File(ImgGUI.this.mySave.toFile(), name + counter);
              BufferedImage sprites = ImgGUI.this.image.getSubimage(j * width, i * height, width, height);
              try 
              {
                ImageIO.write(sprites, ImgGUI.this.getFormat(ImgGUI.this.myFile), output);
                System.out.println("entered Successfully");
              } 
              catch (IOException e) 
              {
                e.printStackTrace();
              }
              counter++;
          } 
        }
    }
}
  
public class RowColHandler implements ActionListener {
    public RowColHandler() {}
    
    public void actionPerformed(ActionEvent event) {
        String[] ten = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        JComboBox numbers = new JComboBox(ten);
        JOptionPane.showMessageDialog(null, numbers, "Enter a row", 3);
        ImgGUI.this.rows = Integer.parseInt((String)numbers.getSelectedItem());
        JOptionPane.showMessageDialog(null, numbers, "Enter a col", 3);
        ImgGUI.this.cols = Integer.parseInt((String)numbers.getSelectedItem());
    }
}
  
public class FileMenuHandler implements ActionListener {
    public FileMenuHandler() {}
    
    public void actionPerformed(ActionEvent event) {
        String menuName = event.getActionCommand();
        if (menuName.equals("Load Image"))
        {
            if (loadImage()) {
              System.out.println("worked");
            }
        } 
        else if (menuName.equals("Quit"))
        System.exit(3);
    }

    private boolean loadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        ImgGUI.this.myFile = chooser.getSelectedFile();
        System.out.println(ImgGUI.this.myFile.getPath());
        System.out.print(ImgGUI.this.getFormat(ImgGUI.this.myFile));
        if (acceptFile(ImgGUI.this.myFile)) 
        {
            try
            {
            ImgGUI.this.image = ImageIO.read(ImgGUI.this.myFile);
            ImgGUI.this.inputDirectoryText.setText(ImgGUI.this.myFile.getPath());
            return true;
            } 
            catch (IOException e) 
            {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean acceptFile(File f) {
        return (f.getName().toLowerCase().endsWith(".jpg")) || 
        (f.getName().toLowerCase().endsWith(".png")) || 
        (f.getName().toLowerCase().endsWith(".bmp")) ||
        (f.getName().toLowerCase().endsWith(".jpeg"));
        }
}
  
public String getFormat(File f) {
        if (f.getName().toLowerCase().endsWith(".jpg"))
            return "jpg";
        if (f.getName().endsWith(".png"))
            return "png";
        if (f.getName().endsWith(".bmp"))
            return "bmp";
        if (f.getName().endsWith(".jpeg")) {
            return "jpeg";
            }
        return "null";
  }
  
public String getOutputFileName() {
      String outputName = JOptionPane.showInputDialog("Name Your File");
      return outputName;
}
  
public static void main(String[] args) {
        ImgGUI driver = new ImgGUI();
  }
}