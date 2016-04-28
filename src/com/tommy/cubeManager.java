package com.tommy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Tommy on 4/21/2016.
 */
public class cubeManager extends JFrame implements WindowListener {
    private JPanel root;
    private JTextField cubeSolverName;
    private JTextField newTime;
    private JButton addNewUserButton;
    private JTable cubeSolverTable;
    private JButton saveAndQuitButton;
    private JButton deleteSolverButton;
    private JButton updateSolverButton;

    cubeManager(final CubeSolverDataModel cubeSolverDataModel) {
        setContentPane(root);
        pack();
        setTitle("Rubik's Cube Record Keeper");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        cubeSolverTable.setGridColor(Color.BLACK);
        cubeSolverTable.setModel(cubeSolverDataModel);


        addNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String solverName = cubeSolverName.getText();

                if (solverName == null || solverName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a name");
                }

                float timeData;

                try {
                    timeData = Float.parseFloat(newTime.getText());
                    if (timeData < 0) {
                        throw new NumberFormatException("Time needs to be more than 0");
                    }
                }catch (NumberFormatException ne){
                    JOptionPane.showMessageDialog(rootPane, "Time needs to be more than 0");
                    return;
                }
                System.out.println("Adding " + solverName + " " + timeData);
                boolean insertedRow = cubeSolverDataModel.insertRow(solverName, timeData);

                if (!insertedRow){
                    JOptionPane.showMessageDialog(rootPane, "Error adding new cube solver");
                }

                cubeSolverName.setText("");
                newTime.setText("");
            }
        });

        saveAndQuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CubeDatabase.shutdown();
                System.exit(0);
            }
        });


        deleteSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = cubeSolverTable.getSelectedRow();

                if(currentRow == -1){
                    JOptionPane.showMessageDialog(rootPane,"Please choose a solver to delete");
                }
                boolean deleted = cubeSolverDataModel.deleteRow(currentRow);
                if(deleted){
                    CubeDatabase.loadAllNames();
                }else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting cube solver");
                }
            }
        });

        //working on this one.
//        updateSolverButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String updateName = cubeSolverName.getText();
//                float updateTime;
//                updateTime = Float.parseFloat(newTime.getText());
//                String updateCubeSolver = "UPDATE Cube_Solvers SET
//            }
//        });
    }
    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        CubeDatabase.shutdown();}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
