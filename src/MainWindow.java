import fuzzyverse.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainWindow extends JFrame {

    private JPanel panel1;
    private JLabel titleLbl;
    private JLabel urlLbl;
    private JLabel numThreadsLbl;
    private JLabel wordlistLbl;
    private JTextField threadsTextBox;
    private JTextField wordlistTextBox;
    private JButton browseBtn;
    private JLabel resultsLbl;
    private JTextField urlTextBox;
    private JComboBox filterComboBox;
    private JTextField filterTextBox;
    private JButton createFilterBtn;
    private JButton deleteFilterBtn;
    private JButton runBtn;
    private JTable resultsTable;
    private JTable filterTable;
    private JRadioButton GETRadioButton;
    private JRadioButton POSTRadioButton;
    private JButton httpHeaderButton;
    private JButton cookieButton;

    private List<UrlInfo> _urls;
    private List<UrlInfo> _displayUrls;
    private List<FilterItem> _filters;

    private UrlFilter _urlFilter;

    public MainWindow(String title)
    {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();

        _urls = new ArrayList<>();
        _filters = new ArrayList<>();
        _urlFilter = new UrlFilter();

        SetupFiltersComboBox();
        SetupTestingData();

        createFilterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateFilter();
            }
        });
        deleteFilterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteFilter();
            }
        });
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RunScan();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseWordlist();
            }
        });
    }

    public void ChooseWordlist()
    {
        JFileChooser chooser = new JFileChooser();
        var ret = chooser.showOpenDialog(this);

        if(ret == JFileChooser.APPROVE_OPTION)
        {
            wordlistTextBox.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
        }
    }

    public void CreateFilter()
    {
        if(filterTextBox.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Invalid value for filter");
            return;
        }

        FilterItem filter = new FilterItem();
        filter.Type = _urlFilter.GetFilterTypeFromString(filterComboBox.getSelectedItem().toString());
        filter.Value = filterTextBox.getText();

        _filters.add(filter);

        UpdateFilterTableData(_filters);

        _displayUrls = _urlFilter.Execute(_urls, _filters);
        UpdateUrlTableData(_displayUrls);

        filterTextBox.setText("");
    }

    public void DeleteFilter()
    {
        DefaultTableModel model = (DefaultTableModel)filterTable.getModel();

        var selectedrow = filterTable.getSelectedRow();

        if(selectedrow != -1) {
            _filters.remove(selectedrow);
        }

        UpdateFilterTableData(_filters);

        _displayUrls = _urlFilter.Execute(_urls, _filters);
        UpdateUrlTableData(_displayUrls);
    }

    public void RunScan() throws IOException, InterruptedException {
        Request request = new Request();


        // Remember to switch out the replace with word from wordlist
        request.GetRequest(urlTextBox.getText().replace("{}", ""));
    }

    public static void main(String[] args)
    {
        JFrame frame = new MainWindow("Fuzzy Verse");
        frame.setVisible(true);
    }

    private void SetupTestingData()
    {
        int r1min = 0;
        int r1max = 2;
        int r2min = 0;
        int r2max = 7;

        int[] retcodes = { 200, 501, 301, 500 };
        int[] sizes = { 31, 205, 1231, 26435, 532, 7522, 376, 4733 };

        Random rand = new Random();

        for(int i = 0; i < 20; i++)
        {
            int random1 = rand.nextInt(4);
            int random2 = rand.nextInt(8);

            UrlInfo info = new UrlInfo();
            info.ReturnCode = retcodes[random1];
            info.Size = sizes[random2];
            info.Url = urlTextBox.getText().replace("{}", "Test" + i);

            _urls.add(info);
        }

        FilterItem item = new FilterItem();
        item.Type = FilterType.SizeIsGreaterThan;
        item.Value = "2000";
        _filters.add(item);

        _displayUrls = _urlFilter.Execute(_urls, _filters);

        UpdateUrlTableData(_displayUrls);

        UpdateFilterTableData(_filters);

    }

    private void SetupFiltersComboBox()
    {
        filterComboBox.addItem("Return Code Is");
        filterComboBox.addItem("Return Code Is Not");
        filterComboBox.addItem("Size Is");
        filterComboBox.addItem("Size Is Not");
        filterComboBox.addItem("Size Is Less Than");
        filterComboBox.addItem("Size Is Greater Than");
        filterComboBox.addItem("Domain Contains");
    }

    private Object[][] ConvertUrlListToObjectArray(List<UrlInfo> urls)
    {
        Object[][] output = new Object[urls.size()][];

        for(int i = 0; i < urls.size(); i++)
        {
            output[i] = new Object[]{ urls.get(i).Url, urls.get(i).Size, urls.get(i).ReturnCode };
        }

        return output;
    }

    private Object[][] ConvertFilterListToObjectArray(List<FilterItem> filters)
    {
        Object[][] output = new Object[filters.size()][];

        for(int i = 0; i < filters.size(); i++)
        {
            output[i] = new Object[]{ _urlFilter.GetFilterTypeString(filters.get(i).Type), filters.get(i).Value };
        }

        return output;
    }

    private void UpdateUrlTableData(List<UrlInfo> inputData)
    {

        TableModel dataModel = new AbstractTableModel()
        {
            protected String[] columnNames = { "Domain", "Size", "Return Code" };

            protected Class[] columnClasses = new Class[] { String.class, Integer.class, Integer.class };

            public Object[][] data = ConvertUrlListToObjectArray(inputData);

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public String getColumnName(int col) {
                return columnNames[col];
            }

            @Override
            public int getColumnCount() {
                return data[0].length;
            }

            @Override
            public int getRowCount(){
                return data.length;
            }

            @Override
            public Object getValueAt(int row, int col){
                return data[row][col];
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                super.setValueAt(aValue, rowIndex, columnIndex);
            }
        };

        resultsTable.setModel(dataModel);
    }

    private void UpdateFilterTableData(List<FilterItem> filters)
    {
        Object[] columnNames = { "Filter Type", "Value" };
        DefaultTableModel filterModel = new DefaultTableModel(ConvertFilterListToObjectArray(_filters), columnNames)
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        filterTable.setModel(filterModel);
    }
}
