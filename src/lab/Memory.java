package lab;

/**
 * Created by Kevin on 6/1/15.
 */
public class Memory {

    public String Memorypool;

    public Memory(String Mp)
    {
        Memorypool = Mp;
    }

    public boolean isanumber(String temp) throws Exception{
        for(int i=0; i<temp.length();i++)
        {
            if(temp.charAt(i)<'0'||(temp.charAt(i)>'9'&&temp.charAt(i)<'A')||(temp.charAt(i)>'F'&&temp.charAt(i)<'a')||temp.charAt(i)>'f')
                return  false;
        }
        if(temp.length() == 0) return false;
        return true;
    }

    public String extract(String address, int start, int end, boolean revert) throws Exception{
        int i = Integer.parseInt(address, 16) * 2 ;
        int memlen = Memorypool.length();
        String instuct= "";


        if (i < memlen) {
                instuct = Memorypool.substring(i + start, i + end);
        }
        String result= "";
        if(revert)
        {
            for(int j= instuct.length(); j > 0; j-=2)
            {
                result += instuct.substring(j-2, j);
            }
        }
        else
        {
            result = instuct;
        }
        return  result;
    }

    public void writememory(String address, String content) throws Exception{
        address = address.replaceAll("0x", "");
        content = content.replaceAll("0x", "");
        int i = Integer.parseInt(address, 16) * 2 ;
        int memlen = Memorypool.length(), conlen = content.length();
        String recontent= "";
        for(int j= conlen; j > 0; j-=2)
        {
            recontent += content.substring(j - 2, j);
        }
        String temp = "";
        if(i >= memlen)
        {
            for(int j = memlen; j<i;j++)
                temp += "-";
            temp += recontent;
            Memorypool += temp;
        }
        else
        {
            if(i+conlen<memlen)
                Memorypool = Memorypool.substring(0,i) + recontent + Memorypool.substring(i+conlen, memlen);
            else
            {
                Memorypool = Memorypool.substring(0,i) + recontent;
            }
        }
    }

    public String fetchmemory(String address) throws Exception {
        int i = Integer.parseInt(address, 16) * 2 ;
        int memlen = Memorypool.length();
        if (i+8 <= memlen) {
            String temp = Memorypool.substring(i, i + 8);
            if(!temp.contains("-"))
                return "0x"+temp;
            else
                return "error";
        }
        else
            return "error";
    }

    public String fetchmemoryandreverse(String address) throws Exception {
        int i = Integer.parseInt(address, 16) * 2 ;
        int memlen = Memorypool.length();
        if (i+8 <= memlen) {
            String temp = Memorypool.substring(i, i + 8);
            if(!temp.contains("-")) {
                String result= "";
                for(int j= temp.length(); j > 0; j-=2)
                {
                    result += temp.substring(j - 2, j);
                }
                return "0x" + result;
            }
            else
                return "error";
        }
        else
            return "error";
    }

}
