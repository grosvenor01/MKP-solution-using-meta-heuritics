import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;

public class graphic extends JFrame {
    int num_knap = 0;
    int num_obj = 0;
    final private Font mainfont = new Font("Serif", Font.BOLD, 18);
    JTextField tfknap, tfobj;
    JScrollPane imagePanel1, imagePanel2, imagePanel3;
    JPanel AlgosPanel; // Declare AlgosPanel as a class member

    public void initialize() {
        /* Labels */
        JLabel knapsack_number = new JLabel("Enter the number of knapsack\n");
        knapsack_number.setFont(mainfont);
        JLabel objects_number = new JLabel("Enter the number of objects\n");
        objects_number.setFont(mainfont);

        /* TextFields */
        tfknap = new JTextField();
        tfknap.setPreferredSize(new Dimension(0, 50));
        tfobj = new JTextField();
        tfobj.setPreferredSize(new Dimension(0, 50));

        /* Submitting Button */
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(50, 150, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    long startTime = System.nanoTime();
                    long endTime = System.nanoTime();

                    instance cas_0 = new instance(num_knap, num_obj);
                    // Solution à la instance courante
                    System.out.println("\n\n======Solution BSO======\n\n");
                    cas_0.generate_sol();
                    startTime = System.nanoTime();
                    solution bso = cas_0.BSO_solve(100);
                    endTime = System.nanoTime();
                    double bsoTime = (double) (endTime - startTime) /1000000;
                    bso.display();

                    System.out.println("\n\n======Solution GA======\n\n");
                    startTime = System.nanoTime();
                    solution sol = new solution();
                    solution ga = cas_0.GA_solve();
                    endTime = System.nanoTime();
                    double GaTime = (double) (endTime - startTime) /1000000;
                    ga.display();
                    updateImagePanels(bso , ga, bsoTime, GaTime);

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number entered. Please enter a valid integer.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /* FORM panel */
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 4, 5, 5));
        formPanel.add(knapsack_number);
        formPanel.add(tfknap);
        formPanel.add(objects_number);
        formPanel.add(tfobj);
        formPanel.add(submitButton);

        /* Display panel */
        imagePanel1 = createImagePanel("BSO", null,0);
        imagePanel2 = createImagePanel("GA", null,0);


        AlgosPanel = new JPanel(); // Initialize AlgosPanel
        AlgosPanel.setLayout(new GridLayout(1, 3));
        AlgosPanel.add(imagePanel1);
        AlgosPanel.add(imagePanel2);
        /* Main panel */
        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        mainpanel.setBackground(new Color(128, 128, 255));
        mainpanel.add(formPanel, BorderLayout.NORTH);
        mainpanel.add(AlgosPanel);
        setTitle("Multiple knapsack problem solver");
        setSize(500, 200);
        setMinimumSize(new Dimension(300, 200));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(mainpanel);
        setVisible(true);
    }

    private boolean validateInput() {
        try {
            num_knap = Integer.parseInt(tfknap.getText());
            num_obj = Integer.parseInt(tfobj.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updateImagePanels(solution bso,solution ga, double bsoTime, double gaTime) {

        // Remove existing image panels
        imagePanel1.removeAll();
        imagePanel2.removeAll();
    
        // Create new image panels
        imagePanel1 = createImagePanel("BSO", bso, bsoTime);
        imagePanel2 = createImagePanel("GA", ga, gaTime);
    
        // Add the new image panels to the AlgosPanel
        AlgosPanel.removeAll();
        AlgosPanel.setLayout(new GridLayout(1, 3));
        AlgosPanel.add(imagePanel1);
        AlgosPanel.add(imagePanel2);
    
        // Repaint the frame to update the changes
        revalidate();
        repaint();
    }
    
    private JScrollPane createImagePanel(String name, solution sol, double executionTime) {
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(num_knap, 2));
    
        // Create TitledBorder with the specified name
        TitledBorder titledBorder = BorderFactory.createTitledBorder(name);
        titledBorder.setTitleFont(mainfont);
        imagePanel.setBorder(titledBorder);
    
        for (int i = 0; i < num_knap; i++) {
            String str = "";
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(
                    "C:\\Users\\abdo7\\OneDrive\\Bureau\\Meta Project\\MKP-problem-solution-using-java-\\src\\image.png")
                    .getImage()
                    .getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            JLabel imageLabel = new JLabel(imageIcon);
            JLabel textLabel = new JLabel("Knapsack number: " + (i + 1));
            JLabel content;
            if (sol != null) {
                for (int j = 0; j < sol.objects.size(); j++) {
                    if (sol.objects.get(j) == i + 1) {
                        str += (j + 1) + "  ";
                    }
                }
                content = new JLabel(str);
            } else {
                content = new JLabel("");
            }
            imagePanel.add(imageLabel);
            imagePanel.add(textLabel);
            imagePanel.add(content);
        }
    
        // Execution time label
        JLabel timeLabel;
        if(sol!=null){
            timeLabel = new JLabel("Execution Time: " + executionTime + " milliseconds" +"  fitness:"+sol.fitness_val);
        }
        else {
            timeLabel = new JLabel("Execution Time: " + executionTime + " milliseconds" +"  fitness:"+0);
        }
    
        // Create a panel to hold the imagePanel and timeLabel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imagePanel, BorderLayout.CENTER);
    
        // Create a separate panel for the time label and make it fixed
        JPanel timeLabelPanel = new JPanel();
        timeLabelPanel.add(timeLabel);
        timeLabelPanel.setPreferredSize(new Dimension(panel.getWidth(), 50)); // Adjust the height as needed
        timeLabelPanel.setBackground(Color.WHITE); // Set a background color for the label panel
        timeLabelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        panel.add(timeLabelPanel, BorderLayout.NORTH);
    
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
        return scrollPane;
    }
}