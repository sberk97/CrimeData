package CrimeData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Main class of GUI, it is solution for Task 4
 * @author Sebastian Berk UB:
 */
class GUIHandler {
    
    private final JFrame frame = new JFrame();
    private final ButtonListener buttonListener = new ButtonListener();

    //Panel main components
    private final JButton crimeNumberChart = new JButton("Crime number chart");
    private final JButton findCrime = new JButton("Find crime");
    private final JButton dataQualityCheck = new JButton("Data quality check");
    private final JButton exit = new JButton("Exit");
    private final PanelMain panelMain = new PanelMain();

    //Panel find components
    private final JLabel labelFindInformation = new JLabel("Information: You can use regular expression to find records, for example type -1.4% in Longitude");
    private final JTextField textFieldLongitude = new JTextField(10);
    private final JTextField textFieldLatitude = new JTextField(10);
    private final JTextField textFieldLSOAName = new JTextField(10);
    private final JTextField textFieldLSOACode = new JTextField(10);
    private final JComboBox<String> comboBoxCrimeType = new JComboBox<>(GUIImplementation.createArrayForComboBox(0));
    
    private final JTextField textFieldCrimeID = new JTextField(10);
    private final JTextField textFieldYear = new JTextField(10);
    private final String[] monthList = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final JComboBox<String> comboBoxMonth = new JComboBox<>(monthList);
    private final JComboBox<String> comboBoxReportedBy = new JComboBox<>(GUIImplementation.createArrayForComboBox(1));
    private final JComboBox<String> comboBoxFallsWithin = new JComboBox<>(GUIImplementation.createArrayForComboBox(2));
    private final JTextField textFieldLocation = new JTextField(10);
    private final JComboBox<String> comboBoxLastOutcomeCategory = new JComboBox<>(GUIImplementation.createArrayForComboBox(3));
    private final JComboBox<String> comboBoxSortBy = new JComboBox<>(GUIImplementation.createArrayForComboBox(4));
    
    private final JButton search = new JButton("Search");
    private final JButton clear = new JButton("Clear");
    private final JButton backFind = new JButton("Main menu");
    
    private final ArrayList<String> modelEmptyList = takeOutDataFromFields();
    private PanelFindCrime panelFindCrime = null;
    
    //Panel chart components
    private final JButton backChart = new JButton("Main menu");
    private final JComboBox<String> comboBoxCrimeTypeChart = new JComboBox<>(GUIImplementation.createArrayForComboBox(0));
    private final JFreeChart crimeNumberBarChart = ChartFactory.createBarChart(
            "Number of crimes by type",
            "Fallen within",
            "Number of crimes",
            GUIImplementation.createDataset(comboBoxCrimeTypeChart.getSelectedItem().toString()),
            PlotOrientation.VERTICAL,
            true, true, false
            );
    private PanelChart panelChart = null;
    
    //Panel data quality check components
    private final JLabel labelDataQualityCheckInformation = new JLabel("Be aware that operations can take a while");
    private final JButton exportEmptyCrimeID = new JButton("Export records with empty Crime ID");
    private final JButton exportDuplicates = new JButton("Export records with duplicate Crime ID");
    private final JButton removeEmptyCrimeID = new JButton("Remove records with empty Crime ID");
    private final JButton backDataQualityCheck = new JButton("Main menu");
    private PanelDataQualityCheck panelDataQualityCheck = null;
    
    //Panel table components
    private final JButton searchAgain = new JButton("Search again");
    private final JButton backTable = new JButton("Main menu");
    private DefaultTableModel tableModel = null;
    private PanelTable panelTable = null;
    
