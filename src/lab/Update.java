package lab;


import javax.swing.*;
import java.util.*;

/**
 * Created by Kevin on 6/2/15.
 */
public class Update {

    public Update() {
        inst = new TreeMap<String, String>();
        inst.put("halt", "1");
        inst.put("nop", "0");
        inst.put("rrmovl", "2");
        inst.put("irmovl", "3");
        inst.put("rmmovl", "4");
        inst.put("mrmovl", "5");
        inst.put("opl", "6");
        inst.put("jxx", "7");
        inst.put("cmovxx", "2");
        inst.put("call", "8");
        inst.put("ret", "9");
        inst.put("pushl", "a");
        inst.put("popl", "b");
    }

    private boolean Findin(String astr, String[] barry) throws Exception {

        for(int i=0;i <barry.length;i++)
        {
            if(astr.equals(barry[i]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean Cond(String CC_ZF, String CC_SF, String CC_OF, String icode ,String ifun) throws Exception {
        int zf = Integer.parseInt(CC_ZF);
        int sf = Integer.parseInt(CC_SF);
        int of = Integer.parseInt(CC_OF);

        if(!icode.equals("7")&&!icode.equals("8")) return false;

        if(setins(icode,ifun).equals("jmp")||setins(icode,ifun).equals("call"))
        {
            return true;
        }
        else if(setins(icode,ifun).equals("je")&& (zf == 1) )
        {
            return true;
        }
        else if(setins(icode,ifun).equals("jne")&& (zf == 0) )
        {
            return true;
        }
        else if(setins(icode,ifun).equals("jle")&& ((zf == 1) | ((sf == 1)  ^ (of == 1))) )
        {
            return true;
        }
        else if(setins(icode,ifun).equals("jl")&& ((sf == 1)  ^ (of == 1)) )
        {
            return true;
        }
        else if(setins(icode,ifun).equals("jg")&& ((!((sf == 1)  ^ (of == 1))) & !(zf == 1)) )
        {
            return true;
        }
        else if(setins(icode,ifun).equals("jge")&& (!((sf == 1)  ^ (of == 1))) )
        {
            return true;
        }
        return false;
    }

    private String setvalP(String icode) throws Exception{
        if(icode.equals("nop")) return "1";
        if(icode.equals("halt")) return "1";
        if(icode.equals("rrmovl")) return "2";
        if(icode.equals("irmovl")) return "6";
        if(icode.equals("rmmovl")) return "6";
        if(icode.equals("mrmovl")) return "6";
        if(icode.equals("addl")) return "2";
        if(icode.equals("subl")) return "2";
        if(icode.equals("andl")) return "2";
        if(icode.equals("xorl")) return "2";
        if(icode.equals("jmp")) return "5";
        if(icode.equals("je")) return "5";
        if(icode.equals("jne")) return "5";
        if(icode.equals("jl")) return "5";
        if(icode.equals("jle")) return "5";
        if(icode.equals("jg")) return "5";
        if(icode.equals("jge")) return "5";
        if(icode.equals("call")) return "5";
        if(icode.equals("ret")) return "1";
        if(icode.equals("pushl")) return "2";
        if(icode.equals("popl")) return "2";
        return "error";
    }

    private String hexformat(int a) throws Exception{
        String temp = Integer.toHexString(a);
        int i = temp.length();
        while(i<8) {
            temp = "0" + temp;
            i++;
        }
        return "0x" + temp;
    }

    private int StringtoInt(String a) throws Exception{
        a = a.replace("0x", "");
        long valal = Long.parseLong(a, 16);
        long temp = 2147483647 + 1;
        int vala = 0;
        if(valal > 2147483647)
            vala = (int) (valal- 2* temp);
        else
            vala = (int) valal;
        return vala;
    }

    private String ALU(String a, String b, String ifun) throws Exception{

        a = a.replace("0x", "");
        b = b.replace("0x", "");
        int vala = StringtoInt(a), valb = StringtoInt(b);

        if(ifun.equals("0"))
            return hexformat(vala + valb);
        if(ifun.equals("1"))
            return hexformat(valb - vala);
        if(ifun.equals("2"))
            return hexformat(vala & valb);
        if(ifun.equals("3"))
            return hexformat(vala ^ valb);
        return "error";
    }

    private String setins(String icode, String ifun) throws Exception{

        if(icode.equals("1") && ifun.equals("0")) return "halt";
        if(icode.equals("0") && ifun.equals("0")) return "nop";
        if(icode.equals("2") && ifun.equals("0")) return "rrmovl";
        if(icode.equals("3") && ifun.equals("0")) return "irmovl";
        if(icode.equals("4") && ifun.equals("0")) return "rmmovl";
        if(icode.equals("5") && ifun.equals("0")) return "mrmovl";
        if(icode.equals("6") && ifun.equals("0")) return "addl";
        if(icode.equals("6") && ifun.equals("1")) return "subl";
        if(icode.equals("6") && ifun.equals("2")) return "addl";
        if(icode.equals("6") && ifun.equals("3")) return "xorl";
        if(icode.equals("7") && ifun.equals("0")) return "jmp";
        if(icode.equals("7") && ifun.equals("1")) return "jle";
        if(icode.equals("7") && ifun.equals("2")) return "jl";
        if(icode.equals("7") && ifun.equals("3")) return "je";
        if(icode.equals("7") && ifun.equals("4")) return "jne";
        if(icode.equals("7") && ifun.equals("5")) return "jge";
        if(icode.equals("7") && ifun.equals("6")) return "jg";
        if(icode.equals("8") && ifun.equals("0")) return "call";
        if(icode.equals("9") && ifun.equals("0")) return "ret";
        if(icode.equals("a") && ifun.equals("0")) return "pushl";
        if(icode.equals("b") && ifun.equals("0")) return "popl";
        return "error";

    }

    public Map<String, String> inst;

    private boolean need_regids(String icode) throws Exception {
        if(Findin(icode, new String[]{"2", "6", "a", "b", "3", "4", "5"}))
        {
            return true;
        }
        return false;
    }

    private String regname(String regid) throws Exception{
        if(regid.equals("0")) return "%eax";
        if(regid.equals("1")) return "%ecx";
        if(regid.equals("2")) return "%edx";
        if(regid.equals("3")) return "%ebx";
        if(regid.equals("4")) return "%esp";
        if(regid.equals("5")) return "%ebp";
        if(regid.equals("6")) return "%esi";
        if(regid.equals("7")) return "%edi";
        if(regid.equals("8")) return "———";
        if(regid.equals("———")) return "———";
        return "error";
    }

    public Map<String, String> updatestage(Map<String, String> m, Memory mr) throws Exception {

        Map<String, String> ref = new TreeMap<String, String>();

        ref.putAll(m);
        ///////////////////////////////////////////////////////F_Stage////////////////////////////////////////////////////////////////////////////



        if (ref.get("M_icode").equals(inst.get("jxx")) && ref.get("M_Bch").equals("0")) {
            m.put("f_pc", ref.get("M_valA"));
        } else if (ref.get("W_icode").equals(inst.get("ret"))) {
            m.put("f_pc", ref.get("W_valM"));
        } else m.put("f_pc", ref.get("PredPC"));


        m.put("D_icode", mr.extract(m.get("f_pc").replace("0x", ""), 0, 1, false));
        m.put("D_ifun", mr.extract(m.get("f_pc").replace("0x", ""), 1, 2, false));

        String ins = setins(m.get("D_icode"), m.get("D_ifun"));

        ////////////////////////Deal with halt/////////////////////
        if (!ins.equals("error")) {
            if (ins.equals("halt")) {
                m.put("D_ins", ins);
                m.put("D_stat", "HLT ");
            }
            else {
                m.put("D_ins", ins);
            }
        }
        else {
            m.put("D_stat", "INS ");
        }
        //////////////////////////fetch error ins after ret/halt////////////////////////////////
        if ((ref.get("D_icode").equals(inst.get("ret")) && m.get("D_stat").equals("INS "))
                || ((ref.get("D_icode").equals(inst.get("nop"))) && m.get("D_stat").equals("INS "))
                || (ref.get("D_icode").equals(inst.get("halt")) && m.get("D_stat").equals("INS "))) {
            m.put("D_icode", inst.get("nop"));
            m.put("D_ifun", "0");
            m.put("D_ins", "nop");
            m.put("D_stat", ref.get("D_stat"));
        }
        else {
            if (need_regids(m.get("D_icode")) && m.get("D_stat").equals("AOK ")) {
                m.put("D_rA", mr.extract(m.get("f_pc").replace("0x", ""), 2, 3, false));
                String rra = regname(m.get("D_rA"));
                if (rra.equals("error"))
                    m.put("D_stat", "INS ");
                m.put("D_rB", mr.extract(m.get("f_pc").replace("0x", ""), 3, 4, false));
                String rrb = regname(m.get("D_rB"));
                if (rrb.equals("error"))
                    m.put("D_stat", "INS ");
            } else {
                m.put("D_rA", "8");
                m.put("D_rB", "8");
            }
            String nicode = m.get("D_icode");

            if (!m.get("D_stat").equals("INS ")) {
                m.put("D_valP", ALU(m.get("f_pc"), setvalP(ins), "0"));
                if (Findin(nicode, new String[]{inst.get("irmovl"), inst.get("rmmovl"), inst.get("mrmovl")}))
                    m.put("D_valC", "0x" + mr.extract(m.get("f_pc").replace("0x", ""), 4, 12, true));
                else if (Findin(nicode, new String[]{inst.get("jxx"), inst.get("call")}))
                    m.put("D_valC", "0x" + mr.extract(m.get("f_pc").replace("0x", ""), 2, 10, true));
            }
        }

        if (Findin(m.get("D_icode"), new String[]{inst.get("jxx"), inst.get("call")})) {
            m.put("PredPC", m.get("D_valC"));
        }
        else {
            m.put("PredPC", m.get("D_valP"));
        }


        ///////////////////////////////////////////////////////D_Stage////////////////////////////////////////////////////////////////////////////

        if(!m.get("E_stat").equals("INS ")) {
            //////E_srcA//////
            if (Findin(ref.get("D_icode"), new String[]{inst.get("rrmovl"), inst.get("rmmovl"), inst.get("opl"), inst.get("pushl")}))
                m.put("E_srcA", ref.get("D_rA"));
            else if (Findin(ref.get("D_icode"), new String[]{inst.get("popl"), inst.get("ret")}))
                m.put("E_srcA", "4");
            else m.put("E_srcA", "———");

            //////E_srcB//////
            if (Findin(ref.get("D_icode"), new String[]{inst.get("mrmovl"), inst.get("rmmovl"), inst.get("opl")}))
                m.put("E_srcB", ref.get("D_rB"));
            else if (Findin(ref.get("D_icode"), new String[]{inst.get("popl"), inst.get("ret"), inst.get("call"), inst.get("pushl")}))
                m.put("E_srcB", "4");
            else m.put("E_srcB", "———");

            //////E_dstE//////
            if (Findin(ref.get("D_icode"), new String[]{inst.get("rrmovl"), inst.get("irmovl"), inst.get("opl")}))
                m.put("E_dstE", ref.get("D_rB"));
            else if (Findin(ref.get("D_icode"), new String[]{inst.get("popl"), inst.get("ret"), inst.get("call"), inst.get("pushl")}))
                m.put("E_dstE", "4");
            else m.put("E_dstE", "———");

            //////E_dstM//////
            if (Findin(ref.get("D_icode"), new String[]{inst.get("mrmovl"), inst.get("popl")}))
                m.put("E_dstM", ref.get("D_rA"));
            else m.put("E_dstM", "———");


            //////E_valA//////
            if (Findin(ref.get("D_icode"), new String[]{inst.get("call"), inst.get("jxx")}))
                m.put("E_valA", ref.get("D_valP"));
            else if (ref.get("E_dstE").equals(m.get("E_srcA")) && !m.get("E_srcA").equals("———"))
                m.put("E_valA", ref.get("e_valE"));
            else if (ref.get("M_dstM").equals(m.get("E_srcA")) && !m.get("E_srcA").equals("———"))
                m.put("E_valA", ref.get("m_valM"));
            else if (ref.get("M_dstE").equals(m.get("E_srcA")) && !m.get("E_srcA").equals("———"))
                m.put("E_valA", ref.get("M_valE"));
            else if (ref.get("W_dstM").equals(m.get("E_srcA")) && !m.get("E_srcA").equals("———"))
                m.put("E_valA", ref.get("W_valM"));
            else if (ref.get("W_dstE").equals(m.get("E_srcA")) && !m.get("E_srcA").equals("———"))
                m.put("E_valA", ref.get("W_valE"));
            else
                m.put("E_valA", ref.get(regname(m.get("E_srcA"))));

            //////E_valB//////
            if (ref.get("E_dstE").equals(m.get("E_srcB")) && !m.get("E_srcB").equals("———"))
                m.put("E_valB", ref.get("e_valE"));
            else if (ref.get("M_dstM").equals(m.get("E_srcB")) && !m.get("E_srcB").equals("———"))
                m.put("E_valB", ref.get("m_valM"));
            else if (ref.get("M_dstE").equals(m.get("E_srcB")) && !m.get("E_srcB").equals("———"))
                m.put("E_valB", ref.get("M_valE"));
            else if (ref.get("W_dstM").equals(m.get("E_srcB")) && !m.get("E_srcB").equals("———"))
                m.put("E_valB", ref.get("W_valM"));
            else if (ref.get("W_dstE").equals(m.get("E_srcB")) && !m.get("E_srcB").equals("———"))
                m.put("E_valB", ref.get("W_valE"));
            else
                m.put("E_valB", ref.get(regname(m.get("E_srcB"))));

            //////E_valC//////
            m.put("E_valC", ref.get("D_valC"));
            m.put("E_icode", ref.get("D_icode"));
            m.put("E_ifun", ref.get("D_ifun"));
            m.put("E_ins", ref.get("D_ins"));
            m.put("E_stat", ref.get("D_stat"));
        }
        ///////////////////////////////////////////////////////E_Stage////////////////////////////////////////////////////////////////////////////

        if (ref.get("E_ins").equals("halt")) {
            m.put("M_ins", "halt");
            m.put("M_stat", "HLT ");
        }
        else
        {
            m.put("M_ins", ref.get("E_ins"));
            m.put("M_stat", ref.get("E_stat"));
        }

        if(!m.get("E_stat").equals("INS ")) {


            String alua = "";
            if (Findin(m.get("E_icode"), new String[]{inst.get("rrmovl"), inst.get("opl")}))
                alua = m.get("E_valA");
            else if (Findin(m.get("E_icode"), new String[]{inst.get("irmovl"), inst.get("rmmovl"), inst.get("mrmovl")}))
                alua = m.get("E_valC");
            else if (Findin(m.get("E_icode"), new String[]{inst.get("call"), inst.get("pushl")}))
                alua = "-4";
            else if (Findin(m.get("E_icode"), new String[]{inst.get("ret"), inst.get("popl")}))
                alua = "4";


            String alub = "";

            if (Findin(m.get("E_icode"), new String[]{inst.get("rmmovl"), inst.get("mrmovl"), inst.get("opl"), inst.get("call"),
                    inst.get("pushl"), inst.get("ret"), inst.get("popl")}))
                alub = m.get("E_valB");
            else if (Findin(m.get("E_icode"), new String[]{inst.get("rrmovl"), inst.get("irmovl")}))
                alub = "0";

            String alufun = "0";

            if (m.get("E_icode").equals(inst.get("opl")))
                alufun = m.get("E_ifun");

            String alures = "";

            if (!alua.equals("") && !alub.equals("")) {
                alures = ALU(alua, alub, alufun);
                m.put("e_valE", alures);
            }

            boolean set_cc = false;
            if (m.get("E_icode").equals(inst.get("opl"))) {
                set_cc = true;
                m.put("CC_ZF", "0");
                m.put("CC_SF", "0");
                m.put("CC_OF", "0");
            }

            if (set_cc) {
                int vala = StringtoInt(alua), valb = StringtoInt(alub), res = StringtoInt(alures);
                if (res == 0) {
                    m.put("CC_ZF", "1");
                }
                if (res < 0) {
                    m.put("CC_SF", "1");
                }
                if (((vala < 0) && (valb < 0) && (res > 0)) || ((res < 0) && (vala > 0) && (valb > 0))) {
                    m.put("CC_OF", "1");
                }
            }


            if (Cond(m.get("CC_ZF"), m.get("CC_SF"), m.get("CC_OF"), m.get("E_icode"), m.get("E_ifun"))) {
                m.put("e_Bch", "1");
            } else {
                m.put("e_Bch", "0");
            }
        }

        m.put("M_icode", ref.get("E_icode"));
        m.put("M_ifun", ref.get("E_ifun"));
        m.put("M_Bch", ref.get("e_Bch"));
        m.put("Cnd", ref.get("e_Bch"));
        m.put("M_valE", ref.get("e_valE"));
        m.put("M_valA", ref.get("E_valA"));
        m.put("M_dstE", ref.get("E_dstE"));
        m.put("M_dstM", ref.get("E_dstM"));

        ///////////////////////////////////////////////////////M_Stage////////////////////////////////////////////////////////////////////////////

        if (ref.get("M_ins").equals("halt")) {
            m.put("W_ins", "halt");
            m.put("W_stat", "HLT ");
        }
        else
        {
            m.put("W_ins", ref.get("M_ins"));
            m.put("W_stat", ref.get("M_stat"));
        }

        if(!m.get("M_stat").equals("INS ")) {
            String mem_addr = "";
            if (Findin(m.get("M_icode"), new String[]{inst.get("rmmovl"), inst.get("mrmovl"), inst.get("call"), inst.get("pushl")}))
                mem_addr = m.get("M_valE");
            else if (Findin(m.get("M_icode"), new String[]{inst.get("popl"), inst.get("ret")}))
                mem_addr = m.get("M_valA");

            //////mem_read//////
            if (Findin(m.get("M_icode"), new String[]{inst.get("mrmovl"), inst.get("popl"), inst.get("ret")})) {
                String tempmem = mem_addr.replace("0x", "");
                if (StringtoInt(tempmem)<0) {
                    if (m.get("W_stat").equals("AOK "))
                    {
                        m.put("W_stat", "ADR ");
                    }
                }
                else {
                    tempmem = mr.fetchmemoryandreverse(tempmem);
                    if (!tempmem.equals("error"))
                        m.put("m_valM", tempmem);
                    else {
                        if (m.get("W_stat").equals("AOK ")) m.put("W_stat", "ADR ");
                    }
                }
            }
            //////mem_write//////
            else if (Findin(m.get("M_icode"), new String[]{inst.get("rmmovl"), inst.get("call"), inst.get("pushl")})) {
                if (StringtoInt(m.get("M_valE"))<0)
                {
                    if (m.get("W_stat").equals("AOK "))
                    {
                        m.put("W_stat", "ADR ");
                    }
                }
                else {
                    mr.writememory(m.get("M_valE"), m.get("M_valA"));
                }
            }
        }
        m.put("W_icode", ref.get("M_icode"));
        m.put("W_ins", ref.get("M_ins"));
        m.put("W_valE", ref.get("M_valE"));
        m.put("W_valM", ref.get("m_valM"));
        m.put("W_dstE", ref.get("M_dstE"));
        m.put("W_dstM", ref.get("M_dstM"));

        ///////////////////////////////////////////////////////W_Stage////////////////////////////////////////////////////////////////////////////

        if(!m.get("W_stat").equals("INS ")) {
            if (!m.get("W_dstE").equals("8")) {
                m.put(regname(m.get("W_dstE")), m.get("W_valE"));
            }
            if (!m.get("W_dstM").equals("8")) {
                m.put(regname(m.get("W_dstM")), m.get("W_valM"));
            }

        }
        /////////////////////////////////////////////////////Deal with stall/bubble///////////////////////////////////////////////////////////


        //////F_stall//////
        if (Findin(ref.get("E_icode"), new String[]{inst.get("mrmovl"), inst.get("popl")}) &&
                (!ref.get("E_dstM").equals("———") && Findin(ref.get("E_dstM"), new String[]{m.get("E_srcA"), m.get("E_srcB")})) ||
                Findin(inst.get("ret"), new String[]{ref.get("D_icode"), ref.get("E_icode"), ref.get("M_icode")}))
        {
            m.put("PredPC", ref.get("PredPC"));
        }

        //////D_stall//////
        if ((Findin(ref.get("E_icode"), new String[]{inst.get("mrmovl"), inst.get("popl")}) &&
                (!ref.get("E_dstM").equals("———") && Findin(ref.get("E_dstM"), new String[]{m.get("E_srcA"), m.get("E_srcB")}))))
        {
            m.put("D_icode", ref.get("D_icode"));
            m.put("D_ifun", ref.get("D_ifun"));
            m.put("D_ins", ref.get("D_ins"));
            m.put("D_valP", ref.get("D_valP"));
            m.put("D_valC", ref.get("D_valC"));
            m.put("D_rA", ref.get("D_rA"));
            m.put("D_rB", ref.get("D_rB"));
            m.put("D_stat", ref.get("D_stat"));
        }
        //////D_bubble//////
        else if((ref.get("E_icode").equals(inst.get("jxx"))&&ref.get("e_Bch").equals("0"))||
                Findin(inst.get("ret"), new String[]{ref.get("D_icode"), ref.get("E_icode"), ref.get("M_icode")}))
        {
            m.put("D_icode", "0");
            m.put("D_ifun", "0");
            m.put("D_ins", "nop");
            m.put("D_valP", "0x00000000");
            m.put("D_valC", "0x00000000");
            m.put("D_rA", "8");
            m.put("D_rB", "8");
            m.put("D_stat", "AOK ");
        }

        /////////////E_bubble/////////////////
        if ((ref.get("E_icode").equals(inst.get("jxx")) && ref.get("e_Bch").equals("0")) ||
                (Findin(ref.get("E_icode"), new String[]{inst.get("mrmovl"), inst.get("popl")}) &&
                        (!ref.get("E_dstM").equals("———") && Findin(ref.get("E_dstM"), new String[]{m.get("E_srcA"), m.get("E_srcB")})))) {
            m.put("E_icode", "0");
            m.put("E_ifun", "0");
            m.put("E_ins", "nop");
            m.put("E_valA", "0x00000000");
            m.put("E_valC", "0x00000000");
            m.put("E_valB", "0x00000000");
            m.put("E_srcA", "———");
            m.put("E_srcB", "———");
            m.put("E_dstE", "———");
            m.put("E_dstM", "———");
            m.put("E_stat", "AOK ");
        }

        return m;
    }

}
