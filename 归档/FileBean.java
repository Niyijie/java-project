package com.company;

public class FileBean {
    public String name;
    public byte[] content;

    FileBean()
    {

    }

    FileBean(String name,byte[] content)
    {
        this.name = name;
        this.content = content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
