package lab;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.util.Timer;
import java.util.TimerTask;

public class testframe {
    private JPanel frame;
    private JTextArea ACode;
    private JTextField FileLocation;
    private JButton loadButton;
    private JButton BrowserButton;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JButton resetButton;
    private JButton stepButton;
    private JButton pauseButton;
    private JButton startButton;
    private JSlider CpuRate;
    private JRadioButton outputToFile;
    private JLabel F_ins;
    private JLabel D_ins;
    private JLabel E_ins;
    private JLabel M_ins;
    private JLabel W_ins;
    private JLabel W_valE;
    private JLabel Cnd;
    private JLabel E_valC;
    private JLabel W_valM;
    private JLabel W_dstE;
    private JLabel W_dstM;
    private JLabel M_valE;
    private JLabel M_valA;
    private JLabel M_dstE;
    private JLabel M_dstM;
    private JLabel E_valA;
    private JLabel E_valB;
    private JLabel E_dstE;
    private JLabel E_dstM;
    private JLabel E_srcA;
    private JLabel E_srcB;
    private JLabel rA;
    private JLabel rB;
    private JLabel D_valC;
    private JLabel D_valP;
    private JLabel PredPC;
    private JTable Memtable;
    private JScrollPane Memorypanel;
    private JLabel clockcycle;
    private JLabel CC_ZF;
    private JLabel CC_SF;
    private JLabel CC_OF;
    private JTextField fetchmemory;
    private JLabel r_eax;
    private JLabel r_ecx;
    private JLabel r_edx;
    private JLabel r_ebx;
    private JLabel r_esi;
    private JLabel r_edi;
    private JLabel r_esp;
    private JLabel r_ebp;
    private JButton MClear;
    private JButton fetchButton;
    private JLabel W_stat;
    private JLabel M_stat;
    private JLabel E_stat;
    private JLabel D_stat;
    private JLabel F_stat;

    private String[] address;
    private String[] command;
    private int commandnum;

    private String Memorypool;
    private String outputString;
    private String filename;
    private Map<String, String> updates;
    Update ud = new Update();

    Memory mr;

    public boolean isstart;
    public boolean isover;

    private void createUIComponents() {
        String[] cn = {"Address", "Value"};
        String[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, cn);
        Memtable = new JTable(model);
    }

    public final class YoFileFilter extends FileFilter {

        private String extension;

        private String description;

        public YoFileFilter(String extension, String description) {
            super();
            this.extension = extension;
            this.description = description;
        }

        public boolean accept(File f) {
            if (f != null) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = getExtension(f);
                if (extension != null && extension.equalsIgnoreCase(this.extension)) {
                    return true;
                }
            }
            return false;
        }

        public String getDescription() {
            return description;
        }

