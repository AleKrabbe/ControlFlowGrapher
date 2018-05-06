package br.com.cfg.controlflowgrapher.model;

/**
 * @author bianca
 */
public class Token 
{
    public EnumToken name;
    public EnumToken attribute;
    public String value;
    public int lineNumber;
    //public STEntry tsPtr;
    
    public Token(EnumToken name)
    {
        this.name = name;
        attribute = EnumToken.UNDEF;
        lineNumber = -1;
        value = "";
        //tsPtr = null;
    }
    
    public Token(EnumToken name, EnumToken attr)
    {
        this.name = name;
        attribute = attr;
        value = "";
        //tsPtr = null;
    }
}
