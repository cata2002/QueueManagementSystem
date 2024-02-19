package business_logic;

import gui.UserInterface;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static gui.UserInterface.*;

public class SimulationManager implements Runnable{
    private Scheduler scheduler;
    private FileWriter f;

    {
        try {
            f = new FileWriter("log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Task> tasks=new ArrayList<>(2000);
    private SelectionPolicy selectionPolicy=getPolicyUI();
    public int timeLimit = getSimTime();
    public int maxProcessingTime = getMaxService();
    public int minProcessingTime = getMinService();
    public int numberOfServers = getNoQueues();

    public  int minArrivalTime=getMinArrive();
    public int maxArrivalTime=getMaxArrive();
    public int numberofClients =getClients();

    public int serviceTotal=0;

    //private UserInterface ui;


    public SimulationManager() {
        generateRandomTasks();
        this.scheduler=new Scheduler(numberOfServers,numberofClients);
        this.scheduler.changeStrategy(selectionPolicy);
        //this.ui=new UserInterface();
    }

    public SimulationManager(Scheduler scheduler, List<Task> tasks, SelectionPolicy selectionPolicy) {
        this.scheduler = scheduler;
        this.tasks = tasks;
        this.selectionPolicy = selectionPolicy;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public void generateRandomTasks(){
        int addedNo=0;
        //Random rand =new Random();
        for(int i=0;i<numberofClients;i++) {
            Task toAdd=new Task();
            toAdd.setID(i+1);
            toAdd.setArrivalTime(new Random().nextInt(minArrivalTime,maxArrivalTime));
            toAdd.setServiceTime(new Random().nextInt(minProcessingTime,maxProcessingTime));
            serviceTotal+=toAdd.getServiceTime();
            this.tasks.add(toAdd);
        }
        tasks.sort((o1, o2) -> Integer.compare(o1.getArrivalTime(), o2.getArrivalTime()));
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(Task t:tasks){
            sb.append(t.toString()+"\n");
        }
        return sb.toString();
    }

    public void printTasks(int currentTime, String tasks, String forScheduler){
        System.out.println("Time = "+currentTime);
        System.out.println("Tasks in queue: "+this.tasks.toString());
        System.out.println(scheduler.toString());
    }

    public String printGui(int time){
        StringBuilder sb=new StringBuilder();
        sb.append("Time = "+time+"\n");
        sb.append("Tasks in queue: "+this.tasks.toString()+"\n");
        sb.append(scheduler.toString()+"\n");
        return sb.toString();
    }

    @Override
    public synchronized void run() {
        int now = 1;
        double wait=0.0;
        int max=-1;
        int index=0;
            while (now <= timeLimit) {
                List<Task> taskList = new ArrayList<>();
                for (Task t : tasks) {
                    if (t.getArrivalTime() == now) {
                        scheduler.dispatchTask(t);
                        taskList.add(t);
                    }
                }
                tasks.removeAll(taskList);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //printTasks(now, "aa", "bb");
                String on = printGui(now);
                //System.out.println(now);
                updateFrame(on);
                if(scheduler.countTasks()>max){
                    max=scheduler.countTasks();
                    index=now;
                }
                wait+=scheduler.getAvg();
                try {f.write(on);} catch (IOException e) {
                    throw new RuntimeException(e);}
                now++;
                try {Thread.sleep(500);} catch (InterruptedException e) {
                    throw new RuntimeException(e);}
                //refreshFrame();
            }
            showMessage("Peak hour: "+index+"\nAverage service time: "+new DecimalFormat("0.##").format((double)serviceTotal/(numberofClients))+"\n Average waiting time: "+new DecimalFormat("0.##").format(wait/(numberofClients)));
        try {f.write("Peak hour: "+index+"\nAverage service time: "+new DecimalFormat("0.##").format((double)serviceTotal/(numberofClients))+"\n Average waiting time: "+new DecimalFormat("0.##").format(wait/(numberofClients)));} catch (IOException e) {
            throw new RuntimeException(e);}
        try {f.close();} catch (IOException e) {
            throw new RuntimeException(e);}
    }
}
