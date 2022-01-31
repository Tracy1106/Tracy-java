import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Calculator UI for user to view and operate
 */
public class CalculatorGUI  extends javax.swing.JFrame  {
    
    private static final Dimension DEFAULT_MIN_SIZE = new Dimension(300, 400);
    
    private static final int DEFAULT_FILL = GridBagConstraints.HORIZONTAL;
    
    private static final int DEFAULT_ANCHOR = GridBagConstraints.CENTER;
     
    private static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);
    
    private static final String[][] BUTTON_LABELS = {
        { "1",  "2", "3", "+", "<<" },
        { "4",  "5", "6", "-", "C"  },
        { "7",  "8", "9", "*", "("  },
        { "+/-","0", ".", "/", ")"  },
        { "=",            "!", "OFF"}    
    };
    
    private static final Set<String> SPECIAL_BUTTON_LABEL_SET = new HashSet<>();
    
    private JPanel MainPanel = new JPanel();
    
    private JTextField InputTextBox = new JTextField();
    
    /**
     * Constructor of CalculatorGUI
     */
    public CalculatorGUI() {
        this.initFrame();
        this.initSpecialButtonLabelSet();
        initComponents();
    }
    
    /**
     * Initialize <code>SPECIAL_BUTTON_LABEL_SET</code>
     * 
     */
    private void initSpecialButtonLabelSet() {
        String[] specialChars = new String[] {"<<", "C", "+/-", "=", "OFF"};
        SPECIAL_BUTTON_LABEL_SET.addAll(Arrays.asList(specialChars));
    }
    
    /**
     * Initilize JFrame
     */
    private void initFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setMinimumSize(DEFAULT_MIN_SIZE);
        this.setResizable(true);
        setLocaltion();
    }
    
    /** 
     * Set current UI to be located in the center both horizontally and vertically
     */
    private void setLocaltion() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(screenSize.width/2 - this.getWidth()/2, screenSize.height/2 - this.getHeight()/2);
        this.setLocation(middle);
    }
    
    /**
     * Main method to initialize all the components of UI
     * 
     */
    private void initComponents() {
        this.initPanel();
        this.initHeader();
        this.initInputTextBox();
        this.initButtons();
        this.getContentPane().add(MainPanel);
    }
    
    /**
     * Initialize header of the calculator with calculator name and version
     * 
     */
    private void initHeader() {
        JLabel header = new JLabel("My PROG5001 – Calculator (1)");
        header.setHorizontalAlignment(JLabel.CENTER);
        this.MainPanel.add(header, new GridBagConstraints(0, 0, 5, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 ));
    }
    
    /**
     * Initialize input textbox with alignment, width and height.
     * 
     */
    private void initInputTextBox() {
        InputTextBox.setHorizontalAlignment(JTextField.LEFT);
        this.MainPanel.add(InputTextBox, new GridBagConstraints(0, 1, 5, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 ));
    }
    
    /**
     * Initialize panel by setting minimum size and layout
     * 
     */
    private void initPanel() {
        MainPanel.setMinimumSize(DEFAULT_MIN_SIZE);
        MainPanel.setLayout(new GridBagLayout());        
    }
    
    /**
     * Initialize buttons with button names and layouts
     * 
     */
    private void initButtons() {
        for(int i = 0; i < BUTTON_LABELS.length; i++) {
            for(int j = 0; j < BUTTON_LABELS[i].length; j++) {
                JButton button = new JButton(BUTTON_LABELS[i][j]);
                button.addMouseListener(new ButtonMouseClickListener(i, j));
                GridBagConstraints constraints = null;                
                if("=".equals(BUTTON_LABELS[i][j])) {
                    constraints = new GridBagConstraints(j, i + 2, 3, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 );
                } else if("!".equals(BUTTON_LABELS[i][j])) {
                    constraints = new GridBagConstraints(j + 2, i + 2, 1, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 );
                } else if("OFF".equals(BUTTON_LABELS[i][j])) {
                    constraints = new GridBagConstraints(j + 2, i + 2, 1, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 );
                } else {
                    constraints = new GridBagConstraints(j, i + 2, 1, 1, 20, 20, DEFAULT_ANCHOR, DEFAULT_FILL, DEFAULT_INSETS, 20, 20 );
                }                
                MainPanel.add(button, constraints);
            }
        }
    }
    
    /**
     * Mouse click listener for all buttons
     * 
     */
    private class ButtonMouseClickListener extends MouseAdapter {
        
        private int col;
        
        private int row;
        
        /**
         * Constructor of class <code>ButtonMouseClickListener</code>
         * @param col column of a button in <code>BUTTON_LABELS</code>
         * @param row row of a button in <code>BUTTON_LABELS</code>
         */
        public ButtonMouseClickListener(int col, int row) {
            this.col = col;
            this.row = row;
        } 
        
        /**
         * Mouse click event is triggered when button is clicked
         * 
         */
        @Override
        public void mouseClicked(MouseEvent evt) {
            if(!SPECIAL_BUTTON_LABEL_SET.contains(BUTTON_LABELS[col][row])) {
                appendText(BUTTON_LABELS[col][row]);
            } else if("C".equals(BUTTON_LABELS[col][row])) {
                InputTextBox.setText("");
            } else if("<<".equals(BUTTON_LABELS[col][row])) {
                String originalText = InputTextBox.getText();
                if(!originalText.isEmpty()) {
                    originalText = originalText.substring(0, originalText.length() - 1);
                }
                InputTextBox.setText(originalText);
            } else if("+/-".equals(BUTTON_LABELS[col][row])) {
                switchNagOrPos();
            } else if("=".equals(BUTTON_LABELS[col][row])) {
                Calculator.ExpRes result = Calculator.calculateExp(InputTextBox.getText());
                if("OK".equals(result.getTag())) {
                    double dResult = result.getRes();
                    String formattedResult = formatResult(dResult);
                    InputTextBox.setText(formattedResult);
                } else {
                    JOptionPane.showMessageDialog(CalculatorGUI.this, result.getMsg());
                }
            } else if("OFF".equals(BUTTON_LABELS[col][row])) {
                System.exit(0);
            }
        }
    }
    
    /**
     * Format double value to int if the double value's equals to the ceil of double value
     * 
     * @param num the double value to be formatted
     */
    private String formatResult(double num) {
        if((int)Math.ceil(num) == (int)num) {
            return Integer.valueOf((int)num).toString();
        }
        
        return String.valueOf(num);
    }
    
    /**
     * Change num to be nagtive or positive
     */
    private void switchNagOrPos(){
        String exp = InputTextBox.getText();
        if("0".equals(exp)) {
            return;
        }
        
        if(isNagtive(exp)) {
            InputTextBox.setText(formatResult(Math.abs(Double.valueOf(exp))));
            return;
        }
        
        int ptr = exp.length()-1;
        
        if(ptr >= 0 && exp.charAt(ptr) >= 48 && exp.charAt(ptr) <= 57) {
            while (ptr >= 0 && exp.charAt(ptr) >= 48 && exp.charAt(ptr) <= 57){
                ptr--;
            }

            if(!exp.substring(ptr+1).trim().equals("")) {
                String tmp = exp.substring(0, ptr+1) + "(-" + exp.substring(ptr+1)+")";
                InputTextBox.setText(tmp);
            }        
        } else if(ptr >= 0 && exp.charAt(ptr) == ')') {
            ptr = ptr - 1;
            String nums = null;
            while (ptr >= 0 && exp.charAt(ptr) >= 48 && exp.charAt(ptr) <= 57){
                ptr--;
                
            }
            if(ptr < exp.length() - 2) {
                nums = exp.substring(ptr+1);
                nums = nums.substring(0, nums.length() -1);
            }
            
            boolean hasLeftBracket = false;
            if(ptr >= 1 && exp.charAt(ptr) == '-' && exp.charAt(ptr-1) == '(') {
                ptr-=2;
                hasLeftBracket = true;
            }
            
            if(nums != null && !nums.isEmpty() && hasLeftBracket) {
                String prefix = exp.substring(0, ptr+1);
                InputTextBox.setText(prefix + nums);
            }
        }
    }
    
    /**
     * Check if a <code>String</code> number is negtive
     * 
     * @param numStr the number to be checked
     * @return true if the number is negative else return false
     */
    private boolean isNagtive(String numStr) {
        try {
            return Double.valueOf(numStr) < 0;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Append characters to <code>InputTextBox</code>
     * 
     * @param txt the character to be appended
     */
    private void appendText(String txt) {
        String originalText = InputTextBox.getText();
        InputTextBox.setText(originalText + txt);
    }
    
    /**
     * Main method to start calculator entry
     * 
     */
    public static void main(String[] args) {
        new CalculatorGUI();
    }
}