        private String getExtension(File f) {
            if (f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');
                if (i > 0 && i < filename.length() - 1) {
                    return filename.substring(i + 1).toLowerCase();
                }
            }
            return null;
        }

    }

    public String readfile(File file) {
        String TrimmedInput = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            filename = file.getName().replace(".yo", ".txt");
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String[] OString = tempString.split(":|\\|");
                String[] O2String = tempString.split("\\||#");
                address[commandnum++] = OString[0].trim();
                if(!address[commandnum-1].equals("")) {
                    command[commandnum - 1] = OString[1].trim();
                    if(command[commandnum - 1].equals("") && commandnum>=2)
                    {
                        int len = (Integer.parseInt(address[commandnum - 1].replace("0x",""),16)
                                - Integer.parseInt(address[commandnum - 2].replace("0x",""),16))*2 - command[commandnum-2].length();
                        for(int i=0;i<len;i++)
                        {
                            Memorypool +="-";
                        }
                    }
                    else {
                        Memorypool += command[commandnum - 1];
                    }
                    TrimmedInput = TrimmedInput + address[commandnum - 1] + "\t|\t" + O2String[1] + "\n";
                }
                else commandnum--;
            }
            mr = new Memory(Memorypool);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return TrimmedInput;
    }

    public void updateentry(JLabel jl, String ptr, int xptr) {
        if(!ptr.equals("int"))
            jl.setText(ptr);
        else
            jl.setText(String.format("%d", xptr));
    }

    public void resetupdates() {
        updates.put("f_pc", "0x00000000");

        updates.put("%ebp", "0x00000000");
        updates.put("%esp", "0x00000000");
        updates.put("%esi", "0x00000000");
        updates.put("%edi", "0x00000000");
        updates.put("%eax", "0x00000000");
        updates.put("%ebx", "0x00000000");
        updates.put("%edx", "0x00000000");
        updates.put("%ecx", "0x00000000");
        updates.put("———", "0x00000000");

        updates.put("D_ins", "nop");
        updates.put("E_ins", "nop");
        updates.put("M_ins", "nop");
        updates.put("W_ins", "nop");

        updates.put("W_icode", "0");
        updates.put("W_valE", "0x00000000");
        updates.put("W_valM", "0x00000000");
        updates.put("W_dstE", "———");
        updates.put("W_dstM", "———");

        updates.put("m_valM", "0x00000000");
        updates.put("M_icode", "0");
        updates.put("M_ifun", "0");
        updates.put("M_Bch", "0");
        updates.put("M_dstE", "———");
        updates.put("M_dstM", "———");
        updates.put("M_valA", "0x00000000");
        updates.put("M_valE", "0x00000000");
        updates.put("Cnd", "0");

        updates.put("e_Bch", "0");
        updates.put("e_valE", "0x00000000");
        updates.put("E_icode", "0");
        updates.put("E_ifun", "0");
        updates.put("E_dstE", "———");
        updates.put("E_dstM", "———");
        updates.put("E_srcA", "———");
        updates.put("E_srcB", "———");
        updates.put("E_valA", "0x00000000");
        updates.put("E_valB", "0x00000000");
        updates.put("E_valC", "0x00000000");

        updates.put("D_icode", "0");
        updates.put("D_ifun", "0");
        updates.put("D_valC", "0x00000000");
        updates.put("D_valP", "0x00000000");
        updates.put("D_rA", "8");
        updates.put("D_rB", "8");


        updates.put("PredPC", "0x00000000");

        updates.put("W_stat", "AOK ");
        updates.put("M_stat", "AOK ");
        updates.put("E_stat", "AOK ");
        updates.put("D_stat", "AOK ");
        updates.put("F_stat", "AOK ");

        updates.put("CC_OF", "0");
        updates.put("CC_SF", "0");
        updates.put("CC_ZF", "0");

    }

    private String itsmyfault(String fault) {
        if(fault.equals("———")) return "8";
        else return fault;
    }

    private String itsmyfault2(String fault){
        if (fault.equals("1"))
        {
            return "true";
        }
        else return "false";
    }

    private String regname(String regid){
        if(regid.equals("0")) return "%eax";
        if(regid.equals("1")) return "%ecx";
        if(regid.equals("2")) return "%edx";
        if(regid.equals("3")) return "%ebx";
        if(regid.equals("4")) return "%esp";
        if(regid.equals("5")) return "%ebp";
        if(regid.equals("6")) return "%esi";
        if(regid.equals("7")) return "%edi";
        if(regid.equals("8")) return "———";
        return "———";
    }

    private void setvalues() {
        r_edi.setText(updates.get("%edi"));
        r_esi.setText(updates.get("%esi"));
        r_esp.setText(updates.get("%esp"));
        r_ebp.setText(updates.get("%ebp"));
        r_eax.setText(updates.get("%eax"));
        r_ecx.setText(updates.get("%ecx"));
        r_edx.setText(updates.get("%edx"));
        r_ebx.setText(updates.get("%ebx"));

        D_ins.setText(updates.get("D_ins"));
        E_ins.setText(updates.get("E_ins"));
        M_ins.setText(updates.get("M_ins"));
        W_ins.setText(updates.get("W_ins"));

        D_stat.setText(updates.get("D_stat"));
        E_stat.setText(updates.get("E_stat"));
        M_stat.setText(updates.get("M_stat"));
        W_stat.setText(updates.get("W_stat"));

        PredPC.setText(updates.get("PredPC"));
        rA.setText(regname(updates.get("D_rA")));
        rB.setText(regname(updates.get("D_rB")));
        D_valP.setText(updates.get("D_valP"));
        D_valC.setText(updates.get("D_valC"));

        E_valA.setText(updates.get("E_valA"));
        E_valB.setText(updates.get("E_valB"));
        E_valC.setText(updates.get("E_valC"));
        E_srcB.setText(regname(updates.get("E_srcB")));
        E_srcA.setText(regname(updates.get("E_srcA")));
        E_dstM.setText(regname(updates.get("E_dstM")));
        E_dstE.setText(regname(updates.get("E_dstE")));

        Cnd.setText(updates.get("Cnd"));
        M_valE.setText(updates.get("M_valE"));
        M_valA.setText(updates.get("M_valA"));
        M_dstM.setText(regname(updates.get("M_dstM")));
        M_dstE.setText(regname(updates.get("M_dstE")));

        W_dstE.setText(regname(updates.get("W_dstE")));
        W_valM.setText(updates.get("W_valM"));
        W_dstM.setText(regname(updates.get("W_dstM")));
        W_valE.setText(updates.get("W_valE"));

        CC_OF.setText(updates.get("CC_OF"));
        CC_SF.setText(updates.get("CC_SF"));
        CC_ZF.setText(updates.get("CC_ZF"));

    }

    private void outputtofile() {
        if(outputToFile.isSelected()) {
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(filename));
                output.write(outputString);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeoutputString() {
        outputString += "Cycle_"+ clockcycle.getText() +"\r\n" +
                "--------------------\r\n" +
                "FETCH:\r\n" +
                "\tF_predPC \t= "+updates.get("PredPC")+"\r\n" +
                "\r\n" +
                "DECODE:\r\n" +
                "\tD_icode  \t= 0x"+updates.get("D_icode")+"\r\n" +
                "\tD_ifun   \t= 0x"+updates.get("D_ifun")+"\r\n" +
                "\tD_rA     \t= 0x"+itsmyfault(updates.get("D_rA"))+"\r\n" +
                "\tD_rB     \t= 0x"+itsmyfault(updates.get("D_rB"))+"\r\n" +
                "\tD_valC   \t= "+updates.get("D_valC")+"\r\n" +
                "\tD_valP   \t= "+updates.get("D_valP")+"\r\n" +
                "\r\n" +
                "EXECUTE:\r\n" +
                "\tE_icode  \t= 0x"+updates.get("E_icode")+"\r\n" +
                "\tE_ifun   \t= 0x"+updates.get("E_ifun")+"\r\n" +
                "\tE_valC   \t= "+updates.get("E_valC")+"\r\n" +
                "\tE_valA   \t= "+updates.get("E_valA")+"\r\n" +
                "\tE_valB   \t= "+updates.get("E_valB")+"\r\n" +
                "\tE_dstE   \t= 0x"+itsmyfault(updates.get("E_dstE"))+"\r\n" +
                "\tE_dstM   \t= 0x"+itsmyfault(updates.get("E_dstM"))+"\r\n" +
                "\tE_srcA   \t= 0x"+itsmyfault(updates.get("E_srcA"))+"\r\n" +
                "\tE_srcB   \t= 0x"+itsmyfault(updates.get("E_srcB"))+"\r\n" +
                "\r\n" +
                "MEMORY:\r\n" +
                "\tM_icode  \t= 0x"+updates.get("M_icode")+"\r\n" +
                "\tM_Bch    \t= "+itsmyfault2(updates.get("Cnd"))+"\r\n" +
                "\tM_valE   \t= "+updates.get("M_valE")+"\r\n" +
                "\tM_valA   \t= "+updates.get("M_valA")+"\r\n" +
                "\tM_dstE   \t= 0x"+itsmyfault(updates.get("M_dstE"))+"\r\n" +
                "\tM_dstM   \t= 0x"+itsmyfault(updates.get("M_dstM"))+"\r\n" +
                "\r\n" +
                "WRITE BACK:\r\n" +
                "\tW_icode  \t= 0x"+updates.get("W_icode")+"\r\n" +
                "\tW_valE   \t= "+updates.get("W_valE")+"\r\n" +
                "\tW_valM   \t= "+updates.get("W_valM")+"\r\n" +
                "\tW_dstE   \t= 0x"+itsmyfault(updates.get("W_dstE"))+"\r\n" +
                "\tW_dstM   \t= 0x"+itsmyfault(updates.get("W_dstM"))+"\r\n\r\n";
    }

    private class ActionHandler implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {

            if(e.getSource() == startButton)
            {
                isstart = true;
            }
            else if(e.getSource() == pauseButton)
            {
                isstart = false;
            }
            else if(e.getSource() == resetButton)
            {
                resetupdates();
                setvalues();
                clockcycle.setText("0");
                isstart = false;
                isover = false;
                startButton.setEnabled(true);
                stepButton.setEnabled(true);
            }
            else if(e.getSource() == stepButton)
            {
                isstart =false;
                if(!isover) {
                    try {
                        updates = ud.updatestage(updates, mr);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    setvalues();
                    int m_clock = Integer.parseInt(clockcycle.getText());
                    updateentry(clockcycle, "int", m_clock + 1);
                    makeoutputString();
                    if (!updates.get("W_stat").equals("AOK "))
                    {
                        isover = true;
                        startButton.setEnabled(false);
                        stepButton.setEnabled(false);
                        outputtofile();
                    }
                }
            }

            if (isstart && !isover) {
                final Timer timer = new Timer();
                TimerTask tt=new TimerTask() {
                    public void run() {
                        if(!isstart)
                        {
                            timer.cancel();
                        }
                        else {
                            try {
                                updates = ud.updatestage(updates, mr);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            setvalues();
                            int m_clock = Integer.parseInt(clockcycle.getText());
                            updateentry(clockcycle, "int", m_clock + 1);
                            makeoutputString();
                            if(!updates.get("W_stat").equals("AOK ")) {
                                isover = true;
                                isstart = false;
                                startButton.setEnabled(false);
                                stepButton.setEnabled(false);
                                outputtofile();
                            }
                        }

                    }
                };
                timer.schedule(tt,(int)((1.0/CpuRate.getValue())*1000),(int)((1.0/CpuRate.getValue())*1000));
            }
        }

    }

    public testframe() {

        /////////////////init////////////////////
        commandnum = 0;
        address = new String[1000];
        command = new String[1000];
        Memorypool = "";
        outputString = "";
        isstart = false;
        isover = false;
        updates = new TreeMap<String, String>();
        resetupdates();
        setvalues();
        makeoutputString();
        /////////////////////////////////////////

        ActionHandler ah = new ActionHandler();
        startButton.addActionListener(ah);
        pauseButton.addActionListener(ah);
        stepButton.addActionListener(ah);
        resetButton.addActionListener(ah);

        BrowserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser dialog = new JFileChooser();
                YoFileFilter yff = new YoFileFilter("yo", ".yo文件");
                dialog.addChoosableFileFilter(yff);
                dialog.setApproveButtonText("Choose");
                dialog.setCurrentDirectory(new File("/Users/Kevin/Desktop/Courseware/计算机原理/project/"));
                dialog.showOpenDialog(new JFrame());
                File file = dialog.getSelectedFile();
                String location;
                if (file!=null && file.exists()) {
                    resetupdates();
                    setvalues();
                    commandnum = 0;
                    clockcycle.setText("0");
                    isstart = false;
                    isover = false;
                    Memorypool = "";
                    outputString = "";
                    location = file.getPath();
                    FileLocation.setText(location);
                    ACode.setText(readfile(file));
                    startButton.setEnabled(true);
                    stepButton.setEnabled(true);
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String Input = FileLocation.getText();
                File file = new File(Input);
                if(!file.exists())
                {
                    JOptionPane.showMessageDialog(null, "File Not Exist!", "Warning", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    resetupdates();
                    setvalues();
                    commandnum = 0;
                    Memorypool = "";
                    outputString = "";
                    ud = new Update();
                    clockcycle.setText("0");
                    isstart = false;
                    isover = false;
                    ACode.setText(readfile(file));
                    startButton.setEnabled(true);
                    stepButton.setEnabled(true);
                }

            }
        });

        fetchmemory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String T_address = fetchmemory.getText();
                if(!T_address.equals("")) {
                    String F_address = T_address.replace("0x", "");
                    String result = "null";
                    try {
                        if (mr.isanumber(F_address))
                            result = mr.fetchmemory(F_address);
                        else result = "error!";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DefaultTableModel model = (DefaultTableModel) Memtable.getModel();
                    Vector row = new Vector();
                    row.add("0x" + F_address);
                    row.add(result);
                    model.addRow(row);
                }
            }
        });
        MClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultTableModel model = (DefaultTableModel) Memtable.getModel();
                if(model.getRowCount() > 0)
                    model.removeRow(0);
            }
        });
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String T_address = fetchmemory.getText();
                if(!T_address.equals("")) {
                    String F_address = T_address.replace("0x", "");
                    String result = "null";
                    try {
                        if (mr.isanumber(F_address))
                            result = mr.fetchmemory(F_address);
                        else result = "error!";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DefaultTableModel model = (DefaultTableModel) Memtable.getModel();
                    Vector row = new Vector();
                    row.add("0x" + F_address);
                    row.add(result);
                    model.addRow(row);
                }
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Y86 PIPE Simulator");
        frame.setContentPane(new testframe().frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
