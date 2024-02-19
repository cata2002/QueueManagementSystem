package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks=new LinkedBlockingQueue<>();
    private AtomicInteger waitingPeriod=new AtomicInteger(0);

    public Server() {
    }

    public Server(BlockingQueue<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public int getSize(){
        return tasks.size();
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public synchronized void run() {
        while(true) {
            if (!tasks.isEmpty()) {
                try {
                    int sleepTime = tasks.peek().getServiceTime();
                    for (int i = 0; i < sleepTime; i++) {
                        try {
                            //int timeHere=sleepTime;
                            Thread.sleep(1000);
                            tasks.peek().setServiceTime(tasks.peek().getServiceTime()-1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (waitingPeriod.intValue() != 0)
                            waitingPeriod.getAndDecrement();
                    }
                    try {
                        tasks.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public int getWaitTime(){
        int sum=0;
        if(tasks==null) return 0;
        for(Task t:tasks){
            sum+=t.getServiceTime();
        }
        return sum;
    }


    public int getLowestTimeServer() {
        int sum = 0;
        int min=99999;
        int index=0;
        for (int i = 0; i < tasks.size(); i++) {
            try {
                Task t=tasks.take();
                sum+=t.getServiceTime();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(sum<min){
                min=sum;
                index=i;
            }
        }
        return index;
    }
}
