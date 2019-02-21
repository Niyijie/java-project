package com.company;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * StudentUI
 */
class StudentUI extends JFrame{

    private JLabel lblIcon ;

    public StudentUI(){
        init();
    }

    private void init() {
        this.setTitle("学生端");
        this.setBounds(0, 0, 1280, 800);
        this.setLayout(null);

        //标签空间
        lblIcon = new JLabel();
        lblIcon.setBounds(0, 0, 1280, 800);

        //图标
        ImageIcon icon = new ImageIcon("/Users/niyijie/Desktop/大数据/vim.png");
        lblIcon.setIcon(icon);
        this.add(lblIcon);
        this.setVisible(true);
    }

    /**
     * 更新图标
     */
    public void updateIcon(byte[] dataBytes){
        ImageIcon icon = new ImageIcon(dataBytes);
        lblIcon.setIcon(icon);
    }
}