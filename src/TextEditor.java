import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener{
    JTextArea textArea;
    JScrollPane scrollPane;
    JSpinner fontSizeSpinner;
    JLabel fontLabel;
    JButton fontColorButton;
    JComboBox fontBox;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Zihan's Text Editor");
        this.setSize(800, 600);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        //Text writing
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN, 20));

        //Scroll pane
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(750,550));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Font Size
        fontLabel = new JLabel("Font Size");

        //Font size
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
            }
        });

        //Font color
        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        //Font face
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        //Menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(null, "Choose color", Color.black);
            textArea.setForeground(color);
        }
        if (e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }
        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "doc", "docx", "pdf");
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()){
                        while(fileIn.hasNextLine()){
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileIn.close();
                }
            }
        }
        if (e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showSaveDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                }
                catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally{
                    fileOut.close();
                }
            }
        }
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }
}
