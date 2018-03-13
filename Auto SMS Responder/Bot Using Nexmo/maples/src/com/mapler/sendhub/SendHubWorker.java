package com.mapler.sendhub;

import com.mapler.service.*;
import com.mapler.model.SModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.log4j.Logger;

/**
 *
 * @author none
 */
public class SendHubWorker implements ITask{
    public ScheduledExecutorService executor =  Executors.newScheduledThreadPool(10);
    private SModel sModel;
    
    private static Logger log = Logger.getLogger(SendHubWorker.class);
    private INotifier iNotifier;
    public SendHubWorker(INotifier iNotifier,SModel sModel){
        this.sModel = sModel;
        this.iNotifier = iNotifier;
    }
    @Override
    public void update() {
        try {
            iNotifier.notify("tbot:: Going to start working.");
            executor.execute(new SendHubClick(iNotifier, this.sModel)); 
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.debug("Tbot:: Error "+ex);
        }
    }

    @Override
    public void finish() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
