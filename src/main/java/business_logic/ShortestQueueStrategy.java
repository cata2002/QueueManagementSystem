package business_logic;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task){
        int index=0;
        int k=0;
        int minNr=999;
        for(Server s:servers){
            if(s.getSize()<minNr){
                minNr=s.getSize();
                index=k;
            }
            k++;
        }
        servers.get(index).addTask(task);
    }
}
