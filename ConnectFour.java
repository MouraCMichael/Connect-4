/****************************************************************************************************************************************************
 *  @Author:         Corey M. Moura
 *  @Date:           February 17, 2018
 *  @Professor:      Dr. Trafftz
 *  @Project:        Project 2 of CS163: Connect-4 Game, player vs computer 
 *  @Notes:          Run this programs main to play the game  
/****************************************************************************************************************************************************/


import javax.swing.JFrame;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//import viewPackage.*;

public class ConnectFour{

    //-----------------------------------------------------------------
    //  Creates and displays the main program frame.
    //-----------------------------------------------------------------
    public static void main (String[] args)
    {

        JMenuBar menus;
        JMenu fileMenu;
        JMenuItem quitItem;
        JMenuItem gameItem;   

        JFrame frame = new JFrame ("Connect Four");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("quit");
        gameItem = new JMenuItem("new game");

        fileMenu.add(gameItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        frame.setJMenuBar(menus);
        menus.add(fileMenu);

        ConnectFourPanel panel = new ConnectFourPanel(quitItem,gameItem);
        frame.getContentPane().add(panel);

        frame.setSize(625, 625);
        frame.setVisible(true);
        
        
        
    } 
}
