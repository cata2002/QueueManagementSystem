package business_logic;

import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers=new ArrayList<>();
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler() {
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public void setMaxNoServers(int maxNoServers) {
        this.maxNoServers = maxNoServers;
    }

    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        for(int i=0;i<maxNoServers;i++){
            Server s=new Server();
            Thread t=new Thread(s);
            t.start();
            this.servers.add(s);
        }
        this.strategy=new ShortestQueueStrategy();
        this.maxTasksPerServer=maxTasksPerServer;
    }

    public void changeStrategy(SelectionPolicy selectionPolicy){
        if(selectionPolicy==SelectionPolicy.SHORTEST_TIME)
            strategy=new TimeStrategy();
        else strategy=new ShortestQueueStrategy();
    }

    public void dispatchTask(Task t){
        if(strategy instanceof ShortestQueueStrategy){
            changeStrategy(SelectionPolicy.SHORTEST_QUEUE);
            strategy.addTask(servers,t);
        }
        else if(strategy instanceof TimeStrategy){
            changeStrategy(SelectionPolicy.SHORTEST_TIME);
            strategy.addTask(servers,t);
        }
    }

    public int countTasks(){
        int sum=0;
        for(Server s:servers){
            sum+=s.getSize();
        }
        return sum;
    }

    public double getAvg(){
        int sum=0;
        for(Server s :servers){
            sum+=s.getWaitTime();
        }
        return sum/servers.size();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        int index=0;
        for(Server s:servers){
            ++index;
            sb.append("Queue "+ index+": ");
            for(Task t:s.getTasks()){
                sb.append(t.toString()+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
