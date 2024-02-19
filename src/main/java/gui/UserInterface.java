package gui;

import business_logic.SelectionPolicy;
import business_logic.SimulationManager;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserInterface extends JFrame {
    public boolean started=false;
    private JLabel noClients;
    private JLabel noQueues;
    private JLabel minServTime;
    private JLabel maxServTime;
    private JLabel minArrive;
    private JLabel maxArrive;
    private JLabel strategy;
    private JLabel simTime;
    private static JTextField noClientsField;
    private static JTextField noQueuesField;
    private static JTextField minServTimeField;
    private static JTextField maxServTimeField;
    private static JTextField minArriveField;
    private static JTextField maxArriveField;
    private static JTextField simTimeField;
    private static JComboBox strategies;
    private static JTextArea queues;
    private JButton increment;
    private JLabel currTime;
    private JTextField currTimeField;

    public JButton getIncrement() {
        return increment;
    }

    public UserInterface() {
        //SimulationManager s=new SimulationManager();
        //Thread th=new Thread(s);
        //th.start();
        started=false;
        this.setBounds(100, 100, 1020, 665);
        //this.setSize(1080,720);
        this.setTitle("Queue management");
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(173,216,230));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Queues simulation");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titleLabel.setBounds(400, 50, 400, 35);
        this.getContentPane().add(titleLabel);

        noClients =new JLabel("Number of clients");
        noClients.setBounds(100,80,140,20);
        this.getContentPane().add(noClients);
        //this.setVisible(true);

        noQueues =new JLabel("Number of queues");
        noQueues.setBounds(100,120,140,20);
        this.getContentPane().add(noQueues);
        //this.setVisible(true);

        minServTime =new JLabel("Min service time");
        minServTime.setBounds(100,160,140,20);
        this.getContentPane().add(minServTime);
        //this.setVisible(true);

        maxServTime =new JLabel("Max service time");
        maxServTime.setBounds(100,200,140,20);
        this.getContentPane().add(maxServTime);
        //this.setVisible(true);

        minArrive =new JLabel("Min arrival time");
        minArrive.setBounds(100,240,140,20);
        this.getContentPane().add(minArrive);
        //this.setVisible(true);

        maxArrive =new JLabel("Max arrival time");
        maxArrive.setBounds(100,280,140,20);
        this.getContentPane().add(maxArrive);
        //this.setVisible(true);

        strategy =new JLabel("Strategy");
        strategy.setBounds(100,320,140,20);
        this.getContentPane().add(strategy);

        simTime=new JLabel("Max sim time");
        simTime.setBounds(100,360,140,20);
        this.getContentPane().add(simTime);

        noClientsField=new JTextField();
        noClientsField.setBounds(220,80,60,20);
        this.getContentPane().add(noClientsField);

        noQueuesField=new JTextField();
        noQueuesField.setBounds(220,120,60,20);
        this.getContentPane().add(noQueuesField);

        minServTimeField=new JTextField();
        minServTimeField.setBounds(220,160,60,20);
        this.getContentPane().add(minServTimeField);

        maxServTimeField=new JTextField();
        maxServTimeField.setBounds(220,200,60,20);
        this.getContentPane().add(maxServTimeField);

        minArriveField=new JTextField();
        minArriveField.setBounds(220,240,60,20);
        this.getContentPane().add(minArriveField);

        maxArriveField=new JTextField();
        maxArriveField.setBounds(220,280,60,20);
        this.getContentPane().add(maxArriveField);

        String []strats={"Shortest time","Shortest queue"};
        strategies=new JComboBox(strats);
        strategies.setBounds(180,320,100,20);
        this.getContentPane().add(strategies);

        simTimeField=new JTextField();
        simTimeField.setBounds(220,360,60,20);
        this.getContentPane().add(simTimeField);

        queues=new JTextArea();
        queues.setBounds(320,100,400,450);
        this.getContentPane().add(queues);

        JScrollPane sp = new JScrollPane(queues);
        sp.setBounds(320,100,400,450);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.getContentPane().add(sp);

        increment=new JButton("Start sim");
        increment.setBounds(750,120,120,40);
        this.getContentPane().add(increment);

        increment.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                //updateFrame();
                SimulationManager sim=new SimulationManager();
                Thread th=new Thread(sim);
                th.start();
            }
        });
        this.setVisible(true);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void initFrame(SimulationManager s){
        String clients=this.noClientsField.getText();
        s.numberofClients=Integer.parseInt(clients);
        //System.out.println(s.numberofClients);
        List<Task> taskuri=new ArrayList();
        s.generateRandomTasks();
        taskuri=s.getTasks();
        queues.append("Initial clients: ");
        for(int i=0;i<s.numberofClients;i++)
            queues.append(taskuri.get(i).toString()+" ");
        queues.append("\n");
    }

    public static void updateFrame(String message){
        queues.append(message);
    }

    public static void refreshFrame(){
        queues.setText("");
    }

    public static int getClients(){
        return Integer.parseInt(noClientsField.getText());
    }

    public static int getNoQueues(){
        return Integer.parseInt(noQueuesField.getText());
    }

    public static int getMinArrive(){
        return Integer.parseInt(minArriveField.getText());
    }

    public static int getMaxArrive(){
        return Integer.parseInt(maxArriveField.getText());
    }

    public static int getMinService(){
        return Integer.parseInt(minServTimeField.getText());
    }

    public static int getMaxService(){
        return Integer.parseInt(maxServTimeField.getText());
    }

    public  static int getSimTime(){
        return Integer.parseInt(simTimeField.getText());
    }

    public static SelectionPolicy getPolicyUI(){
        if(strategies.getSelectedItem().toString().equals("Shortest time")) return SelectionPolicy.SHORTEST_TIME;
        else return SelectionPolicy.SHORTEST_QUEUE;
    }

}