    /**
     * It chose default close operation and makes frame visible then it loads main menu
     */
    void setup() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        swapPanel(0);
    }
    
    /**
     * Method used to change actual content of JFrame by removing old JPanel from it and adding new one
     * It also sets proper title, and preferred size. Then calls recreatePanel method 
     * to paint everything properly in JFrame
     * @param operation 0: Main menu 1: Search engine 2: Panel with bar chart 3: Panel with table
     * 4: Panel with data quality check operations
     */
    private void swapPanel(int operation) {
        SwingUtilities.invokeLater(() -> {
            switch (operation) {
                case 0:
                    frame.add(panelMain);
                    frame.setTitle("British police crime database - Main menu");
                    frame.setPreferredSize(null);
                    recreatePanel();
                    break;
                case 1:
                    if (panelFindCrime == null) {
                        panelFindCrime = new PanelFindCrime();
                    }
                    frame.add(panelFindCrime);
                    frame.setTitle("British police crime database - Find crime");
                    labelFindInformation.setText("Information: You can use regular expression to find records, for example type -1.4% in Longitude");
                    frame.setPreferredSize(null);
                    recreatePanel();
                    break;
                case 2:
                    if (panelChart == null) {
                        panelChart = new PanelChart();
                    }
                    frame.add(panelChart);
                    frame.setTitle("British police crime database - Crime number chart");
                    frame.setPreferredSize(null);
                    recreatePanel();
                    break;
                case 3:
                    panelTable = new PanelTable();
                    frame.add(panelTable);
                    frame.setTitle("British police crime database - Records table");
                    frame.setPreferredSize(null);
                    recreatePanel();
                    break;
                case 4:
                    if (panelDataQualityCheck == null) {
                        panelDataQualityCheck = new PanelDataQualityCheck();
                    }
                    frame.add(panelDataQualityCheck);
                    frame.setTitle("British police crime database - Data quality check");
                    frame.setPreferredSize(new Dimension(600, 300));
                    labelDataQualityCheckInformation.setText("Be aware that operations can take a while");
                    recreatePanel();
                    break;
                default:
                    break;
            }
        });
    }
    
    /**
     * This methods pack frame to set up size of window, revalidate and repaint to show new
     * components in frame properly
     */
    private void recreatePanel() {
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }
    
    /**
     * This class implements ActionListener and is called when any button is pressed or JComboBox value changed
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == findCrime || e.getSource() == searchAgain || e.getSource() == backFind || e.getSource() == backTable || e.getSource() == backChart || e.getSource() == backDataQualityCheck || e.getSource() == crimeNumberChart || e.getSource() == dataQualityCheck) {
                frame.getContentPane().removeAll();
            }
            if (e.getSource() == exit) {
                System.exit(0);
            } else if (e.getSource() == backFind || e.getSource() == backTable || e.getSource() == backChart || e.getSource() == backDataQualityCheck) {
                swapPanel(0);
                if (e.getSource() == backFind || e.getSource() == backTable) {
                    clearInputsFromFindCrime();
                } 
            } else if (e.getSource() == findCrime || e.getSource() == searchAgain) {
                swapPanel(1);
            } else if (e.getSource() == crimeNumberChart) {
                swapPanel(2);
            } else if (e.getSource() == dataQualityCheck) {
                swapPanel(4);
            } else if (e.getSource() == search) {
                ArrayList<String> searchParameters = takeOutDataFromFields();
                try {
                if (checkIfInputExist(searchParameters)) {
                    tableModel = GUIImplementation.startCreatingTable(searchParameters);
                    frame.remove(panelFindCrime);
                    swapPanel(3);
                }
                } catch (EmptySearchException | NoMatchingInputException ex) {
                        labelFindInformation.setText(ex.getMessage());
                }
            } else if (e.getSource() == clear) {
                clearInputsFromFindCrime();
            } else if (e.getSource() == comboBoxCrimeTypeChart) {
                CategoryPlot plot = (CategoryPlot) crimeNumberBarChart.getPlot();
                plot.setDataset(GUIImplementation.createDataset(comboBoxCrimeTypeChart.getSelectedItem().toString()));
            } else if (e.getSource() == exportEmptyCrimeID) {
                boolean ifDone = DataQualityCheck.chooseDataQualityCheckOperation(0);
                if (ifDone) {
                    labelDataQualityCheckInformation.setText("Done! " + DataQualityCheck.getNumberOfRecords(0) + " Records with empty Crime ID has been exported to nocrimeid.txt");
                } else {
                    labelDataQualityCheckInformation.setText("Something went wrong");
                }
            } else if (e.getSource() == exportDuplicates) {
                boolean ifDone = DataQualityCheck.chooseDataQualityCheckOperation(1);
                if (ifDone) {
                    labelDataQualityCheckInformation.setText("Done! " + DataQualityCheck.getNumberOfRecords(1) +  " Records with duplicate Crime ID has been exported to duplicatecrimeid.txt");
                } else {
                    labelDataQualityCheckInformation.setText("Something went wrong");
                }
            } else if (e.getSource() == removeEmptyCrimeID) {
                String emptyCrimeIDNumber = DataQualityCheck.getNumberOfRecords(0);
                boolean ifDone = DataQualityCheck.chooseDataQualityCheckOperation(2);
                if (ifDone) {
                    labelDataQualityCheckInformation.setText("Done! " + emptyCrimeIDNumber + " Records with empty Crime ID has been removed from database");
                } else {
                    if ("0".equals(DataQualityCheck.getNumberOfRecords(0))) {
                        labelDataQualityCheckInformation.setText("Database didn't have any records with empty Crime ID");
                    } else {
                    labelDataQualityCheckInformation.setText("Something went wrong");
                    }
                }
            }
        }
    }
    
    /**
     * JPanel of main menu, there are all sub panels added
     */
    private class PanelMain extends JPanel { {       
        this.setLayout(new BorderLayout());
        
        PanelMainNorth panelMainNorth = new PanelMainNorth();
        this.add(panelMainNorth, BorderLayout.NORTH);
        
        PanelMainSouth panelMainSouth = new PanelMainSouth();
        this.add(panelMainSouth, BorderLayout.SOUTH);
    }}
    
    /**
     * North panel of Main menu, it contains logo which appears on the top in main menu
     */
    private class PanelMainNorth extends JPanel { {
        this.add(new JLabel(new ImageIcon("assets/logo.png")));
    }}
    
    /**
     * South panel of Main menu, there are added JButtons and every of them have added ActionListener
     */
    private class PanelMainSouth extends JPanel { {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        
        this.add(crimeNumberChart, gridBagConstraints);
        this.add(findCrime, gridBagConstraints);
        this.add(dataQualityCheck, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        gridBagConstraints.gridx = 1;
        this.add(exit, gridBagConstraints);
        
        exit.addActionListener(buttonListener);
        crimeNumberChart.addActionListener(buttonListener);
        findCrime.addActionListener(buttonListener);
        dataQualityCheck.addActionListener(buttonListener);
    }}
    
    /**
     * JPanel of search engine, there are all sub panels added 
     */
    private class PanelFindCrime extends JPanel { {
        this.setLayout(new BorderLayout());
        
        PanelFindCrimeNorth panelFindCrimeNorth = new PanelFindCrimeNorth();
        this.add(panelFindCrimeNorth, BorderLayout.NORTH);
        
        PanelFindCrimeCenter panelFindCrimeCenter = new PanelFindCrimeCenter();
        this.add(panelFindCrimeCenter, BorderLayout.CENTER);
        
        PanelFindCrimeSouth panelFindCrimeSouth = new PanelFindCrimeSouth();
        this.add(panelFindCrimeSouth, BorderLayout.SOUTH);
        
    }}
    
    /**
     * North panel of search engine, there is added image with text Find crime
     */
    private class PanelFindCrimeNorth extends JPanel { {
        this.add(new JLabel(new ImageIcon("assets/findcrime.png")));
    }}
    
    /**
     * Center panel of search engine, there are added all JLabels, JTextFields and JComboBoxes
     * It also contains image Advanced options
     */
    private class PanelFindCrimeCenter extends JPanel { {       
        Border border = this.getBorder();
        Border margin = new EmptyBorder(10, 10, 10, 10);
        this.setBorder(new CompoundBorder(border, margin));

        GridBagLayout panelGridBagLayout = new GridBagLayout();
        panelGridBagLayout.columnWidths = new int[] {86, 86, 0 };
        panelGridBagLayout.rowHeights = new int[] {20, 20, 20, 20, 20, 0 };
        panelGridBagLayout.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE };
        panelGridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        this.setLayout(panelGridBagLayout);
        
        GridBagConstraints gridBagConstraintForLabel = new GridBagConstraints();
        gridBagConstraintForLabel.fill = GridBagConstraints.BOTH;
        gridBagConstraintForLabel.insets = new Insets(0, 0, 5, 5);
        gridBagConstraintForLabel.gridx = 0;
        
        GridBagConstraints gridBagConstraintForTextField = new GridBagConstraints();
        gridBagConstraintForTextField.fill = GridBagConstraints.BOTH;
        gridBagConstraintForTextField.insets = new Insets(0, 0, 5, 0);
        gridBagConstraintForTextField.gridx = 1;
        int index = 0;

        gridBagConstraintForLabel.gridy = index;
        JLabel labelLongitude = new JLabel("Longitude: ");
        this.add(labelLongitude, gridBagConstraintForLabel);

        gridBagConstraintForTextField.gridy = index++;
        labelLongitude.setLabelFor(textFieldLongitude);
        this.add(textFieldLongitude, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelLatitude = new JLabel("Latitude: ");
        this.add(labelLatitude, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelLongitude.setLabelFor(textFieldLatitude);
        this.add(textFieldLatitude, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelLSOAName = new JLabel("LSOA name: ");
        this.add(labelLSOAName, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelLSOAName.setLabelFor(textFieldLSOAName);
        this.add(textFieldLSOAName, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelLSOACode = new JLabel("LSOA code: ");
        this.add(labelLSOACode, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelLSOACode.setLabelFor(textFieldLSOACode);
        this.add(textFieldLSOACode, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelCrimeType = new JLabel("Crime type: ");
        this.add(labelCrimeType, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelCrimeType.setLabelFor(comboBoxCrimeType);
        this.add(comboBoxCrimeType, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index++;
        gridBagConstraintForLabel.gridwidth = 2;
        this.add(new JLabel(new ImageIcon("assets/advanced.png")), gridBagConstraintForLabel);

        gridBagConstraintForLabel.gridwidth = 1;
        gridBagConstraintForLabel.gridy = index;
        JLabel labelCrimeID = new JLabel("Crime ID: ");
        this.add(labelCrimeID, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelCrimeID.setLabelFor(textFieldCrimeID);
        this.add(textFieldCrimeID, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelYear = new JLabel("Year: ");
        this.add(labelYear, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelYear.setLabelFor(textFieldYear);
        this.add(textFieldYear, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelMonth = new JLabel("Month: ");
        this.add(labelMonth, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelMonth.setLabelFor(comboBoxMonth);
        this.add(comboBoxMonth, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelReportedBy = new JLabel("Reported by: ");
        this.add(labelReportedBy, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelReportedBy.setLabelFor(comboBoxReportedBy);
        this.add(comboBoxReportedBy, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelFallsWithin = new JLabel("Falls within: ");
        this.add(labelFallsWithin, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelFallsWithin.setLabelFor(comboBoxFallsWithin);
        this.add(comboBoxFallsWithin, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelLocation = new JLabel("Location: ");
        this.add(labelLocation, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelLocation.setLabelFor(textFieldLocation);
        this.add(textFieldLocation, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelLastOutcomeCategory = new JLabel("Last outcome: ");
        this.add(labelLastOutcomeCategory, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelLastOutcomeCategory.setLabelFor(comboBoxLastOutcomeCategory);
        this.add(comboBoxLastOutcomeCategory, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index;
        JLabel labelSortBy = new JLabel("Sort by: ");
        this.add(labelSortBy, gridBagConstraintForLabel);
        
        gridBagConstraintForTextField.gridy = index++;
        labelSortBy.setLabelFor(comboBoxSortBy);
        this.add(comboBoxSortBy, gridBagConstraintForTextField);
        
        gridBagConstraintForLabel.gridy = index + 2;
        gridBagConstraintForLabel.gridwidth = 2;
        labelFindInformation.setHorizontalAlignment(JLabel.CENTER);
        this.add(labelFindInformation, gridBagConstraintForLabel);    
    }}
    
    /**
     * South panel of search engine, there are all JButtons and buttons are having added ActionListeners
     */
    private class PanelFindCrimeSouth extends JPanel { {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        
        this.add(search, gridBagConstraints);
        this.add(clear, gridBagConstraints);
        this.add(backFind, gridBagConstraints);
        
        search.addActionListener(buttonListener);
        clear.addActionListener(buttonListener);
        backFind.addActionListener(buttonListener);
    }}
    
    /**
     * This method checks if values from fields are not empty. It is done by comparing SearchParameters array with model empty
     * array which is filled at the beginning of initialization of the class.
     * Both Arrays are copied to prevent from removing all values from empty list, and removing value from searchParameter list
     * It is done because Sort by parameter is not considered as search value
     * @param searchParameters Values of parameters from fields
     * @return true if there is something entered to any of the fields without Sort by
     * @throws EmptySearchException is thrown when empty list and parameter list are equal
     */
    private boolean checkIfInputExist(ArrayList<String> searchParameters) throws EmptySearchException {
        ArrayList<String> newModelEmptyList = new ArrayList<>(modelEmptyList);
        ArrayList<String> newsearchParameters = new ArrayList<>(searchParameters);
        newModelEmptyList.remove(newModelEmptyList.size() - 1);
        newsearchParameters.remove(newsearchParameters.size() - 1);
        if (newsearchParameters.equals(newModelEmptyList)) {
            throw new EmptySearchException();
        } else {
            return true;  
        }
    }
    
    /**
     * This method creates an ArrayList of Strings which are values from fields
     * @return ArrayList<String> with values from fields
     */
    private ArrayList<String> takeOutDataFromFields() {
        ArrayList<String> searchParameters = new ArrayList<>();
        searchParameters.add(textFieldLongitude.getText());
        searchParameters.add(textFieldLatitude.getText());
        searchParameters.add(textFieldLSOAName.getText());
        searchParameters.add(textFieldLSOACode.getText());
        searchParameters.add(comboBoxCrimeType.getSelectedItem().toString());
        searchParameters.add(textFieldCrimeID.getText());
        searchParameters.add(textFieldYear.getText());
        searchParameters.add(Integer.toString(comboBoxMonth.getSelectedIndex()));
        searchParameters.add(comboBoxReportedBy.getSelectedItem().toString());
        searchParameters.add(comboBoxFallsWithin.getSelectedItem().toString());
        searchParameters.add(textFieldLocation.getText());
        searchParameters.add(comboBoxLastOutcomeCategory.getSelectedItem().toString());
        searchParameters.add(comboBoxSortBy.getSelectedItem().toString());
        return searchParameters;
    }
    
    /**
     * This method removes any values from JTextFields, Set default values in JComboBoxes
     * and sets default message to information label
     */
    private void clearInputsFromFindCrime() {
        textFieldLongitude.setText("");
        textFieldLatitude.setText("");
        textFieldLSOAName.setText("");
        textFieldLSOACode.setText("");
        comboBoxCrimeType.setSelectedIndex(0);
        textFieldCrimeID.setText("");
        textFieldYear.setText("");
        comboBoxMonth.setSelectedIndex(0);
        comboBoxReportedBy.setSelectedIndex(0);
        comboBoxFallsWithin.setSelectedIndex(0);
        textFieldLocation.setText("");
        comboBoxLastOutcomeCategory.setSelectedIndex(0);
        comboBoxSortBy.setSelectedIndex(0);

        labelFindInformation.setText("Information: You can use regular expression to find records, for example type -1.4% in Longitude");
    }
    
    /**
     * JPanel which contains JTable with data from fields and sub panel
     */
    private class PanelTable extends JPanel { { 
        this.setLayout(new BorderLayout());
        
        Border border = this.getBorder();
        Border margin = new EmptyBorder(10, 10, 10, 10);
        this.setBorder(new CompoundBorder(border, margin));
        
        JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            /*
              This method changes the colour of every second row to gray
              It is taken from https://stackoverflow.com/a/11558241/7259312
             */
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
             }
        });
        setColumnWidths(table, 200, 75, 160, 160, 75, 75, 230, 95, 110, 170, 280, 15);
        table.setFont(new Font("Verdana", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Verdana", Font.ITALIC, 15));
        setRowColSize(table);
        table.setAutoCreateRowSorter(true);
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
        
        PanelTableSouth panelTableSouth = new PanelTableSouth();
        this.add(panelTableSouth, BorderLayout.SOUTH);
    }}
    
    /**
     * This method set minimal widths of column
     * It is taken from https://stackoverflow.com/a/37561883
     * @param table JTable which columns have to be set
     * @param widths Minimal widths of each columns
     */
    private static void setColumnWidths(JTable table, int... widths) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMinWidth(widths[i]);
            }
            else break;
        }
    }
    
    /**
     * This method makes additional amendments to column widths and row heights
     * It is taken from https://stackoverflow.com/a/34433430
     * @param table JTable which columns have to be set
     */
    private static void setRowColSize(JTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
                int rowHeight = table.getRowHeight(row);

                for (int col = 1; col < table.getColumnCount(); col++) {
                    Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                    TableColumn column = table.getColumnModel().getColumn(col);
                    int colWidth = column.getWidth();

                    rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                    colWidth = comp.getPreferredSize().width;

                    column.setPreferredWidth(colWidth);
                }
                table.setRowHeight(row, rowHeight);
            }
    }
    /**
     * South JPanel of Panel Table, it contains all the JButtons and there JButtons are having added ActionListeners
     */
    private class PanelTableSouth extends JPanel { {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 30, 0, 30);
        
        this.add(searchAgain, gridBagConstraints);
        this.add(backTable, gridBagConstraints);
        
        searchAgain.addActionListener(buttonListener);
        backTable.addActionListener(buttonListener);
    }}
    
    /**
     * JPanel of Bar chart, there is bar chart added and sub panel
     */
    private class PanelChart extends JPanel { {
        this.setLayout(new BorderLayout());
        
        ChartPanel panelCrimeNumberBarChart = new ChartPanel(crimeNumberBarChart);
        this.add(panelCrimeNumberBarChart, BorderLayout.CENTER);
        
        PanelChartSouth panelChartSouth = new PanelChartSouth();
        this.add(panelChartSouth, BorderLayout.SOUTH);
    }}
    
    /**
     * South JPanel of chart panel, there are is JButton and JComboBox, both are having added ActionListener
     */
    private class PanelChartSouth extends JPanel { {      
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
     
        this.add(comboBoxCrimeTypeChart, gridBagConstraints);
        this.add(backChart, gridBagConstraints);
        
        backChart.addActionListener(buttonListener);
        comboBoxCrimeTypeChart.addActionListener(buttonListener);
    }}
    
    /**
     * JPanel of Data Quality Check operations, there are added sub panels
     */
    private class PanelDataQualityCheck extends JPanel { {
        this.setLayout(new BorderLayout());
        
        PanelDataQualityCheckNorth panelDataQualityCheckNorth = new PanelDataQualityCheckNorth();
        this.add(panelDataQualityCheckNorth, BorderLayout.CENTER);
        
        PanelDataQualityCheckSouth panelDataQualityCheckSouth = new PanelDataQualityCheckSouth();
        this.add(panelDataQualityCheckSouth, BorderLayout.SOUTH);
    }}
    
    /**
     * North panel of DQC, there is added JLabel with message
     */
    private class PanelDataQualityCheckNorth extends JPanel { {
        this.add(labelDataQualityCheckInformation);
    }}
    
    /**
     * South panel of DQC, there are added JButtons and ActionListeners to them
     */
    private class PanelDataQualityCheckSouth extends JPanel { {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        
        int indexY = 1;
        gridBagConstraints.gridy = indexY++;       
        this.add(exportEmptyCrimeID, gridBagConstraints);
        gridBagConstraints.gridy = indexY++;       
        this.add(exportDuplicates, gridBagConstraints);
        gridBagConstraints.gridy = indexY++;       
        this.add(removeEmptyCrimeID, gridBagConstraints);
        gridBagConstraints.gridy = indexY++;       
        this.add(backDataQualityCheck, gridBagConstraints);
        
        exportEmptyCrimeID.addActionListener(buttonListener);
        exportDuplicates.addActionListener(buttonListener);
        removeEmptyCrimeID.addActionListener(buttonListener);
        backDataQualityCheck.addActionListener(buttonListener);
    }}
}
